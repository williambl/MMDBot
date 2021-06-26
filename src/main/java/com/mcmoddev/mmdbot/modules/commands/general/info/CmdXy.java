package com.mcmoddev.mmdbot.modules.commands.general.info;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.mcmoddev.mmdbot.core.Utils;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.Color;
import java.time.Instant;

/**
 * The type Cmd xy.
 *
 * @author
 */
public final class CmdXy extends Command {

    /**
     * The constant BODY.
     */
    private static final String BODY =
        "The XY problem is asking about your attempted solution rather than your actual problem."
            + "This leads to enormous amounts of wasted time and energy, both on the part of people asking for help,"
            + "and on the part of those providing help." + System.lineSeparator()
            + Utils.makeHyperlink("More Info", "http://xyproblem.info/");

    /**
     * Instantiates a new Cmd xy.
     */
    public CmdXy() {
        super();
        name = "xy";
        aliases = new String[]{"xy-problem", "xyproblem"};
        help = "Gives info on \"The XY Problem\"";
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

        embed.setTitle("The XY Problem, what is it?");
        embed.setDescription(BODY);
        embed.setColor(Color.ORANGE);
        embed.setTimestamp(Instant.now());

        channel.sendMessageEmbeds(embed.build()).queue();
    }
}
