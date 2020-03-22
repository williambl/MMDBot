package com.mcmoddev.bot.helpers.dialog;

import java.util.Arrays;
import java.util.List;

public class DialogField {

	public String prompt;
	public String response;
	public List<String> validResponses;

	DialogField(final String prompt, final String... validResponses) {
		this.prompt = prompt;
		this.validResponses = Arrays.asList(validResponses);
	}

	public Boolean isValidResponse(final String response) {
		return validResponses.size() <= 0 || validResponses.contains(response);
	}
}
