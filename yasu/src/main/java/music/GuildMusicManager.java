package music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import core.DiscordMessage;
import core.Yasu;
import java.util.Timer;
import java.util.TimerTask;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;

public class GuildMusicManager {
	private static final int ACTIVITYDELAY = 30000;
	private static final int LEAVEDELAY = 10000;
	public final Guild guild;
	public final AudioPlayer player;
	public final TrackScheduler scheduler;
	public final Timer checkActivity;

	public GuildMusicManager(AudioPlayerManager manager, final Guild guild) {
		this.guild = guild;
		this.player = manager.createPlayer();
		this.scheduler = new TrackScheduler(this.player, guild);
		this.checkActivity = new Timer();
		this.checkActivity.schedule(new TimerTask() {
			private boolean isChecking = false;

			public void run() {
				if ((!GuildMusicManager.this.scheduler.isPlaying()) && (!GuildMusicManager.this.player.isPaused())
						&& (guild.getAudioManager().isConnected()) && (!this.isChecking)) {
					this.isChecking = true;

					final Message msg = guild.getDefaultChannel()
							.sendMessage(DiscordMessage.embedMusicMessage(Yasu.bot.getInstance().getSelfUser(),
									"Auto Leave",
									"Seems like no one is using the bot! Will leave music channel in **10** seconds!"))
							.complete();

					new Timer().schedule(new TimerTask() {
						public void run() {
							if ((GuildMusicManager.this.scheduler.isPlaying())
									|| (GuildMusicManager.this.player.isPaused())) {
								msg.editMessage(DiscordMessage.embedMusicMessage(Yasu.bot.getInstance().getSelfUser(),
										"Auto Leave",
										"**Activity Detected** - Continuing with music player activity! Music player wont disconnect."))
										.complete();
							} else {
								msg.editMessage(DiscordMessage.embedMusicMessage(Yasu.bot.getInstance().getSelfUser(),
										"Auto Leave", "**Leaving** music channel and **clearing** music data!"))
										.complete();
								player.destroy();
								scheduler.clearQueue();
								guild.getAudioManager().closeAudioConnection();
							}
							isChecking = false;
						}
					}, LEAVEDELAY);
				}
			}
		}, ACTIVITYDELAY, ACTIVITYDELAY);

		this.player.addListener(this.scheduler);
	}

	public AudioPlayerSendHandler getSendHandler() {
		return new AudioPlayerSendHandler(this.player);
	}
}
