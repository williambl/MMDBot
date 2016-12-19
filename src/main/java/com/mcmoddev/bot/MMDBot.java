package com.mcmoddev.bot;

import com.mcmoddev.bot.handler.CommandHandler;
import com.mcmoddev.bot.database.DatabaseHandler;
import com.mcmoddev.bot.handler.EventHandler;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RateLimitException;

public class MMDBot {

    public static IDiscordClient INSTANCE;
    public static DatabaseHandler DATABASE;

    public static void main (String... args) throws RateLimitException {

        try {

            INSTANCE = new ClientBuilder().withToken(args[0]).login();
            INSTANCE.getDispatcher().registerListener(new EventHandler());
            initHandlers();
        }

        catch (final DiscordException e) {

            e.printStackTrace();
        }
    }

    public static void initHandlers () {

        if(DATABASE != null){
            DATABASE.disconnect();
        }
        DATABASE = new DatabaseHandler();
        DATABASE.connect();

        CommandHandler.initCommands();
    }
}