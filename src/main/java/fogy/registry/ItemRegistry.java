package fogy.registry;

import java.util.function.Supplier;

import fogy.main.FogyMain;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemRegistry {
	private static final String VANILLA_NAMESPACE = "minecraft";

	private static IEventBus modEventBus;

	private static final DeferredRegister<Item> FOGY_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
			FogyMain.MODID);
	private static final DeferredRegister<Item> VANILLA_REPLACEMENT_ITEMS = DeferredRegister
			.create(ForgeRegistries.ITEMS, VANILLA_NAMESPACE);

	public static void setModEventBus(IEventBus modEventBus) {
		ItemRegistry.modEventBus = modEventBus;

		FOGY_ITEMS.register(ItemRegistry.modEventBus);
		VANILLA_REPLACEMENT_ITEMS.register(ItemRegistry.modEventBus);
	}

	public static void registerItem(String itemName, Supplier<? extends Item> itemSupplier) {
		FOGY_ITEMS.register(itemName, itemSupplier);
	}

	public static void replaceItem(String itemName, Supplier<? extends Item> itemSupplier) {
		VANILLA_REPLACEMENT_ITEMS.register(itemName, itemSupplier);
	}
}
