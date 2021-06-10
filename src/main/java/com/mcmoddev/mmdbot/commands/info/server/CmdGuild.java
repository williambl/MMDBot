package com.mcmoddev.mmdbot.commands.info.server;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.mcmoddev.mmdbot.core.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Locale;

/**
 *
 * @author
 *
 */
public final class CmdGuild extends Command {

    /**
     *
     */
    public CmdGuild() {
        super();
        name = "guild";
        aliases = new String[]{"server"};
        help = "Gives info about this guild.";
    }

    /**
     * @param event The {@link CommandEvent CommandEvent} that triggered this Command.
     */
    @Override
    protected void execute(final CommandEvent event) {
        if (!Utils.checkCommand(this, event)) {
            return;
        }
        final Guild guild = event.getGuild();
        final EmbedBuilder embed = new EmbedBuilder();
        final TextChannel channel = event.getTextChannel();
        final Instant dateGuildCreated = guild.getTimeCreated().toInstant();

        embed.setTitle("Guild info");
        embed.setColor(Color.GREEN);
        embed.setThumbnail(guild.getIconUrl());
        embed.addField("Guilds name:", guild.getName(), true);
        embed.addField("Member count:", Integer.toString(guild.getMemberCount()), true);
        embed.addField("Emote count:", Long.toString(guild.getEmoteCache().size()), true);
        embed.addField("Category count:", Long.toString(guild.getCategoryCache().size()), true);
        embed.addField("Channel count:", Integer.toString(guild.getChannels().size()), true);
        embed.addField("Role count:", Long.toString(guild.getRoleCache().size()), true);
        embed.addField("Date created:", new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.ENGLISH).format(dateGuildCreated.toEpochMilli()), true);
        embed.addField("Guilds age:", Utils.getTimeDifference(Utils.getLocalTime(dateGuildCreated), LocalDateTime.now()), true);
        embed.setTimestamp(Instant.now());
        channel.sendMessage(embed.build()).queue();
    }
}
