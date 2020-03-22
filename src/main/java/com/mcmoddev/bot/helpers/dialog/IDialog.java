package com.mcmoddev.bot.helpers.dialog;

import net.dv8tion.jda.api.entities.Message;

import java.util.List;

public interface IDialog {
	public List<String> getFields();
	public boolean isDone();
	public String next(Message response);
}
