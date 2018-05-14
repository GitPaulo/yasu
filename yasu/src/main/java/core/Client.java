package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import commands.Command;
import commands.CommandParser;
import music.MusicHandler;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.User;
import utilities.StringSimilar;

public class Client {
	private JDA instance;
	private final HashMap<String, Command> commands;
	private final HashMap<String, String> APIKeys;
	private final String[] prefixes;
	private final CommandParser commandparser;
	private final String hostID;
	private final MusicHandler musicHandler;
	private final ArrayList<User> blackList;
	private final ArrayList<LongMessage> storageBigMessages;

	public Client(JDA instance, HashMap<String, Command> commands, HashMap<String, String> APIKeys, String[] prefixes,
			CommandParser commandparser, String hostID, MusicHandler musicHandler, ArrayList<User> blackList,
			ArrayList<LongMessage> storageBigMessages) {

		this.instance = instance;
		this.commands = commands;
		this.APIKeys = APIKeys;
		this.prefixes = prefixes;
		this.commandparser = commandparser;
		this.hostID = hostID;
		this.musicHandler = musicHandler;
		this.blackList = blackList;
		this.storageBigMessages = storageBigMessages;

	}

	/**
	 * Returns the JDA instance.
	 * 
	 * @return
	 */
	public JDA getInstance() {
		return instance;
	}

	/**
	 * Returns the hashmap with all the bot comman instances.
	 * 
	 * @return
	 */
	public HashMap<String, Command> getCommands() {
		return commands;
	}

	/**
	 * Returns the String Array of the prefixes the bot uses.
	 * 
	 * @return
	 */
	public String[] getPrefixes() {
		return prefixes.clone();
	}

	/**
	 * Returns the parser used for the commands.
	 * 
	 * @return
	 */
	public CommandParser getCommandparser() {
		return commandparser;
	}

	/**
	 * Returns the Host ID. Init at setup.
	 * 
	 * @return
	 */
	public String getHostID() {
		return hostID;
	}

	/**
	 * Returns the music handler instance used by the bots music system.
	 * 
	 * @return
	 */
	public MusicHandler getMusicHandler() {
		return musicHandler;
	}

	/**
	 * Returns an array of Long Messages currently up on this instance.
	 * 
	 * @return
	 */
	public ArrayList<LongMessage> getStorageBigMessages() {
		return storageBigMessages;
	}

	// Data Control
	public boolean isBlackListed(User user) {
		return blackList.contains(user);
	}

	public void addBlackList(User user) {
		blackList.add(user);
	}

	public void removeBlackList(User user) {
		blackList.remove(user);
	}

	public String[] getBlackList() {
		String[] data = new String[blackList.size()];
		Iterator<User> it = blackList.iterator();

		int i = 0;
		while (it.hasNext()) {
			data[i] = it.next().getAsMention();
			i++;
		}

		return data;
	}

	public String getAPIKey(String id) {
		return APIKeys.get(id);
	}

	/**
	 * Loads last-stored instance data to client instance. MAY OVERWRITE CURRENT
	 * DATA!
	 */
	public void loadData() {
		// load things from json storage
		JSONObject jobject = Yasu.DATA.getJSONData();
		if (jobject != null) { // Makes sure this is not the first load.
			JSONArray jblist = (JSONArray) jobject.get("blackList");
			for (int i = 0; i < jblist.length(); i++) {
				String id = (String) jblist.get(i);
				blackList.add(getInstance().getUserById(id.substring(id.indexOf("(") + 1, id.indexOf(")")))); // get by
																												// id
																												// stored
																												// in
																												// json
			}
		}
		Logger.info("Loaded last stored instance data to instance!");
	}

	/**
	 * Saves the instance data to storage.
	 */
	public void saveData() {
		JSONObject json = new JSONObject();
		// Serialise black list
		JSONArray blist = new JSONArray();
		for (User v : blackList) {
			blist.put(v.getName() + "(" + v.getId() + ")");
		}
		// Append to JSONObject and store
		json.put("blackList", blist);
		Yasu.DATA.saveJSONData(json);
		Logger.info("Saved current instance data to storage!");
	}

	/**
	 * Tries to find a user by any means necessary
	 * 
	 * @param str
	 * @return
	 */
	public static User getUserbyString(String str) {
		List<User> users = Yasu.bot.getInstance().getUsers();
		float highest = 0;
		User target = null;
		for (User v : users) {
			String id = (str.chars().allMatch(Character::isDigit)) ? v.getId() : v.getName(); // choose between use id
																								// or name
			float cmp = StringSimilar.getDistance(str, id, 2);

			// found him (one of him)
			if (cmp == 1)
				return v;

			// keep collecting the closest to him
			if (cmp > highest) {
				highest = cmp;
				target = v;
			}
		}

		Logger.debug("Value of match: " + highest);
		if (highest < 0.3) // bull shit was entered
			return null;

		return target;
	}
}