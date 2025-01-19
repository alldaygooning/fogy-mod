package fogy.util;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class Chatter {
	public static void sendChatMessageTo(Player receiver, String messageText) {
		receiver.createCommandSourceStack().sendSystemMessage(Component.literal(messageText));
	}
}
