package utilities;

public final class ShortenURL {
	public static String get(String Link) {
		try {
			return JsonUrlParse.readUrl("http://tinyurl.com/api-create.php?url=" + Link);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "ERROR - try again!";
	}
}
