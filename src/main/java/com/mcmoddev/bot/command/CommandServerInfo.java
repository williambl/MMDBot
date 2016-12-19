package com.mcmoddev.bot.command;

import com.mcmoddev.bot.util.DiscordUtilities;

import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;

public class CommandServerInfo implements Command {
    
    @Override
    public void processCommand (IMessage message, String[] params) {
        
        final IGuild guild = message.getGuild();
        
        if (guild != null)
            DiscordUtilities.sendMessage(message.getChannel(), DiscordUtilities.makeMultilineMessage("People: " + guild.getTotalMemberCount(), "Channels: " + guild.getChannels().size(), "Creation Date: " + guild.getCreationDate().toString()));
    }
    
    @Override
    public String getDescription () {
        
        return "Counts the amount of members in the room.";
    }
}
