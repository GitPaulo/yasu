package commands;

import net.dv8tion.jda.core.entities.ChannelType;

public enum CommandScope {
	BOTH, PUBLIC, PRIVATE;

	private CommandScope() {
	}

	public static boolean matchChannelType(ChannelType ct, CommandScope cs) {
		if ((cs.equals(BOTH)) || ((ct.equals(ChannelType.PRIVATE)) && (cs.equals(PRIVATE)))
				|| ((ct.equals(ChannelType.TEXT)) && (cs.equals(PUBLIC)))) {
			return true;
		}
		return false;
	}
}
