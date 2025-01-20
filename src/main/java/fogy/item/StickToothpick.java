package fogy.item;

import fogy.main.FogyMain;
import fogy.registry.ItemRegistry;
import fogy.util.Chatter;
import fogy.util.message.MessageManager;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public class StickToothpick extends Item {
	private static final Item PARENT_ITEM = Items.STICK;
	private static final String LOOT_TABLE_NAME = "toothpicking";
	private static final String MESSAGE_TABLE_NAME = "toothpicking";

	public StickToothpick(Properties pProperties) {
		super(pProperties);
	}

	public static void register() {
		Properties properties = new Properties();
		properties.stacksTo(PARENT_ITEM.getMaxStackSize());
		ItemRegistry.replaceItem("stick", () -> new StickToothpick(properties));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
		ItemStack itemstack = pPlayer.getItemInHand(pUsedHand);
		if (pUsedHand == InteractionHand.MAIN_HAND) { // Currently only right-hand toothpicking is available
			pPlayer.startUsingItem(pUsedHand);
//			Chatter.sendChatMessageTo(pPlayer,
//					String.format("%s: You have started using a toothpick!", pLevel.isClientSide ? "Client" : "Server"));
		}
		return InteractionResultHolder.consume(itemstack);
	}

	@Override
	public UseAnim getUseAnimation(ItemStack pStack) { // To prevent Minecraft from applying default animations
		return UseAnim.CUSTOM;
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
			this.sendTreasureNotification(player, treasureItemStack);
			if (!player.addItem(treasureItemStack)) {
				player.drop(treasureItemStack, false);
			}
		}
	}

	private void sendTreasureNotification(Player player, ItemStack treasureItemStack) {
		MessageManager messageManager = new MessageManager(new ResourceLocation(FogyMain.MODID, MESSAGE_TABLE_NAME));
		Chatter.sendChatMessageTo(player,
				String.format(messageManager.getMessage(treasureItemStack.getDescriptionId())));
	}
}
