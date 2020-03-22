package com.mcmoddev.bot.helpers.dialog;

import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;
import java.util.Map;

public class DialogManager {

	private final Map<Long, IDialog> currentlyOpenDialogs = new HashMap<>();

	public boolean userHasDialog(final Long userID) {
		return currentlyOpenDialogs.containsKey(userID);
	}

	public boolean userHasDialog(final User user) {
		return currentlyOpenDialogs.containsKey(user.getIdLong());
	}

	public IDialog getDialogForUser(final Long userID) {
		return currentlyOpenDialogs.get(userID);
	}

	public IDialog getDialogForUser(final User user) {
		return currentlyOpenDialogs.get(user.getIdLong());
	}

	public void addDialog(final Long userID, final IDialog dialog) {
		currentlyOpenDialogs.put(userID, dialog);
	}

	public void closeDialogForUser(final Long userID) {
		currentlyOpenDialogs.remove(userID);
	}
}
