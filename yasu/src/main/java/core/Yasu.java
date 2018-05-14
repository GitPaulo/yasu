package core;

import commands.Command;
import commands.CommandParser;
import commands.administrative.GuildManage;
import commands.administrative.MemberManage;
import commands.administrative.Say;
import commands.developer.BlackList;
import commands.developer.BotManage;
import commands.developer.Kill;
import commands.developer.Logs;
import commands.developer.Restart;
import commands.developer.Save;
import commands.fun.Chat;
import commands.fun.Emote;
import commands.fun.Fact;
import commands.fun.Flip;
import commands.fun.Insult;
import commands.fun.Joke;
import commands.fun.Naruto;
import commands.fun.Neko;
import commands.fun.Roll;
import commands.fun.Yasusada;
import commands.informative.ChannelInfo;
import commands.informative.Creator;
import commands.informative.Help;
import commands.informative.Information;
import commands.informative.ServerInfo;
import commands.informative.UserInfo;
import commands.informative.Version;
import commands.music.Music;
import commands.utility.AnimeSearch;
import commands.utility.Define;
import commands.utility.Diagnostics;
import commands.utility.Github;
import commands.utility.Google;
import commands.utility.Invite;
import commands.utility.JavaRun;
import commands.utility.Math;
import commands.utility.Ping;
import commands.utility.Respects;
import commands.utility.TinyUrl;
import commands.utility.Vote;
import commands.utility.Weather;
import commands.utility.Youtube;
import gui.InterfaceGUI;
import gui.SetupGUI;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import javax.security.auth.login.LoginException;
import listeners.ClientListener;
import listeners.GeneralListener;
import listeners.GuildListener;
import listeners.MessageListener;
import music.MusicHandler;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

public class Yasu {
	private static final ConfigurationHandler CONFIG = new ConfigurationHandler();
	public static final String DEVID = "166176374036365312";
	public static final String MOTD = "This version is supposed to be the best most stable version yet. If it's not - cry me a river. \n\n Type !help for command documentation.";
	public static final StorageHandler DATA = new StorageHandler("yasu_data.json");
	public static Client bot;

	public static void main(String[] args) throws LoginException, IllegalArgumentException, RateLimitedException {
		if ((args.length > 0) && (args[0].equals("plunch"))) {
			boot();
			for (Guild g : bot.getInstance().getGuilds()) {
				g.getDefaultChannel().sendMessage(DiscordMessage.embedNotification(bot.getInstance().getSelfUser(),
						"Yasu has restarted successfully! [Programatic signal recieved]")).complete();
			}
			return;
		}
		Scanner in = new Scanner(System.in);
		String input = null;
		Logger.info(new Object[] { "Yasu application started! Setup & Boot process initialising...." });
		if (CONFIG.isNew()) {
			startSetupUI();
		} else {
			Logger.info(new Object[] { "Skip setup? (must have a valid properties file setup already) Y/N" });
			for (;;) {
				input = in.nextLine();
				if (input.equalsIgnoreCase("n")) {
					startSetupUI();
					break;
				}
				if (input.equalsIgnoreCase("y")) {
					boot();
					break;
				}
				Logger.info(new Object[] { "Invalid input! Try Again" });
			}
		}
		input = null;
		Logger.info(new Object[] { "Activate User Interface? Y/N (Doesnt work on linux atm - rip)" });
		for (;;) {
			input = in.nextLine();
			if (input.equalsIgnoreCase("y")) {
				startUI();
				break;
			}
			if (input.equalsIgnoreCase("n")) {
				break;
			}
			Logger.info(new Object[] { "Invalid input! Try again!" });
		}
		in.close();
	}

	public static void boot() throws LoginException, IllegalArgumentException, RateLimitedException {
		String[] prefixes = new String[2];
		HashMap<String, Command> commands = new HashMap<String, Command>();
		CommandParser commandparser = new CommandParser();
		MusicHandler musicHandler = new MusicHandler();
		HashMap<String, String> APIkeys = new HashMap<String, String>();
		ArrayList<LongMessage> storageBigMessages = new ArrayList<LongMessage>();

		ArrayList<User> blackList = new ArrayList<User>();

		String token = CONFIG.getValue("TOKEN");
		String game = CONFIG.getValue("GAME");
		String hostid = CONFIG.getValue("HOSTID");
		OnlineStatus status = OnlineStatus.fromKey(CONFIG.getValue("STATUS"));
		boolean autor = Boolean.parseBoolean(CONFIG.getValue("RECONNECT"));
		prefixes[0] = CONFIG.getValue("PREFIX1");
		prefixes[1] = CONFIG.getValue("PREFIX2");
		APIkeys.put("youtube_data", CONFIG.getValue("YOUTUBE_KEY"));
		APIkeys.put("giphy", CONFIG.getValue("GIPHY_KEY"));
		APIkeys.put("cleverbot", CONFIG.getValue("CLEVERBOT_KEY"));

		commands.put("ping", new Ping());
		commands.put("music", new Music());
		commands.put("flip", new Flip());
		commands.put("diagnostics", new Diagnostics());
		commands.put("information", new Information());
		commands.put("help", new Help());
		commands.put("java", new JavaRun());
		commands.put("math", new Math());
		commands.put("define", new Define());
		commands.put("weather", new Weather());
		commands.put("animesearch", new AnimeSearch());
		commands.put("roll", new Roll());
		commands.put("youtube", new Youtube());
		commands.put("creator", new Creator());
		commands.put("emote", new Emote());
		commands.put("kill", new Kill());
		commands.put("version", new Version());
		commands.put("google", new Google());
		commands.put("tinyurl", new TinyUrl());
		commands.put("blacklist", new BlackList());
		commands.put("userinfo", new UserInfo());
		commands.put("serverinfo", new ServerInfo());
		commands.put("channelinfo", new ChannelInfo());
		commands.put("logs", new Logs());
		commands.put("naruto", new Naruto());
		commands.put("chat", new Chat());
		commands.put("vote", new Vote());
		commands.put("insult", new Insult());
		commands.put("neko", new Neko());
		commands.put("joke", new Joke());
		commands.put("save", new Save());
		commands.put("f", new Respects());
		commands.put("fact", new Fact());
		commands.put("say", new Say());
		commands.put("botmanage", new BotManage());
		commands.put("guildmanage", new GuildManage());
		commands.put("membermanage", new MemberManage());
		commands.put("invite", new Invite());
		commands.put("github", new Github());
		commands.put("yasu", new Yasusada());
		commands.put("restart", new Restart());
		try {
			bot = new Client(
					new JDABuilder(AccountType.BOT).setToken(token).setStatus(status)
							.setGame(Game.of(Game.GameType.WATCHING, game)).setAutoReconnect(autor)
							.setBulkDeleteSplittingEnabled(false).setAudioEnabled(true)
							.addEventListener(new Object[] { new MessageListener() })
							.addEventListener(new Object[] { new GeneralListener() })
							.addEventListener(new Object[] { new ClientListener() })
							.addEventListener(new Object[] { new GuildListener() }).buildBlocking(),
					commands, APIkeys, prefixes, commandparser, hostid, musicHandler, blackList, storageBigMessages);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		bot.loadData();
		Vote.initVotingStructure();
	}

	private static void startSetupUI() {
		Logger.info(

				new Object[] { "Starting setup UI...." });
		SetupGUI frame = new SetupGUI(CONFIG);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.toFront();
	}

	private static void startUI() {
		Logger.info(

				new Object[] { "Starting UI...." });
		InterfaceGUI UI = new InterfaceGUI();
		UI.setLocationRelativeTo(null);
		UI.setVisible(true);
		UI.toFront();
	}

	public static long getUptime() {
		RuntimeMXBean rb = ManagementFactory.getRuntimeMXBean();
		return rb.getUptime();
	}

	public static boolean restartApplication() throws URISyntaxException, IOException {
		String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
		File currentJar = new File(Yasu.class.getProtectionDomain().getCodeSource().getLocation().toURI());
		if (!currentJar.getName().endsWith(".jar")) {
			return false;
		}
		ArrayList<String> command = new ArrayList<String>();
		if (System.getProperty("os.name").equalsIgnoreCase("linux")) {
			command.add("screen");
		}
		command.add(javaBin);
		command.add("-jar");
		command.add(currentJar.getPath());
		command.add("plunch");

		ProcessBuilder builder = new ProcessBuilder(command);
		builder.start();
		System.out.println("REEEEEEEEEEEEEEEEEEEEEEEEEEEESTARTING");
		return true;
	}
}
