package fogy.item;

import java.util.function.Consumer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import fogy.main.FogyMain;
import fogy.registry.ItemRegistry;
import fogy.registry.SoundRegistry;
import fogy.util.Chatter;
import fogy.util.SoundPlayer;
import fogy.util.message.MessageManager;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.registries.RegistryObject;

public class StickToothpick extends Item {
	private static final String LOOT_TABLE_NAME = "toothpicking";
	private static final String MESSAGE_TABLE_NAME = "toothpicking";

	public StickToothpick(Properties pProperties) {
		super(pProperties);
	}

	public static RegistryObject<Item> register() {
		Properties properties = new Properties();
		return ItemRegistry.replaceItem("stick", () -> new StickToothpick(properties));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
		ItemStack itemstack = pPlayer.getItemInHand(pUsedHand);
		if (pUsedHand == InteractionHand.MAIN_HAND) { // Currently only right-hand toothpicking is available
			pPlayer.startUsingItem(pUsedHand);
		}
		return InteractionResultHolder.consume(itemstack);
	}

	@Override
	public UseAnim getUseAnimation(ItemStack pStack) { // To prevent Minecraft from applying default animations
		return UseAnim.NONE;
	}

	@Override
	public int getUseDuration(ItemStack pStack) { // Item will be used for 1 hour straight at most
		return 72000; // 72000 ticks = 1 hour (real time)
	}

	@Override
	public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
		if (pLivingEntity instanceof Player && !pLevel.isClientSide) {
			Player player = (Player) pLivingEntity;
			int duration = pStack.getUseDuration() - pRemainingUseDuration;
			if (duration > 0 && duration % 20 == 0) { // Every second player rolls for an item drop
				ObjectArrayList<ItemStack> treasures = this.rollForTreasure(pLevel, player);
				playAmbientToothpickingSound(player);
				if (treasures.size() != 0) {
					this.awardTreasureTo(player, treasures);
				}
			}
		}
		super.onUseTick(pLevel, pLivingEntity, pStack, pRemainingUseDuration);
	}

	private ObjectArrayList<ItemStack> rollForTreasure(Level level, Player player) {
		LootTable lootTable = level.getServer().getLootData()
				.getLootTable(new ResourceLocation(FogyMain.MODID, LOOT_TABLE_NAME));
		LootParams lootParams = new LootParams.Builder((ServerLevel) level)
				.withParameter(LootContextParams.ORIGIN, player.position().add(0, player.getEyeY() - player.getY(), 0))
				.withParameter(LootContextParams.THIS_ENTITY, player).create(LootContextParamSets.EMPTY);
		return lootTable.getRandomItems(lootParams);
	}

	private void awardTreasureTo(Player player, ObjectArrayList<ItemStack> treasures) {
		for (ItemStack treasureItemStack : treasures) {
			this.displayTreasureNotificationMessage(player, treasureItemStack);
			this.playTreasureNotificationSound(player);
			if (!player.addItem(treasureItemStack)) {
				player.drop(treasureItemStack, false);
			}
		}
	}

	private void displayTreasureNotificationMessage(Player player, ItemStack treasureItemStack) {
		MessageManager messageManager = new MessageManager(new ResourceLocation(FogyMain.MODID, MESSAGE_TABLE_NAME));
		Chatter.sendChatMessageTo(player,
				String.format(messageManager.getMessage(treasureItemStack.getDescriptionId())));
	}

	private void playTreasureNotificationSound(Player player) {
		SoundPlayer.soundAtPlayerLocation(player, SoundEvents.VILLAGER_CELEBRATE, 0.7f, 1f);
		SoundPlayer.soundAtPlayerLocation(player, SoundEvents.ANVIL_BREAK);
	}

	private void playAmbientToothpickingSound(Player player) {
		SoundPlayer.soundAtPlayerLocationVaried(player, SoundRegistry.TOOTHPICKING.get());
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {

			@Override
			public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm,
					ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
				if (player.isUsingItem()) {
					applyPickTransform(poseStack, player, arm, partialTick);
				}
				applyItemArmTransform(poseStack, arm, equipProcess);
				return true;
			}

			private void applyPickTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm,
					float partialTick) {
				float f = player.getTicksUsingItem();
				float f1 = Math.max(1f - f / 20, 0f);

				float oscillation = Mth.sin((f / 20.0F) * (float) Math.PI * 2) * 0.2F;

				if (f > 10) {
					float f2 = Mth.abs(Mth.cos(f / 4.0F * (float) Math.PI) * 0.1F);
					poseStack.translate(0.0F, f2, 0.0F);
				}

				float f3 = 1.0F - (float) Math.pow((double) f1, 20.0);
				int i = arm == HumanoidArm.RIGHT ? 1 : -1;

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

}
