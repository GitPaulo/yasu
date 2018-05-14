package core;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

public class Logger {
	private static final String PREMESSAGE = "YASU-LOGS";
	private static final boolean LOG_PRINT_STACK_SOURCE = false;
	private static final LogType MINLOGLEVEL = LogType.DEBUG;
	private static final boolean LOG_SAVE_TO_FILE = true;
	private static final StorageHandler storageHandler = new StorageHandler("yasu_logs.txt");
	private static final String[] LOGS = new String['?'];
	private static int head = 0;

	public static void print(Object... text) {
		log(LogType.GENERAL, text);
	}

	public static void printf(String text, Object... args) {
		log(LogType.GENERAL, new Object[] { String.format(text, args) });
	}

	public static void debug(Object... text) {
		log(LogType.DEBUG, text);
	}

	public static void debugf(String text, Object... args) {
		log(LogType.DEBUG, new Object[] { String.format(text, args) });
	}

	public static void info(Object... text) {
		log(LogType.INFO, text);
	}

	public static void infof(String text, Object... args) {
		log(LogType.INFO, new Object[] { String.format(text, args) });
	}

	public static void critical(Object... text) {
		log(LogType.CRITICAL, text);
	}

	public static void criticalf(String text, Object... args) {
		log(LogType.CRITICAL, new Object[] { String.format(text, args) });
	}

	private static void log(LogType type, Object... text) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String str = null;
		if (System.getProperty("os.name").contains("Windows")) {
			str = String.format("[%s][%s][%s] >> %s",
					new Object[] { sdf.format(cal.getTime()), "YASU-LOGS", type, Arrays.toString(text) });
		} else {
			str = String.format("%s[%s][%s][%s]%s >> %s", new Object[] { type.getColor(), sdf.format(cal.getTime()),
					"YASU-LOGS", type, "\033[0m", Arrays.toString(text) });
		}
		System.out.println(str);

		saveToFile(str);
	}

	private static void saveToFile(String str) {
		if (head > 999) {
			Arrays.fill(LOGS, null);
			head = 0;
		}
		LOGS[head] = str;
		head += 1;
		storageHandler.appendToTextFile(str);
	}

	public static String[] getLogs() {
		return (String[]) LOGS.clone();
	}

	public static int getHead() {
		return head;
	}
}
