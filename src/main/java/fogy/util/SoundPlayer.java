package fogy.util;

import java.util.Random;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class SoundPlayer {
	private static final Random RANDOM = new Random();

	public static void soundAtPlayerLocation(Player player, SoundEvent soundEvent) {
		soundAtPlayerLocation(player, soundEvent, 1f, 1f);
	}

	public static void soundAtPlayerLocationVaried(Player player, SoundEvent soundEvent) {
		float volume = getRandomVolume(0.85f, 1.15f);
		float pitch = getRandomPitch(0.85f, 1.15f);
		soundAtPlayerLocation(player, soundEvent, volume, pitch);
	}

	public static void soundAtPlayerLocation(Player player, SoundEvent soundEvent, float volume, float pitch) {
		Level level = player.level();
		level.playSound(null, player.getX(), player.getY(), player.getZ(), soundEvent, player.getSoundSource(), volume,
				pitch);
	}

	private static float getRandomVolume(float min, float max) {
		return min + RANDOM.nextFloat() * (max - min);
	}

	private static float getRandomPitch(float min, float max) {
		return min + RANDOM.nextFloat() * (max - min);
	}
}
