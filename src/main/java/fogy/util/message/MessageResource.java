package fogy.util.message;

import java.io.InputStreamReader;
import java.util.Random;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;

public class MessageResource {
	private final JsonObject messageKeys;
	private final Random random;

	public MessageResource(ResourceLocation resourceLocation) {
		this.random = new Random();
		try (InputStreamReader reader = new InputStreamReader(MessageResource.class.getResourceAsStream(
				"/data/" + resourceLocation.getNamespace() + "/messages/" + resourceLocation.getPath() + ".json"))) {
			this.messageKeys = JsonParser.parseReader(reader).getAsJsonObject();
		} catch (Exception e) {
			throw new RuntimeException("Failed to load messages from " + resourceLocation, e);
		}
	}

	public String getMessage(String itemId) {
		if (messageKeys.has(itemId)) {
			JsonArray messageList = messageKeys.getAsJsonArray(itemId);
			return I18n.get(messageList.get(random.nextInt(messageList.size())).getAsString(), null);
		}
		return "No message found for item: " + itemId;
	}
}
