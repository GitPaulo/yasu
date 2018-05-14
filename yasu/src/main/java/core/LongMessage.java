package core;

import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;

public class LongMessage {
	private final Message message;
	private final ArrayList<String> messageParts;
	private final List<MessageEmbed> embed;
	private int page;

	public LongMessage(Message message, ArrayList<String> messageParts, List<MessageEmbed> embed) {
		this.message = message;
		this.messageParts = messageParts;
		this.embed = embed;
		this.page = 0;
	}

	public Message getMessage() {
		return this.message;
	}

	public ArrayList<String> getMessageParts() {
		return this.messageParts;
	}

	public int getPage() {
		return this.page;
	}

	public String getCurrentContent() {
		return (String) this.messageParts.get(this.page);
	}

	public List<MessageEmbed> getEmbed() {
		return this.embed;
	}

	public boolean hasEmbeds() {
		return this.embed != null;
	}

	public void setPage(int page) {
		if ((page >= this.messageParts.size()) || (page < 0)) {
			return;
		}
		this.page = page;
	}

	public static boolean storageHasMessage(String id) {
		for (LongMessage v : Yasu.bot.getStorageBigMessages()) {
			if (v.getMessage().getId().equals(id)) {
				return true;
			}
		}
		return false;
	}

	public static void removeFromStorage(String id) {
		int i = 0;
		for (LongMessage v : Yasu.bot.getStorageBigMessages()) {
			if (v.getMessage().getId().equals(id)) {
				Yasu.bot.getStorageBigMessages().remove(i);
				break;
			}
			i++;
		}
	}

	public static void storeBigMessage(Message msg, String content) {
		Logger.debug(new Object[] { "LONG MESSAGE RAN" });

		int DIVISOR = 4000;
		List<MessageEmbed> embed = null;
		if (!msg.getEmbeds().isEmpty()) {
			DIVISOR = 2048;
			embed = msg.getEmbeds();
		}
		if ((content.length() < DIVISOR) || (content.length() > 20000)) {
			Logger.critical(new Object[] { "[LONG MESSAGE] Message was too big to store size: " + content.length() });
			return;
		}
		ArrayList<String> message = new ArrayList<String>();
		while (content.length() > 0) {
			if (content.length() < DIVISOR) {
				message.add(content);
				break;
			}
			String nextChunk = content.substring(0, DIVISOR);

			message.add(nextChunk);

			content = content.substring(DIVISOR, content.length());
		}
		if (Yasu.bot.getStorageBigMessages().size() > 2) {
			Logger.critical(new Object[] {
					"Storage Messages over 100! Clearning data! This means reactions wont work on older messages." });

			Yasu.bot.getStorageBigMessages().clear();
		}
		Yasu.bot.getStorageBigMessages().add(new LongMessage(msg, message, embed));

		msg.addReaction("?").queue();
		msg.addReaction("?").queue();
		msg.addReaction("?").queue();

		Logger.debug(new Object[] { "LONG REACTIONS ADDED!" });
	}
}
