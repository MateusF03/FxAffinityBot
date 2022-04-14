package me.mateus.fxaffinity.entities;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class SubmissionEntity implements FurEntity {

    private final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("uuuu-MM-dd[ ]['T']HH:mm:ss[X]");

    private final String author;
    private final String name;
    private final String postDate;
    private final String fullUrl;
    private final String originalLink;
    private final String rating;

    public SubmissionEntity(String author, String name,String postDate, String fullUrl, String originalLink, String rating) {

        this.author = author;
        this.name = name;
        this.postDate = postDate;
        this.fullUrl = fullUrl;
        this.originalLink = originalLink;
        this.rating = rating;

    }

    @Override
    public MessageEmbed getEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.GREEN);
        embedBuilder.setAuthor(name, originalLink);
        embedBuilder.addField("Author", author, false);
        embedBuilder.setImage(fullUrl);
        LocalDateTime date = LocalDateTime.parse(postDate, DATE_FORMAT);
        embedBuilder.setTimestamp(date.toInstant(ZoneOffset.UTC));

        return embedBuilder.build();
    }

    @Override
    public boolean isNSFW() {
        return !Objects.equals(rating, "General");
    }
}
