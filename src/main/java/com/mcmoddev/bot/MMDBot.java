package com.mcmoddev.bot;

import com.google.gson.Gson;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.mcmoddev.bot.commands.CmdPaste;
import com.mcmoddev.bot.commands.CmdToggleMcServerPings;
import com.mcmoddev.bot.commands.CmdXy;
import com.mcmoddev.bot.commands.info.*;
import com.mcmoddev.bot.commands.search.CmdBing;
import com.mcmoddev.bot.commands.search.CmdDuckDuckGo;
import com.mcmoddev.bot.commands.search.CmdGoogle;
import com.mcmoddev.bot.commands.search.CmdLmgtfy;
import com.mcmoddev.bot.commands.staff.CmdMute;
import com.mcmoddev.bot.commands.staff.CmdUnmute;
import com.mcmoddev.bot.commands.staff.CmdUser;
import com.mcmoddev.bot.events.MiscEvents;
import com.mcmoddev.bot.events.users.*;
import com.mcmoddev.bot.helpers.dialog.DialogManager;
import com.mcmoddev.bot.misc.BotConfig;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 *
 */
public final class MMDBot {


	/**
	 *
	 */
	public static final String NAME = "MMDBot";

	/**
	 *
	 */
	public static final String VERSION = "3.0";

	/**
	 *
	 */
	public static final String ISSUE_TRACKER = "https://github.com/MinecraftModDevelopment/MMDBot/issues/";

	/**
	 *
	 */
	public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

	/**
	 *
	 */
	private static BotConfig config; // = new BotConfig("mmdbot_config.json");

	/**
	 *
	 */
	private static DialogManager dialogManager = new DialogManager();

	/**
	 * @return The Bots configuration.
	 */
	public static BotConfig getConfig() {
		return config;
	}

	/**
	 *
	 */
	private MMDBot() {
	}

	private static JDA INSTANCE;

	public static JDA getInstance() {
		return INSTANCE;
	}

	public static DialogManager getDialogManager() {
		return dialogManager;
	}

	/**
	 * @param args Arguments provided to the program.
	 */
	public static void main(final String[] args) {
		final File configFile = new File("mmdbot_config.json");
		if (configFile.exists()) {
			try (InputStreamReader reader = new InputStreamReader(new FileInputStream(configFile), StandardCharsets.UTF_8)) {
				config = new Gson().fromJson(reader, BotConfig.class);
			} catch (final IOException exception) {
				MMDBot.LOGGER.trace("Failed to read config...", exception);
			}
		} else {
			try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(configFile), StandardCharsets.UTF_8)) {
				new Gson().toJson(new BotConfig(), writer);
			} catch (final FileNotFoundException exception) {
				MMDBot.LOGGER.error("An FileNotFound occurred...", exception);
			} catch (final IOException exception) {
				MMDBot.LOGGER.error("An IOException occurred...", exception);
			}
			MMDBot.LOGGER.error("New Config generated please edit and add a bot token to the config and start the bot.");
			System.exit(0);
//          throw new RuntimeException(null, null);
		}

		try {
			final JDABuilder botBuilder = new JDABuilder(AccountType.BOT).setToken(config.getBotToken());

			botBuilder.addEventListeners(new EventUserJoined());
			botBuilder.addEventListeners(new EventUserLeft());
			botBuilder.addEventListeners(new EventNicknameChanged());
			botBuilder.addEventListeners(new EventRoleAdded());
			botBuilder.addEventListeners(new EventRoleRemoved());
			botBuilder.addEventListeners(new EventReactionAdded());
			botBuilder.addEventListeners(new MiscEvents());

			final CommandClientBuilder commandBuilder = new CommandClientBuilder();

			commandBuilder.setOwnerId(MMDBot.getConfig().getOwnerID());
			commandBuilder.setPrefix(MMDBot.getConfig().getPrefix());
			commandBuilder.addCommand(new CmdGuild());
			commandBuilder.addCommand(new CmdMe());
			commandBuilder.addCommand(new CmdUser());
			commandBuilder.addCommand(new CmdRoles());
			commandBuilder.addCommand(new CmdJustAsk());
			commandBuilder.addCommand(new CmdPaste());
			commandBuilder.addCommand(new CmdXy());
			commandBuilder.addCommand(new CmdReadme());
			commandBuilder.addCommand(new CmdBing());
			commandBuilder.addCommand(new CmdDuckDuckGo());
			commandBuilder.addCommand(new CmdGoogle());
			commandBuilder.addCommand(new CmdLmgtfy());
			commandBuilder.addCommand(new CmdEventsHelp());
			commandBuilder.addCommand(new CmdToggleMcServerPings());
			commandBuilder.addCommand(new CmdForgeVersion());
			commandBuilder.addCommand(new CmdMute());
			commandBuilder.addCommand(new CmdUnmute());
			commandBuilder.setHelpWord("help");

			final CommandClient commandListener = commandBuilder.build();
			botBuilder.addEventListeners(commandListener);
			INSTANCE = botBuilder.build();
		} catch (final LoginException exception) {
			LOGGER.error("Error logging in the bot! Please give the bot a valid token in the config file.", exception);
			System.exit(1);
			//throw new RuntimeException(null, null);
		}
	}
}
