package com.mcmoddev.mmdbot.modules;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.mcmoddev.mmdbot.MMDBot;
import com.mcmoddev.mmdbot.modules.commands.bot.info.CmdAbout;
import com.mcmoddev.mmdbot.modules.commands.bot.info.CmdUptime;
import com.mcmoddev.mmdbot.modules.commands.bot.management.CmdAvatar;
import com.mcmoddev.mmdbot.modules.commands.bot.management.CmdRename;
import com.mcmoddev.mmdbot.modules.commands.general.fun.CmdCatFacts;
import com.mcmoddev.mmdbot.modules.commands.general.fun.CmdGreatMoves;
import com.mcmoddev.mmdbot.modules.commands.general.info.CmdEventsHelp;
import com.mcmoddev.mmdbot.modules.commands.general.info.CmdFabricVersion;
import com.mcmoddev.mmdbot.modules.commands.general.info.CmdForgeVersion;
import com.mcmoddev.mmdbot.modules.commands.general.info.CmdJustAsk;
import com.mcmoddev.mmdbot.modules.commands.general.info.CmdMe;
import com.mcmoddev.mmdbot.modules.commands.general.info.CmdMinecraftVersion;
import com.mcmoddev.mmdbot.modules.commands.general.info.CmdPaste;
import com.mcmoddev.mmdbot.modules.commands.general.info.CmdSearch;
import com.mcmoddev.mmdbot.modules.commands.general.info.CmdXy;
import com.mcmoddev.mmdbot.modules.commands.server.CmdGuild;
import com.mcmoddev.mmdbot.modules.commands.server.CmdReadme;
import com.mcmoddev.mmdbot.modules.commands.server.CmdRoles;
import com.mcmoddev.mmdbot.modules.commands.server.CmdRules;
import com.mcmoddev.mmdbot.modules.commands.server.CmdToggleEventPings;
import com.mcmoddev.mmdbot.modules.commands.server.CmdToggleMcServerPings;
import com.mcmoddev.mmdbot.modules.commands.server.moderation.CmdCommunityChannel;
import com.mcmoddev.mmdbot.modules.commands.server.moderation.CmdMute;
import com.mcmoddev.mmdbot.modules.commands.server.moderation.CmdOldChannels;
import com.mcmoddev.mmdbot.modules.commands.server.moderation.CmdUnmute;
import com.mcmoddev.mmdbot.modules.commands.server.moderation.CmdUser;

/**
 * This is the main class for setting up commands before they are loaded in by the bot,
 * this way we can disable and enable them at will. Or at least that is the hope.
 *
 * @author ProxyNeko
 */
public class CommandModule {

    /**
     * Setup and load the bots command module.
     */
    public static void setupCommandModule() {
        final var commandListener = new CommandClientBuilder()
            .setOwnerId(MMDBot.getConfig().getOwnerID())
            .setPrefix(MMDBot.getConfig().getMainPrefix())
            .setAlternativePrefix(MMDBot.getConfig().getAlternativePrefix())
            .addCommand(new CmdGuild())
            .addCommand(new CmdAbout())
            .addCommand(new CmdMe())
            .addCommand(new CmdUser())
            .addCommand(new CmdRoles())
            .addCommand(new CmdJustAsk())
            .addCommand(new CmdPaste())
            .addCommand(new CmdXy())
            .addCommand(new CmdReadme())
            .addCommand(new CmdRules())
            .addCommand(new CmdCatFacts())
            .addCommand(new CmdSearch("google", "https://www.google.com/search?q=", "goog"))
            .addCommand(new CmdSearch("bing", "https://www.bing.com/search?q="))
            .addCommand(new CmdSearch("duckduckgo", "https://duckduckgo.com/?q=", "ddg"))
            .addCommand(new CmdSearch("lmgtfy", "https://lmgtfy.com/?q=", "let-me-google-that-for-you"))
            .addCommand(new CmdEventsHelp())
            .addCommand(new CmdToggleMcServerPings())
            .addCommand(new CmdToggleEventPings())
            .addCommand(new CmdForgeVersion())
            .addCommand(new CmdMinecraftVersion())
            .addCommand(new CmdFabricVersion())
            .addCommand(new CmdMute())
            .addCommand(new CmdUnmute())
            .addCommand(new CmdCommunityChannel())
            .addCommand(new CmdOldChannels())
            .addCommand(new CmdGreatMoves())
            .addCommand(new CmdAvatar())
            .addCommand(new CmdRename())
            .addCommand(new CmdUptime())
            //TODO Setup DB storage for tricks and polish them off/add permission restrictions for when needed.
            /*
             .addCommand(new CmdAddTrick())
             .addCommand(new CmdListTricks())
             .addCommand(new CmdRemoveTrick())
             .addCommands(Tricks.createTrickCommands().toArray(new Command[0]))
             */
            .setHelpWord("help")
            .build();

            if (MMDBot.getConfig().isCommandModuleEnabled()) {
                MMDBot.getInstance().addEventListener(commandListener);
                MMDBot.LOGGER.warn("Command module enabled and loaded.");
            } else {
                MMDBot.LOGGER.warn("Command module disabled via config, commands will not work at this time!");
            }
    }
}
