package music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import core.DiscordMessage;
import core.Yasu;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.core.entities.Guild;

public class TrackScheduler extends AudioEventAdapter {
	private final AudioPlayer player;
	private final Guild guild;
	private final BlockingQueue<AudioTrack> queue;
	private boolean repeat_track;
	private boolean repeat_queue;
	private AudioTrack repeatTrack;
	private BlockingQueue<AudioTrack> repeatQueue;

	public TrackScheduler(AudioPlayer player, Guild guild) {
		this.player = player;
		this.queue = new LinkedBlockingQueue<AudioTrack>();
		this.guild = guild;
		this.repeat_track = false;
		this.repeat_queue = false;
		this.repeatTrack = null;
	}

	public void queue(AudioTrack track) {
		if ((!this.player.startTrack(track, true)) && (!this.queue.offer(track))) {
			this.guild.getDefaultChannel()
					.sendMessage(DiscordMessage.embedMusicMessage(Yasu.bot.getInstance().getSelfUser(),
							"Queue Internal Error",
							":warning: The track '" + track.getInfo().title + "(" + track.getIdentifier()
									+ ") could not be queued into this guild's track scheduler."))
					.complete();
		}
	}

	public void nextTrack() {
		if (this.player.isPaused()) {
			resume();
		}
		if (this.repeat_track) {
			this.player.startTrack(this.repeatTrack.makeClone(), false);
		} else {
			AudioTrack nextTrack = (AudioTrack) this.queue.peek();
			if (nextTrack == null) {
				if (this.repeat_queue) {
					setupQueueForRepeat();
					this.guild.getDefaultChannel()
							.sendMessage(DiscordMessage.embedMusicMessage(Yasu.bot.getInstance().getSelfUser(),
									"Repeating Queue", ":arrows_counterclockwise: Setting up player for queue repeat!"))

							.complete();
				} else {
					this.guild.getDefaultChannel()
							.sendMessage(DiscordMessage.embedMusicMessage(Yasu.bot.getInstance().getSelfUser(),
									"End of Queue",
									":end: Queue has ended!\nThere are no more tracks in this guild player's queue."))
							.complete();
				}
			}
			this.player.startTrack((AudioTrack) this.queue.poll(), false);
		}
	}

	private void setupQueueForRepeat() {
		try {
			this.queue.put(this.player.getPlayingTrack().makeClone());
			for (AudioTrack v : this.repeatQueue) {
				this.queue.put(v.makeClone());
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void clearQueue() {
		this.queue.clear();
	}

	public void setVolume(int volume) {
		this.player.setVolume(volume);
	}

	public void stop() {
		this.player.setPaused(true);
	}

	public boolean isPlaying() {
		return this.player.getPlayingTrack() != null;
	}

	public boolean isPaused() {
		return this.player.isPaused();
	}

	public boolean hasNextTrack() {
		return !this.queue.isEmpty();
	}

	public boolean isOnTrackRepeat() {
		return this.repeat_track;
	}

	public boolean isOnQueueRepeat() {
		return this.repeat_queue;
	}

	public BlockingQueue<AudioTrack> getQueue() {
		return this.queue;
	}

	public void resume() {
		this.player.setPaused(false);
	}

	public AudioTrack getPlaying() {
		return this.player.getPlayingTrack();
	}

	public boolean toggleTrackRepeat() {
		this.repeat_track = (!this.repeat_track);
		if (this.repeat_track) {
			this.repeatTrack = this.player.getPlayingTrack();
		}
		return this.repeat_track;
	}

	public boolean toggleQueueRepeat() {
		this.repeat_queue = (!this.repeat_queue);
		if (this.repeat_queue) {
			this.repeatQueue = getQueue();
		}
		return this.repeat_queue;
	}

	public AudioTrack getRepeatedTrack() {
		return this.repeatTrack;
	}

	public void shuffleTrack() {
		Collection<AudioTrack> shuffled_queue = new ArrayList<AudioTrack>();
		this.queue.drainTo(shuffled_queue);
		this.queue.clear();
		long seed = System.nanoTime();
		Collections.shuffle((List<AudioTrack>) shuffled_queue, new Random(seed));
		this.queue.addAll(shuffled_queue);
	}

	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		if (endReason.mayStartNext) {
			nextTrack();
		}
	}

	public void onTrackStart(AudioPlayer player, AudioTrack track) {
		long millis = track.getInfo().length;
		String length = String.format("%02d:%02d:%02d",
				new Object[] { Long.valueOf(TimeUnit.MILLISECONDS.toHours(millis)),
						Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1L)),
						Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1L)) });
		this.guild.getDefaultChannel().sendMessage(DiscordMessage.embedMusicMessage(
				Yasu.bot.getInstance().getSelfUser(), "Now Playing - ID: " + track.getIdentifier(),
				":notes: *" + track.getInfo().title + "*\n **Author:** " + track.getInfo().author + "\n **Length:** "
						+ length + "\n **URI:** " + track.getInfo().uri + "\n **Track repeat:** " + this.repeat_track
						+ "\n **Queue repeat:** " + this.repeat_queue))

				.complete();
	}

	public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
		this.guild.getDefaultChannel()
				.sendMessage(DiscordMessage.embedMusicMessage(Yasu.bot.getInstance().getSelfUser(),
						"Now Playing - Track got stuck", ":warning: The track '" + track.getInfo().title + "("
								+ track.getIdentifier() + ") got *stuck*. Just here to say that. Maybe reset player?"))
				.complete();
	}

	public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
		this.guild.getDefaultChannel()
				.sendMessage(DiscordMessage
						.embedMusicMessage(Yasu.bot.getInstance().getSelfUser(), "Now Playing - Exception",
								":warning: The track '" + track.getInfo().title + "(" + track.getIdentifier()
										+ ") just *killed* the player. Just here to say that. Maybe reset player?"))
				.complete();
	}
}
