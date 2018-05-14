package commands.music;

import commands.Command;
import commands.CommandCategory;
import commands.CommandScope;
import core.DiscordMessage;
import core.LongMessage;
import core.Yasu;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import music.MusicHandler;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Music implements Command {
	private static final HashMap<String, String> subcommands = new HashMap<String, String>();
	private static final HashMap<String, String> shortcuts;

	static {
		subcommands.put("play", "Plays the specified file/url/shortcut.");
		subcommands.put("queue", "Gets the current music player queue.");
		subcommands.put("leave", "Disconnects bot from voice channel.");
		subcommands.put("purge", "Resets the music queue.");
		subcommands.put("destroy", "Destroys the music player - might throw interrupt.");
		subcommands.put("volume", "Sets the volume(0-100) using the next parameter.");
		subcommands.put("pause", "Pauses the current song.");
		subcommands.put("resume", "Resumes the current song.");
		subcommands.put("skip", "Skips to the next song in queue, if any.");
		subcommands.put("playing", "Displays data of the current song playing.");
		subcommands.put("shuffle", "Toggles shuffle.");
		subcommands.put("trackrepeat", "Toggles repeat of track.");
		subcommands.put("queuerepeat", "Toggles repeat of queue");
		subcommands.put("types", "Displays the file/url types permited by the player.");
		subcommands.put("shortcuts", "Displays all current Universal shortcuts");
		subcommands.put("shortadd", "<shortcut> <url> adds a shortcut refering to the url");
		subcommands.put("shortremove", "<shortcut> <url> removes a shortcut refering to the url");

		shortcuts = new HashMap<String, String>();
		shortcuts.put("gamutape", "https://www.youtube.com/watch?v=x3O9rASQCuo");
		shortcuts.put("gasgasgas", "https://www.youtube.com/watch?v=atuFSv2bLa8");
		shortcuts.put("dejavu", "https://www.youtube.com/watch?v=dv13gl0a-FA");
		shortcuts.put("chanuto", "https://www.youtube.com/playlist?list=PLnVyz6ZocZudKIZ7S_49sISwqbU9VaXMV");
		shortcuts.put("chanimu", "https://www.youtube.com/playlist?list=PLnVyz6ZocZucT007Y6iWY9WUWAQOitW_M");
		shortcuts.put("DtB", "https://www.youtube.com/playlist?list=PLnVyz6ZocZueM3fMPBFkEdIxrQXMAsexH");
		shortcuts.put("omae", "https://www.youtube.com/watch?v=sU0K4q3YRfw");
		shortcuts.put("spaceboi", "https://www.youtube.com/watch?v=XswuFtW1bco");
	}

	public boolean called(String raw, String[] args, MessageReceivedEvent event) {
		if (args.length < 1) {
			event.getTextChannel()
					.sendMessage(
							DiscordMessage.embedProblem(event.getAuthor(), "Invalid **arguments**! Try !help music"))
					.queue();

			return false;
		}
		return true;
	}

	public void action(String raw, String[] args, MessageReceivedEvent event) {
		TextChannel channel = event.getTextChannel();
		MusicHandler mh = Yasu.bot.getMusicHandler();
		if ("play".contains(args[0])) {
			if (args.length < 2) {
				event.getTextChannel().sendMessage(
						DiscordMessage.embedProblem(event.getAuthor(), "Invalid **arguments**! Try !help music"))
						.queue();
			}
			if (shortcuts.containsKey(args[1])) {
				args[1] = shortcuts.get(args[1]);
			}
			mh.loadAndPlay(event.getAuthor(), channel, args[1]);
		} else if ("queue".contains(args[0])) {
			mh.getQueue(channel);
		} else if ("skip".contains(args[0])) {
			mh.skipTrack(channel);
		} else if ("leave".contains(args[0])) {
			mh.leave(channel);
		} else if ("purge".contains(args[0])) {
			mh.clearQueue(channel);
		} else if ("destroy".contains(args[0])) {
			mh.destroy(channel);
		} else if ("volume".contains(args[0])) {
			int volume = 100;
			try {
				volume = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				event.getChannel()
						.sendMessage(
								DiscordMessage.embedProblem(event.getAuthor(), "The volume number is not a value!"))
						.queue();
				return;
			}
			mh.setVolume(channel, volume);
		} else if ("pause".contains(args[0])) {
			mh.pause(channel);
		} else if ("resume".contains(args[0])) {
			mh.resume(channel);
		} else if ("playing".contains(args[0])) {
			mh.nowPlaying(channel);
		} else if ("shuffle".contains(args[0])) {
			mh.shuffle(channel);
		} else if ("trackrepeat".contains(args[0])) {
			mh.toggleTrackRepeat(channel);
		} else if ("queuerepeat".contains(args[0])) {
			mh.toggleQueueRepeat(channel);
		} else if ("types".contains(args[0])) {
			event.getChannel().sendMessage(DiscordMessage.embedMusicMessage(event.getAuthor(), "Supported Types",
					"**File Types:**\nMP3\r\nFLAC\r\nWAV\r\nMatroska/WebM (AAC, Opus or Vorbis codecs)\r\nMP4/M4A (AAC codec)\r\nOGG streams (Opus, Vorbis and FLAC codecs)\r\nAAC streams\r\nStream playlists (M3U and PLS)\n\n**Websites/Applications Types:**\nYouTube\r\nSoundCloud\r\nBandcamp\r\nVimeo\r\nTwitch streams\r\nLocal files\r\nHTTP URLs"))
					.queue();
		} else if ("shortcuts".contains(args[0])) {
			StringBuilder ret = new StringBuilder("");
			Iterator<Map.Entry<String, String>> it = shortcuts.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> pair = it.next();
				ret.append("[" + pair.getKey() + ":" + pair.getValue() + "]\n");
			}
			ret.append("```xl\n" + ret + "```");
			MessageEmbed embed = DiscordMessage.embedMusicMessage(event.getAuthor(), "Shortcut list:", ret.toString());

			LongMessage.storeBigMessage((Message) event.getChannel().sendMessage(embed).complete(), ret.toString());
		} else if ("shortadd".contains(args[0])) {
			if (args.length < 3) {
				event.getChannel().sendMessage(
						DiscordMessage.embedProblem(event.getAuthor(), "Invalid #arguments for this subcommand!"))
						.queue();
				return;
			}
			String key = args[1];
			String url = args[2];

			shortcuts.put(key, url);
			event.getChannel().sendMessage(DiscordMessage.embedMusicMessage(event.getAuthor(), "New Shortcut:",
					"Added **" + key + "** linked to *" + url + "*")).queue();
		} else if ("shortremove".contains(args[0])) {
			if (args.length < 2) {
				event.getChannel().sendMessage(
						DiscordMessage.embedProblem(event.getAuthor(), "Invalid #arguments for this subcommand!"))
						.queue();
				return;
			}
			String key = args[1];
			if (!shortcuts.containsKey(key)) {
				event.getChannel().sendMessage(DiscordMessage.embedProblem(event.getAuthor(),
						"Could not find that shortcut! Check !music shortcuts.")).queue();
				return;
			}
			shortcuts.remove(key);
			event.getChannel().sendMessage(DiscordMessage.embedMusicMessage(event.getAuthor(), "Removed Shortcut:",
					"Removed **" + key + "** out of shortcuts!")).queue();
		} else {
			event.getTextChannel().sendMessage(DiscordMessage.embedProblem(event.getAuthor(),
					"Invalid **sub command**! Please check the list using !help music")).queue();
		}
	}

	public String getDescription() {
		return "All the music player commands start with this one.";
	}

	public CommandCategory getCategory() {
		return CommandCategory.MUSIC;
	}

	public CommandScope getScope() {
		return CommandScope.PUBLIC;
	}

	public boolean isListed() {
		return true;
	}

	public HashMap<String, String> getSubCommands() {
		return subcommands;
	}

	public void executed(boolean succ, MessageReceivedEvent event) {
	}
}
