package com.mcmoddev.mmdbot.events;

import com.mcmoddev.mmdbot.MMDBot;
import com.mcmoddev.mmdbot.core.TaskScheduler;
import com.mcmoddev.mmdbot.core.Utils;
import net.dv8tion.jda.api.events.DisconnectEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ReconnectedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author
 *
 */
public final class MiscEvents extends ListenerAdapter {

    /**
     *
     */
    @Override
    public void onReady(final @NotNull ReadyEvent event) {
        MMDBot.LOGGER.info("Bot is online and ready.");
        TaskScheduler.init();
    }

    /**
     *
     */
    @Override
    public void onDisconnect(final @NotNull DisconnectEvent event) {
        MMDBot.LOGGER.warn("*** Connection to Discord terminated ***");
    }

    /**
     *
     */
    @Override
    public void onReconnected(final @NotNull ReconnectedEvent event) {
        Utils.sleepTimer();
        MMDBot.LOGGER.warn("*** Bot reconnected to Discord successfully. ***");
    }
}
