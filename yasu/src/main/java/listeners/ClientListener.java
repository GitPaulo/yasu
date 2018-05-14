package listeners;

import core.Logger;
import net.dv8tion.jda.client.events.group.GroupJoinEvent;
import net.dv8tion.jda.client.events.group.GroupLeaveEvent;
import net.dv8tion.jda.client.events.relationship.FriendAddedEvent;
import net.dv8tion.jda.client.events.relationship.FriendRemovedEvent;
import net.dv8tion.jda.client.events.relationship.FriendRequestCanceledEvent;
import net.dv8tion.jda.client.events.relationship.FriendRequestIgnoredEvent;
import net.dv8tion.jda.client.events.relationship.FriendRequestReceivedEvent;
import net.dv8tion.jda.client.events.relationship.FriendRequestSentEvent;
import net.dv8tion.jda.client.events.relationship.UserBlockedEvent;
import net.dv8tion.jda.client.events.relationship.UserUnblockedEvent;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class ClientListener extends ListenerAdapter {
	public void onFriendAdded(FriendAddedEvent event) {
		Logger.info(new Object[] { "Friend has been added: " + event.getUser().getName() + " relationship: "
				+ event.getRelationshipType().name() });
	}

	public void onFriendRemoved(FriendRemovedEvent event) {
		Logger.info(new Object[] { "Friend has been removed: " + event.getUser().getName() + " relationship: "
				+ event.getRelationshipType().name() });
	}

	public void onUserBlocked(UserBlockedEvent event) {
		Logger.info(new Object[] { "User has been blocked: " + event.getUser().getName() });
	}

	public void onUserUnblocked(UserUnblockedEvent event) {
		Logger.info(new Object[] { "User has been unblocked: " + event.getUser().getName() });
	}

	public void onFriendRequestSent(FriendRequestSentEvent event) {
		Logger.info(new Object[] { "Friend request sent to: " + event.getUser().getName() });
	}

	public void onFriendRequestCanceled(FriendRequestCanceledEvent event) {
		Logger.info(new Object[] { "Friend request canceled: " + event.getUser().getName() });
	}

	public void onFriendRequestReceived(FriendRequestReceivedEvent event) {
		Logger.info(new Object[] { "Friend request received from: " + event.getFriendRequest().getUser().getName()
				+ " ID: " + event.getFriendRequest().getUser().getId() });
		event.getFriendRequest().accept().complete();
		Logger.info(new Object[] { "Accepted friend request!" });
		((PrivateChannel) event.getFriendRequest().getUser().openPrivateChannel().complete()).sendMessage(
				"**Hello there, general ke-!** Uhhhm, nvm that, I have accepted your friend request! Thankyou for your kindness, if you want to know more about me type !help")

				.complete();
	}

	public void onFriendRequestIgnored(FriendRequestIgnoredEvent event) {
		Logger.info(new Object[] { "Friend request ignored: " + event.getFriendRequest().getUser().getName() });
	}

	public void onGroupJoin(GroupJoinEvent event) {
		Logger.info(new Object[] { "Group joined: " + event.getGroup().getName() });
	}

	public void onGroupLeave(GroupLeaveEvent event) {
		Logger.info(new Object[] { "Group left: " + event.getGroup().getName() });
	}
}
