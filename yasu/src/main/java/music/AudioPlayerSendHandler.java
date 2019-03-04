package music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import net.dv8tion.jda.core.audio.AudioSendHandler;

public class AudioPlayerSendHandler
  implements AudioSendHandler
{
  private final AudioPlayer audioPlayer;
  private AudioFrame lastFrame;
  
  public AudioPlayerSendHandler(AudioPlayer audioPlayer)
  {
    this.audioPlayer = audioPlayer;
  }
  
  public boolean canProvide()
  {
    if (this.lastFrame == null) {
      this.lastFrame = this.audioPlayer.provide();
    }
    return this.lastFrame != null;
  }
  
  public byte[] provide20MsAudio()
  {
    if (this.lastFrame == null) {
      this.lastFrame = this.audioPlayer.provide();
    }
    byte[] data = this.lastFrame != null ? this.lastFrame.getData() : null;
    this.lastFrame = null;
    
    return data;
  }
  
  public boolean isOpus()
  {
    return true;
  }
}
