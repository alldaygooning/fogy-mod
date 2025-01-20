package fogy.registry;

import fogy.main.FogyMain;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SoundRegistry {
	private static IEventBus modEventBus;

	public static final DeferredRegister<SoundEvent> FOGY_SOUND_EVENTS = DeferredRegister
			.create(ForgeRegistries.SOUND_EVENTS, FogyMain.MODID);

	public static final RegistryObject<SoundEvent> TOOTHPICKING = SoundRegistry.register("toothpicking");

	public static void setModEventBus(IEventBus modEventBus) {
		SoundRegistry.modEventBus = modEventBus;

		FOGY_SOUND_EVENTS.register(SoundRegistry.modEventBus);
	}

	private static RegistryObject<SoundEvent> register(String name) {
		return FOGY_SOUND_EVENTS.register(name,
				() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FogyMain.MODID, name)));
	}
}
