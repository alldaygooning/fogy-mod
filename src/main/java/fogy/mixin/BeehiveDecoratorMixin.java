package fogy.mixin;

import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.logging.LogUtils;

import net.minecraft.world.level.levelgen.feature.treedecorators.BeehiveDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;

@Mixin({ BeehiveDecorator.class })
public abstract class BeehiveDecoratorMixin {

	private static final Logger logger = LogUtils.getLogger();

	@Inject(method = "Lnet/minecraft/world/level/levelgen/feature/treedecorators/BeehiveDecorator;place(Lnet/minecraft/world/level/levelgen/feature/treedecorators/TreeDecorator$Context;)V", at = @At("HEAD"), cancellable = true)
	public void injectPlaceMethod(TreeDecorator.Context pContext, CallbackInfo callbackInfo) {
		if (pContext.logs().isEmpty()) {
			logger.info("Prevented BeehiveDecorator crash");
			callbackInfo.cancel();
		}
	}
}
