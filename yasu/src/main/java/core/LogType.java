package core;

public enum LogType {
	DEBUG, INFO, GENERAL, WARN, CRITICAL;

	public static final String ANSI_RESET = "\033[0m";
	public static final String ANSI_BLACK = "\033[30m";
	public static final String ANSI_RED = "\033[31m";
	public static final String ANSI_GREEN = "\033[32m";
	public static final String ANSI_YELLOW = "\033[33m";
	public static final String ANSI_BLUE = "\033[34m";
	public static final String ANSI_PURPLE = "\033[35m";
	public static final String ANSI_CYAN = "\033[36m";
	public static final String ANSI_WHITE = "\033[37m";
	public static final String ANSI_BLACK_BACKGROUND = "\033[40m";
	public static final String ANSI_RED_BACKGROUND = "\033[41m";
	public static final String ANSI_GREEN_BACKGROUND = "\033[42m";
	public static final String ANSI_YELLOW_BACKGROUND = "\033[43m";
	public static final String ANSI_BLUE_BACKGROUND = "\033[44m";
	public static final String ANSI_PURPLE_BACKGROUND = "\033[45m";
	public static final String ANSI_CYAN_BACKGROUND = "\033[46m";
	public static final String ANSI_WHITE_BACKGROUND = "\033[47m";

	private LogType() {
	}

	public String getColor() {
		switch (this) {
		case DEBUG:
			return "\033[32m";
		case INFO:
			return "\033[34m";
		case GENERAL:
			return "\033[37m";
		case WARN:
			return "\033[33m";
		case CRITICAL:
			return "\033[31m";
		}
		return null;
	}
}
