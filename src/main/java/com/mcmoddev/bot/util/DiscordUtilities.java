package com.mcmoddev.bot.util;

import com.google.common.collect.Lists;
import com.mcmoddev.bot.MMDBot;
import org.apache.commons.io.FileUtils;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import sx.blah.discord.util.RequestBuffer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class DiscordUtilities {
    
    /**
     * Static reference to the line seperator on the current operating system.
     */
    public static final String SEPERATOR = System.lineSeparator();
    
    /**
     * A wrapper for {@link FileUtils#copyURLToFile(URL, File)}. Allows for quick download of
     * files based on input from users.
     *
     * @param site The site/url to download the file from.
     * @param fileName The location to save the file to.
     * @return The file that was downloaded.
     */
    public static File downloadFile (String site, String fileName) {
        
        final File file = new File(fileName);
        
        try {
            
            FileUtils.copyURLToFile(new URL(site), file);
        }

        catch (final IOException e) {
            
            e.printStackTrace();
        }
        
        return file;
    }

    /**
     * Makes a String message italicized. This only applies to chat.
     * 
     * @param message The message to format.
     * @return String The message with the formatting codes applied.
     */
    public static String makeItalic (String message) {
        
        return "*" + message + "*";
    }
    
    /**
     * Makes a String message bold. This only applies to chat.
     * 
     * @param message The message to format.
     * @return String The message with the bold formatting codes applied.
     */
    public static String makeBold (String message) {
        
        return "**" + message + "**";
    }
    
    /**
     * Makes a String message scratched out. This only applies to chat.
     * 
     * @param message The message to format.
     * @return String The message with the scratched out formatting codes applied.
     */
    public static String makeScratched (String message) {
        
        return "~~" + message + "~~";
    }
    
    /**
     * Makes a String message underlined. This only applies to chat.
     * 
     * @param message The message to format.
     * @return String The message with the underlined formatting codes applied.
     */
    public static String makeUnderlined (String message) {
        
        return "__" + message + "__";
    }
    
    /**
     * Makes a String message appear in a code block. This only applies to chat.
     * 
     * @param message The message to format.
     * @return String The message with the code block format codes applied.
     */
    public static String makeCodeBlock (String message) {
        
        return "`" + message + "`";
    }
    
    /**
     * Makes a string which represents multiple lines of text.
     * 
     * @param lines The lines of text to display. Each entry is a new line.
     * @return A string which has been split up.
     */
    public static String makeMultilineMessage (String... lines) {
        
        String text = "";
        
        for (final String line : lines)
            text += line + SEPERATOR;
        
        return text;
    }
    
    /**
     * Makes a String message appear in a multi-lined code block. This only applies to chat.
     * 
     * @param message The message to format.
     * @return String The message with the multi-lined code block format codes applied.
     */
    public static String makeMultiCodeBlock (String message) {
        
        return "```" + message + "```";
    }
    
    /**
     * Attempts to send a private message to a user. If a private message channel does not
     * already exist, it will be created.
     *
     * @param user The user to send the private message to.
     * @param message The message to send to the user.
     */
    public static void sendPrivateMessage (IUser user, String message) {
        
        try {
            
            sendMessage(MMDBot.INSTANCE.getOrCreatePMChannel(user), message);
        }
        
        catch (final Exception e) {
            
            e.printStackTrace();
        }
    }
    
    public static void sendMessage (IChannel channel, String message, EmbedObject object) {

        RequestBuffer.request(() -> {
            try {
                channel.sendMessage(message, object, false);
            } catch (DiscordException | MissingPermissionsException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Sends a message into the chat. This version of the method will handle exceptions for
     * you.
     *
     * @param channel The channel to send to message to.
     * @param message The message to send to the channel.
     */
    public static IMessage sendMessage (IChannel channel, String message) {

        if (message.contains("@")) {

            DiscordUtilities.sendMessage(channel, "I tried to send a message, but it contained an @. I can not ping people!");
            System.out.println(message);
            return null;
        }

        if (message.length() > 2000) {

            DiscordUtilities.sendMessage(channel, "I tried to send a message, but it was too long. " + message.length() + "/2000 chars!");
            System.out.println(message);
            return null;
        }

        RequestBuffer.RequestFuture<IMessage> request = RequestBuffer.request(() -> {
            try {
                return channel.sendMessage(message);
            } catch (MissingPermissionsException | DiscordException e) {
                e.printStackTrace();
            }
            return null;
        });
        return request.get();
    }

    public static void editMessage(IMessage msg, String message, EmbedObject embed) {
        RequestBuffer.request(() -> {
            try {
                msg.edit(message, embed);
            } catch (MissingPermissionsException | DiscordException e) {
                e.printStackTrace();
            }
        });
    }
}