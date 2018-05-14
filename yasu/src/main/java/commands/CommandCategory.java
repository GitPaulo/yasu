package commands;

import java.util.ArrayList;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.utils.PermissionUtil;

public enum CommandCategory {
	DEVELOPER("developer", "Developing", "Category of commands reserved for developers.",
			new ArrayList()), ADMINISTRATIVE("administrative", "Administration", "Administrator only commands.",
					new ArrayList()), INFORMATIVE("informative", "Information", "Informative commands.",
							new ArrayList()), FUN("fun", "Fun", "Fun section of commands.", new ArrayList()), UTILITY(
									"utility", "Utility", "Uitlity based commands.", new ArrayList()), MUSIC("utility",
											"Utility", "Commands for the MUSIC system",
											new ArrayList()), UNKNOWN("unknown", "Unknown",
													"Other category of commands", new ArrayList());

	private final String ID;
	private final String displayName;
	private final String description;
	private final ArrayList<Permission> neededPermissions;

	private CommandCategory(String ID, String displayName, String description,
			ArrayList<Permission> neededPermissions) {
		this.ID = ID;
		this.displayName = displayName;
		this.description = description;
		this.neededPermissions = neededPermissions;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public String getID() {
		return this.ID;
	}

	public String getDesciption() {
		return this.description;
	}

	public ArrayList<Permission> getNeededPermissions() {
		return this.neededPermissions;
	}

	public boolean hasPermissions(Member m) {
		for (Permission v : this.neededPermissions) {
			if (!PermissionUtil.checkPermission(m, new Permission[] { v })) {
				return false;
			}
		}
		return true;
	}
}
