package fogy.item.extension;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class StickToothpickExtension implements IClientItemExtensions {

	@Override
	public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand,
			float partialTick, float equipProcess, float swingProcess) {

		return true;
	}

	private void applyRightHandItemTransformation(PoseStack poseStack, HumanoidArm arm, float equipProcess) {
	}


	private void applyRightHandItemAttackTransform(PoseStack pPoseStack, HumanoidArm pHand, float pSwingProgress) {

	}
}
