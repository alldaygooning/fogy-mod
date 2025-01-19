package fogy.main;

import fogy.item.StickToothpick;
import fogy.registry.ItemRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(FogyMain.MODID)
public class FogyMain {
	public static final String MODID = "fogy";

	public FogyMain() {
		this.registerItems();

		MinecraftForge.EVENT_BUS.register(this);
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();


		ItemRegistry.setModEventBus(modEventBus);
	}

	private void registerItems() {
		StickToothpick.register();
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
