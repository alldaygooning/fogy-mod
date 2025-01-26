package fogy.main;

import fogy.item.StickToothpick;
import fogy.registry.ItemRegistry;
import fogy.registry.SoundRegistry;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;

@Mod(FogyMain.MODID)
public class FogyMain {
	public static final String MODID = "fogy";

	public static RegistryObject<Item> TOOTHPICK = StickToothpick.register();

	public FogyMain() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		MinecraftForge.EVENT_BUS.register(this);


		ItemRegistry.setModEventBus(modEventBus);
		SoundRegistry.setModEventBus(modEventBus);
	}
}


//	private void commonSetup(final FMLCommonSetupEvent event) {
//
//	}
//
//	private void addCreative(BuildCreativeModeTabContentsEvent event) {
//	}
//
//	@SubscribeEvent
//	public void onServerStarting(ServerStartingEvent event) {
//	}
//
//	@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
//	public static class ClientModEvents {
//		@SubscribeEvent
//		public static void onClientSetup(FMLClientSetupEvent event) {
//
//		}
//	}
