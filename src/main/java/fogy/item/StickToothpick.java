package fogy.item;

import java.util.Random;
import java.util.function.Consumer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import fogy.registry.ItemRegistry;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class StickToothpick extends Item {
	private static Item parentItem = Items.STICK;

	private static int maxStackSize = parentItem.getDefaultMaxStackSize();

	private static int useDuration = Integer.MAX_VALUE; // ticks

	private static Item treasureItem = Items.ROTTEN_FLESH;
	private static ItemStack treasure = new ItemStack(treasureItem, 1);
	private static float treasureChance = 0.25f / 60;
	private static final Random RANDOM = new Random();

	public StickToothpick(Properties pProperties) {
		super(pProperties);
	}

	public static void register() {
		Properties properties = new Properties();
		properties.stacksTo(maxStackSize);

		ItemRegistry.replaceItem("stick", () -> new StickToothpick(properties));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
		if (pLevel.isClientSide && pUsedHand == InteractionHand.MAIN_HAND) {
			pPlayer.startUsingItem(pUsedHand);
		}
		return super.use(pLevel, pPlayer, pUsedHand);
	}

	@Override
	public UseAnim getUseAnimation(ItemStack pStack) {
		return UseAnim.CUSTOM;
	}

	@Override
	public int getUseDuration(ItemStack pStack) {
		return useDuration;
	}

	@Override
	public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
		if (pLivingEntity instanceof Player) {
			Player player = (Player) pLivingEntity;
			if (pLevel.isClientSide) {
				ItemStack treasureCopy = treasure.copy();
				if (isTreasureFound()) {
					if (player.getInventory().add(treasureCopy)) {
						pLevel.playSound(player, player.getOnPos(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS);
					} else {
						player.drop(treasureCopy, false, false);
					}
				}
				int ticksPicking = player.getTicksUsingItem();
				if (ticksPicking != 0 && ticksPicking % 60 == 0) {
					pLevel.playSound(player, player.getOnPos(), SoundEvents.WOOD_HIT, SoundSource.PLAYERS);
				}
			}
		}
		super.onUseTick(pLevel, pLivingEntity, pStack, pRemainingUseDuration);
	}

	@Override
	public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {
		super.releaseUsing(pStack, pLevel, pLivingEntity, pTimeCharged);
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {

			@Override
			public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand,
					float partialTick, float equipProcess, float swingProcess) {
				if (player.isUsingItem()) {
					applyPickTransform(poseStack, player, arm, partialTick);
				}
				applyItemArmTransform(poseStack, arm, equipProcess);
				return true;
			}

			private void applyPickTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, float partialTick) {
			    float f = player.getTicksUsingItem();
			    float f1 = Math.max(1f - f / 27, 0f);

			    // Calculate the oscillation based on a sine wave
			    float oscillation = Mth.sin((f / 27.0F) * (float) Math.PI * 2) * 0.6F; // Adjust the multiplier for range

			    if (f > 10) {
			        float f2 = Mth.abs(Mth.cos(f / 4.0F * (float) Math.PI) * 0.1F);
			        poseStack.translate(0.0F, f2, 0.0F);
			    }
			    
			    float f3 = 1.0F - (float) Math.pow((double) f1, 27.0);
			    int i = arm == HumanoidArm.RIGHT ? 1 : -1;

			    // Use the oscillation value to translate the item left and right
			    poseStack.translate(oscillation * (float) i, f3 * -0.5F, f3 * 0.0F);
			    poseStack.mulPose(Axis.YP.rotationDegrees((float) i * f3 * 90.0F));
			    poseStack.mulPose(Axis.XP.rotationDegrees(f3 * 10.0F));
			    poseStack.mulPose(Axis.ZP.rotationDegrees((float) i * f3 * 30.0F));
			}

			private void applyItemArmTransform(PoseStack poseStack, HumanoidArm arm, float equipProcess) {
				int i = arm == HumanoidArm.RIGHT ? 1 : -1;
				poseStack.translate((float) i * 0.56F, -0.52F + equipProcess * -0.6F, -0.72F);
			}
		});
		super.initializeClient(consumer);
	}

	private boolean isTreasureFound() {
		return RANDOM.nextDouble() < treasureChance;
	}
}
