package me.mateus.fxaffinity.entities;

import net.dv8tion.jda.api.entities.MessageEmbed;

public interface FurEntity {

    MessageEmbed getEmbed();
    boolean isNSFW();
}
