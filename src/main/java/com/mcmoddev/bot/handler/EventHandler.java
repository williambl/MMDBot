package com.mcmoddev.bot.handler;

import com.mcmoddev.bot.MMDBot;
import com.mcmoddev.bot.libs.Constants;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.*;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;

import java.time.LocalDateTime;

public class EventHandler {

    @EventSubscriber
    public void onChannelCreate(ChannelCreateEvent event) {
        IChannel channel = event.getChannel();
        MMDBot.DATABASE.createOrUpdateChannel(channel);
    }

    @EventSubscriber
    public void onChannelUpdate(ChannelUpdateEvent event) {
        IChannel channel = event.getNewChannel();
        MMDBot.DATABASE.createOrUpdateChannel(channel);
    }

    @EventSubscriber
    public void onJoin(UserJoinEvent event) {
        MMDBot.DATABASE.userJoin(event.getUser(), event.getGuild(),event.getJoinTime());
        MMDBot.DATABASE.incrementJoinDateCount(event.getJoinTime());
    }

    @EventSubscriber
    public void onLeave(UserLeaveEvent event) {
        LocalDateTime ldt = LocalDateTime.now();
        MMDBot.DATABASE.userLeave(event.getUser(), event.getGuild(), ldt);
        MMDBot.DATABASE.incrementLeaveDateCount(ldt);
    }

    @EventSubscriber
    public void onGuild(ReadyEvent event) {
        for (IGuild guild : event.getClient().getGuilds()) {
            setupGuild(guild);
        }
    }

    @EventSubscriber
    public void onGuild(AllUsersReceivedEvent event) {
        setupGuild(event.getGuild());
    }

    public void setupGuild(IGuild guild) {
        boolean dirty = false;

        for (IChannel channel : guild.getChannels()) {
            MMDBot.DATABASE.createOrUpdateChannel(channel);
        }
        Constants.LOG.info("Started loading Users");
        for (IUser user : guild.getUsers()) {
            if (!user.isBot() && !MMDBot.DATABASE.isExistingUser(guild, user)) {
                try {
                    MMDBot.DATABASE.userJoin(user, guild, guild.getJoinTimeForUser(user));
                } catch (DiscordException e) {
                    e.printStackTrace();
                }
                dirty = true;
            }
        }
        Constants.LOG.info("Loaded users");

        if (dirty) {
            MMDBot.DATABASE.createOrUpdateJoinLeaveDate();
        }
        Constants.LOG.info("Setup user joined complete");
    }

    @EventSubscriber
    public void onMessageReceived(MessageReceivedEvent event) {

        if (event.getMessage().getContent().startsWith(CommandHandler.COMMAND_KEY))
            CommandHandler.attemptCommandTriggers(event.getMessage());

        MMDBot.DATABASE.incrementChannelCount(event.getMessage().getChannel().getID(), event.getMessage().getCreationDate());
    }
}
