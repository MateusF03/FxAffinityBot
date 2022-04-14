package me.mateus.fxaffinity.listener;

import me.mateus.fxaffinity.converter.FurURLConverter;
import me.mateus.fxaffinity.entities.FurEntity;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class MessageListener extends ListenerAdapter {

    private final FurURLConverter urlConverter = new FurURLConverter();

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        User author = event.getAuthor();
        if (author.isBot() || !event.isFromGuild())
            return;
        String content = event.getMessage().getContentRaw();
        Matcher matcher = FurURLConverter.SUBMISSION_URL_PATTERN.matcher(content);
        if (!matcher.find())
            return;
        Guild guild = event.getGuild();
        Member selfMember = guild.getSelfMember();
        Member authorMember = guild.getMember(author);
        if (authorMember == null)
            return;
        Message message = event.getMessage();
        List<MessageEmbed> embeds = createEmbeds(content, event.getTextChannel());
        if (embeds.isEmpty())
            return;
        if (!selfMember.canInteract(authorMember)) {
            message.replyEmbeds(embeds).queue();
        } else {
            message.delete().queue((s) -> event.getChannel().sendMessageEmbeds(embeds).queue());
        }
    }

    private List<MessageEmbed> createEmbeds(String content, TextChannel textChannel) {
        List<MessageEmbed> embeds = new ArrayList<>();
        List<FurEntity> entities = urlConverter.convertContent(content);

        for (FurEntity entity : entities) {
            if (!textChannel.isNSFW() && entity.isNSFW()) {
                continue;
            }
            embeds.add(entity.getEmbed());
        }
        return embeds;
    }
}
