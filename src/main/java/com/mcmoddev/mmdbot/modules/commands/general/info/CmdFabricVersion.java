package com.mcmoddev.mmdbot.modules.commands.general.info;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.mcmoddev.mmdbot.core.Utils;
import com.mcmoddev.mmdbot.utilities.updatenotifiers.fabric.FabricVersionHelper;
import com.mcmoddev.mmdbot.utilities.updatenotifiers.minecraft.MinecraftVersionHelper;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.Color;
import java.time.Instant;

/**
 * The type Cmd fabric version.
 *
 * @author williambl
 */
public final class CmdFabricVersion extends Command {

    /**
     * Instantiates a new Cmd fabric version.
     */
    public CmdFabricVersion() {
        super();
        this.name = "fabric";
        aliases = new String[]{"fabricv", "yarn"};
        help = "Get the latest Fabric versions";
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

        String mcVersion = event.getArgs().trim();
        if (mcVersion.isEmpty()) {
            mcVersion = MinecraftVersionHelper.getLatest();
        }

        String yarnVersion = FabricVersionHelper.getLatestYarn(mcVersion);
        if (yarnVersion == null) {
            yarnVersion = "None";
        }
        final var embed = new EmbedBuilder();
        final var channel = event.getTextChannel();

        embed.setTitle("Fabric Versions for Minecraft " + mcVersion);
        embed.addField("Latest Yarn", yarnVersion, true);
        embed.addField("Latest API", FabricVersionHelper.getLatestApi(), true);
        embed.addField("Latest Loader", FabricVersionHelper.getLatestLoader(), true);
        embed.setColor(Color.WHITE);
        embed.setTimestamp(Instant.now());
        channel.sendMessageEmbeds(embed.build()).queue();
    }
}
