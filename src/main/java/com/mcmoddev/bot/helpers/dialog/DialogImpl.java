package com.mcmoddev.bot.helpers.dialog;

import com.mcmoddev.bot.MMDBot;
import net.dv8tion.jda.api.entities.Message;

import java.util.ArrayList;
import java.util.List;

public class DialogImpl implements IDialog {

	protected List<DialogField> fields = new ArrayList<>();
	protected int currentField = 0;
	protected IDialogFinishListener listener;

	public DialogImpl(IDialogFinishListener listener) {
		this.listener = listener;
	}

	@Override
	public List<DialogField> getFields() {
		return fields;
	}

	@Override
	public boolean isDone() {
		return currentField+1 < fields.size();
	}

	@Override
	public String begin() {
		return fields.get(currentField).prompt;
	}

	@Override
	public String next(Message response) {
		final DialogField field = fields.get(currentField);
		final String responseString = response.getContentRaw();
		if (field.isValidResponse(responseString)) {
			fields.get(currentField).response = response.getContentRaw();
			currentField++;
			if (!isDone()) {
				return fields.get(currentField).prompt;
			}
			return "Finished.";
		}
		else {
			MMDBot.getDialogManager().closeDialogForUser(response.getAuthor().getIdLong());
			listener.onDialogFinished(this);
			return String.format("Invalid response. Valid responses are: %s", field.validResponses);
		}
	}
}
