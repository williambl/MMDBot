package com.mcmoddev.bot.helpers.dialog;

public class RequestCreationDialog extends DialogImpl {

	public RequestCreationDialog(IDialogFinishListener listener) {
		super(listener);
		fields.add(new DialogField("Paid? Yes/No", "Yes", "No"));
		fields.add(new DialogField("Request Type:"));
		fields.add(new DialogField("Details:"));
		fields.add(new DialogField("Images:"));
	}

}
