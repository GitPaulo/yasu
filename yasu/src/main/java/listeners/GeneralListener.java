package listeners;

import core.Logger;
import core.Yasu;
import net.dv8tion.jda.core.events.DisconnectEvent;
import net.dv8tion.jda.core.events.ExceptionEvent;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.ReconnectedEvent;
import net.dv8tion.jda.core.events.ResumedEvent;
import net.dv8tion.jda.core.events.ShutdownEvent;
import net.dv8tion.jda.core.events.StatusChangeEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class GeneralListener extends ListenerAdapter {
	public void onReady(ReadyEvent event) {
		Logger.info(new Object[] { "JDA is ready!" });
	}

	public void onResume(ResumedEvent event) {
		Logger.info(new Object[] { "JDA has resumed!" });
	}

	public void onReconnect(ReconnectedEvent event) {
		Logger.info(new Object[] { "JDA has reconnected! Response code:" + event.getResponseNumber() });
	}

	public void onDisconnect(DisconnectEvent event) {
		Yasu.bot.saveData();

		StringBuilder data = new StringBuilder("");
		for (String v : event.getCloudflareRays()) {
			data.append(v + " ");
		}
		Logger.info(new Object[] { "JDA has disconnected. Closed by server: " + event.isClosedByServer()
				+ " Close code: " + event.getCloseCode() + "\n Cloudfare data: " + data.toString() });
	}

	public void onShutdown(ShutdownEvent event) {
		Yasu.bot.saveData();
		Logger.info(new Object[] { "JDA has shutdown! Close code: " + event.getCode() });
	}

	public void onStatusChange(StatusChangeEvent event) {
		Logger.info(new Object[] { "JDA status changed - Old: " + event.getOldStatus().toString() + " New: "
				+ event.getNewStatus().toString() });
	}

	public void onException(ExceptionEvent event) {
		Logger.critical(new Object[] { event.getCause() });
	}
}
