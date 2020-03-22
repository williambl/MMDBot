package com.mcmoddev.bot.events;

import com.mcmoddev.bot.MMDBot;
import com.mcmoddev.bot.helpers.dialog.DialogManager;
import com.mcmoddev.bot.helpers.dialog.IDialog;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EventPrivateMessageReceived extends ListenerAdapter {

	@Override
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
		final DialogManager manager = MMDBot.getDialogManager();
		final User user = event.getAuthor();
		final PrivateChannel channel = event.getChannel();
		if (manager.userHasDialog(user)) {
			final IDialog dialog = manager.getDialogForUser(user);
			channel.sendMessage(dialog.next(event.getMessage())).queue();
		}
	}
}
