package music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import core.DiscordMessage;
import core.LongMessage;
import core.Yasu;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.managers.AudioManager;

public class MusicHandler {
	private final AudioPlayerManager playerManager;
	private final Map<Long, GuildMusicManager> musicManagers;

	public MusicHandler() {
		this.musicManagers = new HashMap<Long, GuildMusicManager>();

		this.playerManager = new DefaultAudioPlayerManager();
		AudioSourceManagers.registerRemoteSources(this.playerManager);
		AudioSourceManagers.registerLocalSource(this.playerManager);
	}

	private synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
		long guildId = Long.parseLong(guild.getId());
		GuildMusicManager musicManager = this.musicManagers.get(Long.valueOf(guildId));
		if (musicManager == null) {
			musicManager = new GuildMusicManager(this.playerManager, guild);
			this.musicManagers.put(Long.valueOf(guildId), musicManager);
		}
		guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());
		return musicManager;
	}

	public void loadAndPlay(User servicer, final TextChannel channel, final String trackUrl) {
		if ((!checkConnection(channel)) && (!tryConnectVoiceChannel(channel, servicer))) {
			return;
		}
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());

		final String msg_id = ( channel.sendMessage(DiscordMessage.embedMusicMessage(Yasu.bot.getInstance().getSelfUser(),
						"Processing Request!", "Please wait while I process your request...."))
				.complete()).getId();
		channel.sendTyping().queue(); 

		this.playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
			public void trackLoaded(AudioTrack track) {
				channel.editMessageById(msg_id, DiscordMessage.embedMusicMessage(Yasu.bot.getInstance().getSelfUser(),
						"Song Added!", "Adding to queue :notes:*" + track.getInfo().title + "*")).queue();

				MusicHandler.this.queue(channel, track);
			}

			public void playlistLoaded(AudioPlaylist playlist) {
				AudioTrack firstTrack = playlist.getSelectedTrack();
				if (firstTrack == null) {
					firstTrack = playlist.getTracks().get(0);
				}
				int i = 0;
				for (AudioTrack at : playlist.getTracks()) {
					i++;
					MusicHandler.this.queue(channel, at);
					if (i % (playlist.getTracks().size() / 5) == 0) {
						channel.editMessageById(msg_id, DiscordMessage.embedMusicMessage(
								Yasu.bot.getInstance().getSelfUser(), "Playlist Added!",
								"Processing playlist " + playlist.getName() + " (Added " + i + " songs to queue)"))
								.queue();
					}
				}
			}

			public void noMatches() {
				channel.sendMessage(DiscordMessage.embedProblem(Yasu.bot.getInstance().getSelfUser(),
						"Nothing found by \"" + trackUrl + "\" (url)")).queue();
			}

			public void loadFailed(FriendlyException exception) {
				channel.sendMessage(DiscordMessage.embedProblem(Yasu.bot.getInstance().getSelfUser(),
						"Could not play: " + exception.getMessage())).queue();
			}
		});
	}

	private void queue(TextChannel channel, AudioTrack track) {
		if (!checkConnection(channel)) {
			channel.sendMessage(DiscordMessage.embedProblem(Yasu.bot.getInstance().getSelfUser(),
					"Please make sure the bot is connected to a voice channel! (use !music play <x>)")).queue();
			return;
		}
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
		musicManager.scheduler.queue(track);
	}

	public void getQueue(TextChannel channel) {
		if (!allowInteraction(channel)) {
			return;
		}
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
		BlockingQueue<AudioTrack> queue = musicManager.scheduler.getQueue();
		if (queue.isEmpty()) {
			channel.sendMessage(
					DiscordMessage.embedMusicMessage(Yasu.bot.getInstance().getSelfUser(), "Song Skip - Empty Queue",
							"The current queue is empty!\n**!music play <song>** to play&queue a song!"))
					.queue();
			return;
		}
		StringJoiner joiner = new StringJoiner("\n");
		int count = 0;
		for (AudioTrack t : queue) {
			long millis = t.getInfo().length;
			String length = String.format("%02d:%02d:%02d",
					new Object[] { Long.valueOf(TimeUnit.MILLISECONDS.toHours(millis)),
							Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1L)),
							Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1L)) });

			joiner.add("**" + count + " - NEW TRACK** - ID:" + t.getInfo().identifier);
			joiner.add("? Title_____ " + t.getInfo().title);
			joiner.add("? Author_____ " + t.getInfo().author);
			joiner.add("? Length_____ " + length);
			joiner.add("? URI_____ " + t.getInfo().uri);
			count++;
		}
		MessageEmbed embed = DiscordMessage.embedMusicMessage(Yasu.bot.getInstance().getSelfUser(),
				"Queue Display - **Size: " + count + "**", joiner.toString());

		LongMessage.storeBigMessage((Message) channel.sendMessage(embed).complete(), joiner.toString());
	}

	public void skipTrack(TextChannel channel) {
		if (!allowInteraction(channel)) {
			return;
		}
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
		if (musicManager.scheduler.hasNextTrack()) {
			AudioTrack nextrack = (musicManager.scheduler.getQueue().peek()).makeClone();
			channel.sendMessage(DiscordMessage.embedMusicMessage(Yasu.bot.getInstance().getSelfUser(), "Song Skip",
					" Skipped to next track: [" + nextrack.getInfo().title + "]")).queue();
		} else if (musicManager.scheduler.isOnQueueRepeat()) {
			channel.sendMessage(DiscordMessage.embedMusicMessage(Yasu.bot.getInstance().getSelfUser(),
					"Song Skip - Queue Repeat", "**Repeating** current queue!")).queue();
		} else if (musicManager.scheduler.isOnTrackRepeat()) {
			channel.sendMessage(DiscordMessage.embedMusicMessage(Yasu.bot.getInstance().getSelfUser(),
					"Song Skip - Track Repeat", "**Repeating** current song!")).queue();
		} else {
			channel.sendMessage(DiscordMessage.embedMusicMessage(Yasu.bot.getInstance().getSelfUser(),
					"Song Skip - End Track", "There is no next track!\n**Ending** song...")).queue();
		}
		musicManager.scheduler.nextTrack();
	}

	public void toggleTrackRepeat(TextChannel channel) {
		if (!allowInteraction(channel)) {
			return;
		}
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
		if (musicManager.scheduler.isOnQueueRepeat()) {
			channel.sendMessage(DiscordMessage.embedMusicMessage(Yasu.bot.getInstance().getSelfUser(),
					"Repeat Track - AutoDisable",
					" **Queue** repeating was enabled. Does not make sense to have it on track repeat.\nToggled it to **false**"))
					.queue();
			toggleQueueRepeat(channel);
		}
		channel.sendMessage(DiscordMessage.embedMusicMessage(Yasu.bot.getInstance().getSelfUser(), "Repeat Track",
				" Repeating current track set to: **" + musicManager.scheduler.toggleTrackRepeat() + "**")).queue();
	}

	public void toggleQueueRepeat(TextChannel channel) {
		if (!allowInteraction(channel)) {
			return;
		}
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
		if (musicManager.scheduler.isOnTrackRepeat()) {
			channel.sendMessage(DiscordMessage.embedMusicMessage(Yasu.bot.getInstance().getSelfUser(),
					"Repeat Track - AutoDisable",
					" **Song** repeating was enabled. Does not make sense to have it on queue repeat.\nToggled it to **false**"))
					.queue();
			toggleTrackRepeat(channel);
		}
		channel.sendMessage(DiscordMessage.embedMusicMessage(Yasu.bot.getInstance().getSelfUser(), "Repeat Queue",
				" Repeating current ACTIVE QUEUE set to: **" + musicManager.scheduler.toggleQueueRepeat() + "**"))
				.queue();
	}

	public void clearQueue(TextChannel channel) {
		if (!allowInteraction(channel)) {
			return;
		}
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
		musicManager.scheduler.clearQueue();
		channel.sendMessage(DiscordMessage.embedMusicMessage(Yasu.bot.getInstance().getSelfUser(), "Reset!",
				"Queue has been reset!")).queue();
	}

	public void setVolume(TextChannel channel, int volume) {
		if (!allowInteraction(channel)) {
			return;
		}
		if ((volume < 0) || (volume > 100)) {
			channel.sendMessage(DiscordMessage.embedProblem(Yasu.bot.getInstance().getSelfUser(),
					"Invalid volume value! 0 to 100 please!")).queue();
			return;
		}
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
		musicManager.scheduler.setVolume(volume);
		channel.sendMessage(DiscordMessage.embedMusicMessage(Yasu.bot.getInstance().getSelfUser(), "Volume Control",
				"Volume of player set to: **" + volume + "**")).queue();
	}

	public void pause(TextChannel channel) {
		if (!allowInteraction(channel)) {
			return;
		}
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
		if (!musicManager.scheduler.isPaused()) {
			AudioTrack track = musicManager.scheduler.getPlaying();
			musicManager.scheduler.stop();
			channel.sendMessage(DiscordMessage.embedMusicMessage(Yasu.bot.getInstance().getSelfUser(), "Paused!",
					"Song [" + track.getInfo().title + "] was paused!")).queue();
		} else {
			channel.sendMessage(
					DiscordMessage.embedProblem(Yasu.bot.getInstance().getSelfUser(), "Nothing is playing!")).queue();
		}
	}

	public void resume(TextChannel channel) {
		if (!allowInteraction(channel)) {
			return;
		}
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
		if (musicManager.scheduler.isPaused()) {
			musicManager.scheduler.resume();
			AudioTrack track = musicManager.scheduler.getPlaying();
			channel.sendMessage(DiscordMessage.embedMusicMessage(Yasu.bot.getInstance().getSelfUser(), "Resumed!",
					"Song [" + track.getInfo().title + "] was resumed!")).queue();
		} else {
			channel.sendMessage(DiscordMessage.embedProblem(Yasu.bot.getInstance().getSelfUser(), "Nothing is paused!"))
					.queue();
		}
	}

	public void leave(TextChannel channel) {
		if (!allowInteraction(channel)) {
			return;
		}
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
		musicManager.player.stopTrack();

		JDA nis = Yasu.bot.getInstance();
		channel.getGuild().getAudioManager().closeAudioConnection();
		channel.sendMessage(DiscordMessage.embedMusicMessage(nis.getSelfUser(), "Disconnected!",
				"Leaving Voice channel! Cya later!")).queue();
	}

	public void destroy(TextChannel channel) {
		if (!allowInteraction(channel)) {
			return;
		}
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
		musicManager.player.destroy();

		JDA nis = Yasu.bot.getInstance();
		channel.getGuild().getAudioManager().closeAudioConnection();
		channel.sendMessage(DiscordMessage.embedMusicMessage(nis.getSelfUser(), "Disconnected!",
				"Destroyed player! Leaving Voice channel! Cya later!")).queue();
	}

	public void nowPlaying(TextChannel channel) {
		if (!allowInteraction(channel)) {
			return;
		}
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
		JDA nis = Yasu.bot.getInstance();
		if (musicManager.scheduler.isPlaying()) {
			StringJoiner joiner = new StringJoiner("\n");
			AudioTrack t = musicManager.scheduler.getPlaying();

			long millis = t.getInfo().length;
			String length = String.format("%02d:%02d:%02d",
					new Object[] { Long.valueOf(TimeUnit.MILLISECONDS.toHours(millis)),
							Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1L)),
							Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1L)) });
			joiner.add("Now Playing - ID:" + t.getInfo().identifier);
			joiner.add("? Title_____ " + t.getInfo().title);
			joiner.add("? Author_____ " + t.getInfo().author);
			joiner.add("? Length_____ " + length);
			joiner.add("? URI_____ " + t.getInfo().uri);

			channel.sendMessage(
					DiscordMessage.embedMusicMessage(nis.getSelfUser(), "Now Playing info:", joiner.toString()))
					.queue();
		} else {
			channel.sendMessage(
					DiscordMessage.embedProblem(Yasu.bot.getInstance().getSelfUser(), "Currently nothing is playing!)"))
					.queue();
		}
	}

	public void shuffle(TextChannel channel) {
		if (!allowInteraction(channel)) {
			return;
		}
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
		musicManager.scheduler.shuffleTrack();
		channel.sendMessage(DiscordMessage.embedMusicMessage(Yasu.bot.getInstance().getSelfUser(), "Shuffle!",
				"Queue has been shuffled! Check !music queue")).queue();
	}

	private boolean checkConnection(TextChannel channel) {
		AudioManager audioManager = channel.getGuild().getAudioManager();
		return !audioManager.isConnected() && (!audioManager.isAttemptingToConnect());
	}

	private boolean allowInteraction(TextChannel channel) {
		if (checkConnection(channel)) {
			return true;
		}
		channel.sendMessage(DiscordMessage.embedProblem(Yasu.bot.getInstance().getSelfUser(),
				"Please make sure the bot is connected to a voice channel! (use !music play <x>)")).queue();
		return false;
	}

	private boolean tryConnectVoiceChannel(TextChannel channel, User servicer) {
		AudioManager audioManager = channel.getGuild().getAudioManager();
		if ((audioManager.isConnected()) || (audioManager.isAttemptingToConnect())) {
			return false;
		}
		List<VoiceChannel> channels = audioManager.getGuild().getVoiceChannels();
		VoiceChannel connected_channel = null;
		for (VoiceChannel voiceChannel : channels) {
			boolean found = false;
			for (Member m : voiceChannel.getMembers()) {
				if (m.getUser().equals(servicer)) {
					found = true;
					break;
				}
			}
			if (found) {
				audioManager.openAudioConnection(voiceChannel);
				connected_channel = voiceChannel;
				break;
			}
		}
		if (connected_channel == null) {
			channel.sendMessage(DiscordMessage.embedProblem(servicer,
					"You are **not** in a voice channel! \n The servicer of this command must join a voice channel!"))
					.queue();
			return false;
		}
		channel.sendMessage(DiscordMessage.embedMusicMessage(Yasu.bot.getInstance().getSelfUser(),
				"Joined Voice Channel!", "Joined channel **" + connected_channel.getName() + "** to play music!"))
				.queue();
		return true;
	}
}
