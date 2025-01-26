package fogy.mixin;

import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.logging.LogUtils;

import net.minecraft.world.level.levelgen.feature.treedecorators.CocoaDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;

@Mixin({ CocoaDecorator.class })
public abstract class CocoaDecoratorMixin<R> {

	private static final Logger logger = LogUtils.getLogger();

	@Inject(method = "Lnet/minecraft/world/level/levelgen/feature/treedecorators/CocoaDecorator;place(Lnet/minecraft/world/level/levelgen/feature/treedecorators/TreeDecorator$Context;)V", at = @At("HEAD"), cancellable = true)
	public void injectPlaceMethod(TreeDecorator.Context pContext, CallbackInfo callbackInfo) {
		if(pContext.logs().isEmpty()) {
			logger.info("Prevented CocoaDecorator crash.");
			callbackInfo.cancel();
		}
	}
}
