package fogy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;

@Mixin({ NoiseBasedChunkGenerator.class })
public class NoiseBasedChunkGeneratorMixin {

	@Overwrite
	private static Aquifer.FluidPicker createFluidPicker(NoiseGeneratorSettings pSettings) {
		Aquifer.FluidStatus aquifer$fluidstatus = new Aquifer.FluidStatus(-100, Blocks.LAVA.defaultBlockState());
		int i = pSettings.seaLevel();
		Aquifer.FluidStatus aquifer$fluidstatus1 = new Aquifer.FluidStatus(i, pSettings.defaultFluid());
		Aquifer.FluidStatus aquifer$fluidstatus2 = new Aquifer.FluidStatus(DimensionType.MIN_Y * 2,
				Blocks.AIR.defaultBlockState());
		return (p_224274_, p_224275_, p_224276_) -> {
			return p_224275_ < Math.min(-100, i) ? aquifer$fluidstatus : aquifer$fluidstatus1;
		};
	}

}
