package com.mcmoddev.mmdbot.events;

import com.mcmoddev.mmdbot.core.Utils;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.mcmoddev.mmdbot.MMDBot.LOGGER;
import static com.mcmoddev.mmdbot.MMDBot.getConfig;
import static com.mcmoddev.mmdbot.utilities.console.MMDMarkers.REQUESTS;

/**
 * The type Event reaction added.
 *
 * @author
 */
public final class EventReactionAdded extends ListenerAdapter {

    /**
     * The Warned messages.
     */
    private final Set<Message> warnedMessages = new HashSet<>();

    /**
     * On message reaction add.
     *
     * @param event the event
     */
    @Override
    public void onMessageReactionAdd(final MessageReactionAddEvent event) {
        final var channel = event.getTextChannel();
        final MessageHistory history = MessageHistory.getHistoryAround(channel,
            event.getMessageId()).limit(1).complete();
        final var message = history.getMessageById(event.getMessageId());
        if (message == null) {
            return;
        }
        final double removalThreshold = getConfig().getRequestsRemovalThreshold();
        final double warningThreshold = getConfig().getRequestsWarningThreshold();
        if (removalThreshold == 0 || warningThreshold == 0) {
            return;
        }

        final var guild = event.getGuild();
        final var guildId = guild.getIdLong();
        final var discussionChannel = guild.getTextChannelById(getConfig()
            .getChannel("requests.discussion"));
        if (getConfig().getGuildID() == guildId && getConfig().getChannel("requests.main")
            == channel.getIdLong()) {

            final int freshnessDuration = getConfig().getRequestFreshnessDuration();
            if (freshnessDuration > 0) {
                final OffsetDateTime creationTime = message.getTimeCreated();
                final var now = OffsetDateTime.now();
                if (now.minusDays(freshnessDuration).isAfter(creationTime)) {
                    return; // Do nothing if the request has gone past the freshness duration
                }
            }

            final List<Long> badReactionsList = getConfig().getBadRequestsReactions();
            final List<Long> goodReactionsList = getConfig().getGoodRequestsReactions();
            final List<Long> needsImprovementReactionsList = getConfig().getRequestsNeedsImprovementReactions();
            final int badReactions = Utils.getNumberOfMatchingReactions(message, badReactionsList::contains);
            final int goodReactions = Utils.getNumberOfMatchingReactions(message, goodReactionsList::contains);
            final int needsImprovementReactions = Utils.getNumberOfMatchingReactions(message,
                needsImprovementReactionsList::contains);

            final double requestScore = (badReactions + needsImprovementReactions * 0.5) - goodReactions;

            final User messageAuthor = message.getAuthor();
            if (requestScore >= removalThreshold) {
                LOGGER.info(REQUESTS, "Removed request from {} due to score of {} reaching removal threshold {}",
                    messageAuthor, requestScore, removalThreshold);

                final Message response = new MessageBuilder().append(messageAuthor.getAsMention()).append(", ")
                    .append("your request has been found to be low quality by community review and has been removed.\n")
                    .append("Please see other requests for how to do it correctly.\n")
                    .appendFormat("It received %d 'bad' reactions, %d 'needs improvement' reactions, and %d "
                            + "'good' reactions.",
                        badReactions, needsImprovementReactions, goodReactions)
                    .build();

                warnedMessages.remove(message);

                final var logChannel = guild.getTextChannelById(getConfig()
                    .getChannel("events.requests_deletion"));
                if (logChannel != null) {
                    logChannel.sendMessage(String.format("Auto-deleted request from %s (%s;%s) due to "
                            + "reaching deletion threshold: %n%s", messageAuthor.getAsMention(),
                        messageAuthor.getAsTag(), messageAuthor.getId(), message.getContentRaw()))
                        .allowedMentions(Collections.emptySet())
                        .queue();
                }

                channel.deleteMessageById(event.getMessageId())
                    .reason(String.format(
                        "Bad request: %d bad reactions, %d needs improvement reactions, %d good reactions",
                        badReactions, needsImprovementReactions, goodReactions))
                    .flatMap(v -> {
                        RestAction<Message> action = messageAuthor.openPrivateChannel()
                            .flatMap(privateChannel -> privateChannel.sendMessage(response));
                        //If we can't DM the user, send it in the discussions channel instead.
                        if (discussionChannel != null) {
                            action = action.onErrorFlatMap(throwable -> discussionChannel.sendMessage(response));
                        }
                        return action;
                    })
                    .queue();

            } else if (!warnedMessages.contains(message) && requestScore >= warningThreshold) {
                LOGGER.info(REQUESTS, "Warned user {} due to their request (message id: {}) score of {} reaching "
                    + "warning threshold {}", messageAuthor, message.getId(), requestScore, warningThreshold);

                final Message response = new MessageBuilder()
                    .append(messageAuthor.getAsMention()).append(", ")
                    .append("your request is close to being removed by community review.\n")
                    .append("Please edit your message to bring it to a higher standard.\n")
                    .appendFormat("It has so far received %d 'bad' reactions, %d 'needs improvement' reactions, "
                            + "and %d 'good' reactions.",
                        badReactions, needsImprovementReactions, goodReactions)
                    .build();

                warnedMessages.add(message);

                RestAction<Message> action = messageAuthor.openPrivateChannel()
                    .flatMap(privateChannel -> privateChannel.sendMessage(response));
                //If we can't DM the user, send it in the discussions channel instead.
                if (discussionChannel != null) {
                    action = action.onErrorFlatMap(throwable -> discussionChannel.sendMessage(response));
                }
                action.queue();
            }
        }
    }
}
