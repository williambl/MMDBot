package com.mcmoddev.bot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.mcmoddev.bot.MMDBot;
import com.mcmoddev.bot.helpers.dialog.DialogField;
import com.mcmoddev.bot.helpers.dialog.IDialog;
import com.mcmoddev.bot.helpers.dialog.RequestCreationDialog;
import com.mcmoddev.bot.misc.BotConfig;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;


/**
 *
 */
public final class CmdCreateRequest extends Command {

	/**
	 *
	 */
	public CmdCreateRequest() {
		super();
		name = "create-request";
		aliases = new String[]{"request"};
		help = "Creates a request.";
	}

	/**
	 *
	 */
	@Override
	protected void execute(final CommandEvent event) {
		final User user = event.getAuthor();
		final PrivateChannel channel = user.openPrivateChannel().complete();
		final IDialog dialog = new RequestCreationDialog(this::onFinish);
		channel.sendMessage(dialog.begin()).queue();
		MMDBot.getDialogManager().addDialog(user.getIdLong(), dialog);
	}

	protected void onFinish(IDialog dialog) {
		final BotConfig config = MMDBot.getConfig();
		final Guild guild = MMDBot.getInstance().getGuildById(config.getGuildID());
		if (guild == null) return;
		final TextChannel channel = guild.getTextChannelById(config.getChannelIDRequests());
		final List<DialogField> fields = dialog.getFields();
		channel.sendMessageFormat("Author: %s" +
			"Paid: %s" +
			"Request Type: %s" +
			"Details: %s" +
			"Images: %s", "", fields.get(0), fields.get(1), fields.get(2), fields.get(3)).queue();
	}
}
