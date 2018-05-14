package core;

import java.awt.Color;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;

public final class DiscordMessage {
	public static Message toSimple(String text) {
		MessageBuilder mb = new MessageBuilder();
		mb.append(checkLength(text));
		return mb.build();
	}

	public static Message toTTS(String text) {
		MessageBuilder mb = new MessageBuilder();
		mb.setTTS(true);
		mb.append(checkLength(text));
		return mb.build();
	}

	public static MessageEmbed embedProblem(User u, String text) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setAuthor(u.getName(), u.getAvatarUrl(), u.getAvatarUrl());
		eb.setColor(Color.red);
		eb.setTitle("OOPS, problem has occured!");
		eb.setDescription(text);
		return eb.build();
	}

	public static MessageEmbed embedNotification(User u, String text) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setAuthor(u.getName(), u.getAvatarUrl(), u.getAvatarUrl());
		eb.setColor(Color.cyan);
		eb.setDescription(checkEmbedLength(text));
		eb.setTitle("Notification!");
		return eb.build();
	}

	public static MessageEmbed embedCleverbot(User u, String title, String text) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setAuthor(u.getName(), u.getAvatarUrl(), u.getAvatarUrl());
		eb.setColor(Color.GREEN);
		eb.setDescription(checkEmbedLength(text));
		eb.setTitle("Cleverbot - " + title);
		eb.setThumbnail(
				"https://site-images.similarcdn.com/url?url=https%3A%2F%2Flh6.ggpht.com%2FQ7V-hL_ditqg8I5wGZjegJjZHxolThYQzYjF-Y0TEdirsYmbgJfHSpxjuKKHqyZk5Mv9%3Dw256&h=911030551373405293");

		return eb.build();
	}

	public static MessageEmbed embedMusicMessage(User u, String title, String text) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setAuthor(u.getName(), u.getAvatarUrl(), u.getAvatarUrl());
		eb.setColor(Color.ORANGE);
		eb.setDescription(checkEmbedLength(text));
		eb.setTitle("MusicPlayer - " + title);
		eb.setThumbnail("http://orig12.deviantart.net/748e/f/2013/102/a/2/anime_render_3_by_fearkubrick-d61i3bb.png");
		return eb.build();
	}

	public static MessageEmbed embedBlackListMessage(User u, String title, String text) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setAuthor(u.getName(), u.getAvatarUrl(), u.getAvatarUrl());
		eb.setColor(Color.BLACK);
		eb.setDescription(checkEmbedLength(text));
		eb.setTitle("BlackList - " + title);
		eb.setThumbnail("http://a.dryicons.com/images/icon_sets/aesthetica/png/128x128/database_remove.png");
		return eb.build();
	}

	public static MessageEmbed embedEventMessage(User u, String title, String text) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setAuthor(u.getName(), u.getAvatarUrl(), u.getAvatarUrl());
		eb.setColor(Color.YELLOW);
		eb.setDescription(checkEmbedLength(text));
		eb.setTitle("Event - " + title);
		eb.setThumbnail(
				"https://tickera-wpsalad.netdna-ssl.com/wp-content/themes/tickera/style/img/freebies/icons/events/6.png");

		return eb.build();
	}

	public static MessageEmbed embedMessage(User u, String title, String text, Color color, String imageurl) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setAuthor(u.getName(), u.getAvatarUrl(), u.getAvatarUrl());
		eb.setColor(color);
		eb.setDescription(checkEmbedLength(text));
		eb.setTitle(title);
		eb.setThumbnail(imageurl);
		return eb.build();
	}

	public static MessageEmbed embedSimple(User u, String title, String text) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setAuthor(u.getName(), u.getAvatarUrl(), u.getAvatarUrl());
		eb.setColor(Color.BLACK);
		eb.setDescription(checkEmbedLength(text));
		eb.setTitle(title);
		return eb.build();
	}

	private static String checkLength(String msg) {
		if (msg.length() <= 4000) {
			return msg;
		}
		return msg.substring(0, 3995) + "(...)";
	}

	private static String checkEmbedLength(String msg) {
		if (msg.length() <= 2048) {
			return msg;
		}
		return msg.substring(0, 2043) + "(...)";
	}
}
