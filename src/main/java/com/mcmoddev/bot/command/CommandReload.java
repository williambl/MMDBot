package com.mcmoddev.bot.command;

import com.mcmoddev.bot.MMDBot;
import com.mcmoddev.bot.util.DiscordUtilities;

import sx.blah.discord.handle.obj.IMessage;

public class CommandReload extends CommandAdmin {
    
    @Override
    public void processCommand (IMessage message, String[] params) {
        
        DiscordUtilities.sendMessage(message.getChannel(), "Reloading handlers and resource!");
        MMDBot.initHandlers();
        DiscordUtilities.sendMessage(message.getChannel(), "Reload complete. That tickled ;)");
    }
    
    @Override
    public String getDescription () {
        
        return "Reloads all of the handlers and resources for the bot, including commands!";
    }
}