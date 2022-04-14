package me.mateus.fxaffinity;

import me.mateus.fxaffinity.listener.MessageListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Scanner;

public class Bot {

    public static void main(String[] args) {
        File tokenFile = new File("token.txt");
        if (!tokenFile.exists()) {
            System.out.println("Put your bot token in token.txt");
            try {
                if (!tokenFile.createNewFile()) {
                    System.err.println("Error while creating token.txt");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        byte[] b;
        try {
            b = Files.readAllBytes(tokenFile.toPath());
            String token = new String(b, StandardCharsets.UTF_8);

            JDABuilder jdaBuilder = JDABuilder.createDefault(token);
            jdaBuilder.addEventListeners(new MessageListener());
            JDA jda = jdaBuilder.build();
            Thread thread = new Thread(() -> {
                Scanner scanner = new Scanner(System.in);
                while (scanner.hasNext()) {
                    String command = scanner.next();
                    if (command.equalsIgnoreCase("shutdown")) {
                        jda.shutdown();
                        System.exit(0);
                    }
                }
            });
            thread.start();
        } catch (IOException | LoginException e) {
            e.printStackTrace();
        }
    }
}
