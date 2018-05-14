package commands.utility;

import commands.Command;
import commands.CommandCategory;
import commands.CommandScope;
import core.DiscordMessage;
import core.Yasu;

import java.awt.Color;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utilities.BeanShellJava;

public class JavaRun implements Command {
	private static final HashMap<String, String> subcommands = new HashMap<String, String>();
	private static long TIMEOUT_SECONDS = 20L;

	static {
		subcommands.put("exec <expression>", "A java expression to be parsed.");
		subcommands.put("timeout <seconds>", "How long the checker thread will wait for the code execution thread to terminate.");
	}

	public boolean called(String raw, String[] args, MessageReceivedEvent event) {
		if (args.length < 2) {
			event.getTextChannel().sendMessage(DiscordMessage.embedProblem(event.getAuthor(), "Invalid Arguments!"))
					.queue();
			return false;
		}
		return true;
	}

	public void action(String raw, final String[] args, final MessageReceivedEvent event) {
		String scmd = args[0];
		args[0] = "";
		if( scmd.equalsIgnoreCase("exec") ) {	
			final String code = String.join(" ", args);
			event.getChannel()
					.sendMessage(DiscordMessage.embedMessage(event.getAuthor(), "Java Interpreter",
							event.getMessage().getAuthor().getAsMention() + " ran java code:\n `" + code + "`",
							Color.YELLOW, "http://logodatabases.com/wp-content/uploads/2012/03/java-icon.png"))
	
					.queue();
	
			final Thread thread = new Thread(new Runnable() {
				public void run() {
					String output = BeanShellJava.interpret(code).equals("") ? "[JAVA] No output from interpreter!"
							: BeanShellJava.interpret(String.join(" ", args));
					event.getChannel()
							.sendMessage(DiscordMessage.embedMessage(event.getAuthor(), "Java Interpreter - Output", output,
									Color.YELLOW, "http://logodatabases.com/wp-content/uploads/2012/03/java-icon.png"))
							.queue();
				}
			});
			thread.setName("External Java Code Thread - [NASTY STUFF]");
	
			new Thread(new Runnable() {
				public void run() {
					ExecutorService es = Executors.newFixedThreadPool(1);
					Future<?> future = es.submit(thread);
					try {
						future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
					} catch (TimeoutException | InterruptedException | ExecutionException e) {
						future.cancel(true);
						event.getChannel()
								.sendMessage(DiscordMessage.embedMessage(event.getAuthor(), "Java Interpreter - ERROR",
										"Thread running the code took to long to respond. What are you doing to me D:< ?!",
										Color.YELLOW, "http://logodatabases.com/wp-content/uploads/2012/03/java-icon.png"))
								.queue();
					}
				}
			}).start();
		} else if ( scmd.equalsIgnoreCase("timeout") ) {
			if(!event.getAuthor().getId().equals(Yasu.DEVID)) {
				event.getChannel()
				.sendMessage(DiscordMessage.embedProblem(event.getAuthor(), "Not Allowed to do that m8! Sorry :/"))
				.queue();
				return;
			}
			try {
				TIMEOUT_SECONDS = Integer.parseInt(args[1]);
			}catch (NumberFormatException e) {
				event.getChannel().sendMessage(DiscordMessage.embedProblem(event.getAuthor(), "Invalid timeout argument! It must be a number!")).queue();;
			}
			event.getChannel()
			.sendMessage(DiscordMessage.embedMessage(event.getAuthor(), "Java Interpreter - Settings",
					"Thread time-out set to: " + TIMEOUT_SECONDS + "secs.",
					Color.YELLOW, "http://logodatabases.com/wp-content/uploads/2012/03/java-icon.png"))
			.queue();
		}
	}

	public String getDescription() {
		return "Parses a java expression and returns the output to chat";
	}

	public HashMap<String, String> getSubCommands() {
		return subcommands;
	}

	public CommandCategory getCategory() {
		return CommandCategory.UTILITY;
	}

	public CommandScope getScope() {
		return CommandScope.BOTH;
	}

	public boolean isListed() {
		return true;
	}

	public void executed(boolean succ, MessageReceivedEvent event) {
	}
}
