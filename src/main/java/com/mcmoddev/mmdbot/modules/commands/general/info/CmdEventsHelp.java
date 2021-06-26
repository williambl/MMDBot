package com.mcmoddev.mmdbot.modules.commands.general.info;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.mcmoddev.mmdbot.core.Utils;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.Color;
import java.time.Instant;

/**
 * The type Cmd events help.
 *
 * @author
 */
public final class CmdEventsHelp extends Command {

    /**
     * The constant IMAGE_URL.
     */
    private static final String IMAGE_URL =
        "https://cdn.discordapp.com/attachments/665281306426474506/665605979798372392/eventhandler.png";

    /**
     * Instantiates a new Cmd events help.
     */
    public CmdEventsHelp() {
        super();
        name = "events";
        aliases = new String[]{"eventshelp", "why-doesnt-my-event-handler-work"};
        help = "Gives info on how to use forge event handlers.";
    }

    /**
     * Execute.
     *
     * @param event The {@link CommandEvent CommandEvent} that triggered this Command.
     */
    @Override
    protected void execute(final CommandEvent event) {
        if (!Utils.checkCommand(this, event)) {
            return;
        }
        final var embed = new EmbedBuilder();
        final var channel = event.getTextChannel();

        embed.setTitle("Why doesn't my event handler work?");
        embed.setImage(IMAGE_URL);
        embed.setColor(Color.GREEN);
        embed.setTimestamp(Instant.now());

        channel.sendMessageEmbeds(embed.build()).queue();
    }
}
