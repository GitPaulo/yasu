package commands.utility;

import java.util.HashMap;
import java.util.Random;

import commands.Command;
import commands.CommandCategory;
import commands.CommandScope;
import core.DiscordMessage;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class OverwatchRoulette implements Command {
	
	private static final HashMap<String, String> subcommands = new HashMap<String, String>();
	
	private enum Role {
		DPS, SUPPORT, TANK
	}
	
	private class Hero {
		String name;
		Role role;
		
		public Hero(String name, Role role) {
			this.name = name;
			this.role = role;
		}
	}
	
	private Hero[] heroData = {
			new Hero("Tracer", Role.DPS),
			new Hero("Reaper", Role.DPS),
			new Hero("Widowmaker", Role.DPS),
			new Hero("Pharah", Role.DPS),
			new Hero("Reinhardt", Role.TANK),
			new Hero("Mercy", Role.SUPPORT),
			new Hero("Torbjorn", Role.DPS),
			new Hero("Hanzo", Role.DPS),
			new Hero("Winston", Role.TANK),
			new Hero("Zenyatta", Role.SUPPORT),
			new Hero("Bastion", Role.DPS),
			new Hero("Symmetra", Role.DPS),
			new Hero("Zarya", Role.TANK),
			new Hero("Mcree", Role.DPS),
			new Hero("Soldier 76", Role.DPS),
			new Hero("Lucio", Role.SUPPORT),
			new Hero("Roadhog", Role.TANK),
			new Hero("Junkrat", Role.DPS),
			new Hero("D.Va", Role.TANK),
			new Hero("Mei", Role.DPS),
			new Hero("Genji", Role.DPS),
			new Hero("Ana", Role.SUPPORT),
			new Hero("Sombra", Role.DPS),
			new Hero("Orisa", Role.TANK),
			new Hero("Doomfist", Role.DPS),
			new Hero("Moira", Role.SUPPORT),
			new Hero("Brigitte", Role.SUPPORT),
			new Hero("Wrecking Ball", Role.TANK),
			new Hero("Ashe", Role.DPS),			
	};
	
	@Override
	public boolean called(String raw, String[] args, MessageReceivedEvent event) {
		if (args.length == 0) {
			event.getChannel().sendMessage(DiscordMessage.embedProblem(event.getAuthor(), "Invalid arguments!"))
					.queue();
			return false;
		}
		return true;
	}

	@Override
	public void action(String raw, String[] args, MessageReceivedEvent event) {
		if( args.length > heroData.length ) {
			event.getChannel().sendMessage(DiscordMessage.embedProblem(event.getAuthor(), "Too many players for the #heroes!"));
			return;
		}
		
		Random ran = new Random();
		
		String str = "";
		
		for( String name : args ) {
			int cr = ran.nextInt(heroData.length);
			str = str + name + " is playing **" + heroData[cr].name + "**\n";
		}
		
		event.getChannel().sendMessage(str).queue();
	}

	@Override
	public void executed(boolean succ, MessageReceivedEvent event) {
	}

	@Override
	public String getDescription() {
		return "Random Overwatch Hero Roulette!";
	}

	@Override
	public HashMap<String, String> getSubCommands() {
		return subcommands;
	}

	@Override
	public CommandCategory getCategory() {
		return CommandCategory.UTILITY;
	}

	@Override
	public CommandScope getScope() {
		return CommandScope.BOTH;
	}

	@Override
	public boolean isListed() {
		return true;
	}

}
