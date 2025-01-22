package fogy.util.message;

import net.minecraft.resources.ResourceLocation;

//Probably should implement a system of storing resources in game cache
//and updating it via /reload in the game.
public class MessageManager {
	private final ResourceLocation resourceLocation;
	private MessageResource messageResource;

	public MessageManager(ResourceLocation resourceLocation) {
		this.resourceLocation = resourceLocation;
		loadMessageResource();
	}

	private void loadMessageResource() {
		this.messageResource = new MessageResource(resourceLocation);
	}

	public String getMessage(String key) {
		return messageResource.getMessage(key);
	}
}
