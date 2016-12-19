package com.mcmoddev.bot.command;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.mcmoddev.bot.util.CurseData;
import com.mcmoddev.bot.util.DiscordUtilities;

import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;

public class CommandCurse implements Command {

    // TOOD implement this
    private static final Map<String, CurseData> CACHE = new HashMap<>();

    @Override
    public void processCommand (IMessage message, String[] params) {

        if (params.length >= 2) {

            if (params[1].contains("@")) {

                DiscordUtilities.sendMessage(message.getChannel(), "This command uses Curse names, not Discord ones!");
                return;
            }

            IMessage msg = DiscordUtilities.sendMessage(message.getChannel(), "Getting stats for " + params[1] + ", this will take a bit.");

            final CurseData data = new CurseData(params[1]);
            final StringBuilder builder = new StringBuilder();
            final EmbedBuilder embed = new EmbedBuilder();

            if (!data.exists()) {

                DiscordUtilities.sendMessage(message.getChannel(), "No user could be found by the name " + params[1]);
                return;
            }

            else if (!data.hasProjects()) {

                DiscordUtilities.sendMessage(message.getChannel(), "No projects found for " + params[1]);
                return;
            }

            int addedProjects = 0;
            long otherDLs = 0;

            for (final Entry<String, Long> set : data.getDownloads().entrySet()) {

                if (addedProjects >= 10) {

                    otherDLs += set.getValue();
                    continue;
                }

                builder.append(set.getKey() + " - " + NumberFormat.getInstance().format(set.getValue()) + DiscordUtilities.SEPERATOR);
                addedProjects++;
            }

            if (addedProjects < data.getProjectCount())
                builder.append("Other Projects (" + (data.getProjectCount() - addedProjects) + ") - " + NumberFormat.getInstance().format(otherDLs) + DiscordUtilities.SEPERATOR);

            builder.append("Total Projects: " + data.getProjectCount() + DiscordUtilities.SEPERATOR);

            embed.ignoreNullEmptyFields();
            embed.withDesc(builder.toString());
            embed.withColor((int) (Math.random() * 0x1000000));
            embed.withFooterText("Powered by Jared");
            embed.withTitle("Total Downloads: " + NumberFormat.getInstance().format(data.getTotalDownloads()));

            if (data.hasAvatar())
                embed.withThumbnail(data.getAvatar());

            if(msg != null)
                DiscordUtilities.editMessage(msg, "Stats for " + params[1], embed.build());
            else
                DiscordUtilities.sendMessage(message.getChannel(), "Stats for " + params[1], embed.build());
        }

        else
            DiscordUtilities.sendMessage(message.getChannel(), "You must specify the name of a user as well!");
    }

    @Override
    public String getDescription () {

        return "Gets the total number of downloads for a user on Curse. Stats are powered by Jared. This may take a few moments as we make him count them by hand.";
    }
}