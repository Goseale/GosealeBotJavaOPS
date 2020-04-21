package GosealeBot.Events;

import GosealeBot.Bot;
import GosealeBot.Configuration;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.awt.*;
import java.io.File;
import java.time.Duration;

public class BotDMEvent extends ListenerAdapter {

    @Override
    public void onPrivateMessageReceived(@Nonnull PrivateMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }

        if (Bot.blkUsers.isBlockedDM(event.getAuthor().getId())) {
            EmbedBuilder embPerson = new EmbedBuilder();
            embPerson.setColor(Color.RED);
            embPerson.setTitle("Message **NOT** sent to bot developer!");
            embPerson.setDescription("You have been blocked from using this bot");
            event.getChannel().sendMessage(embPerson.build()).delay(Duration.ofSeconds(5)).queue(
                    (message) -> {
                        message.delete().queue();
                    }
            );
            return;
        }

        JDA jda = event.getJDA();
        TextChannel infoChannel = jda.getGuildById("348649372449243137").getTextChannelById("697115295847809094");
        EmbedBuilder embChannel = new EmbedBuilder();
        embChannel.setColor(Color.blue);
        embChannel.setTitle("New DM from "+event.getAuthor().getAsTag());
        try {
            embChannel.setThumbnail(event.getAuthor().getAvatarUrl());
        } catch (Exception e) {
            embChannel.setThumbnail(event.getAuthor().getDefaultAvatarUrl());
        }

        embChannel.setDescription(event.getMessage().getContentRaw());
        embChannel.addField("Author", event.getAuthor().getAsMention(),true);
        embChannel.setFooter("Answer command: [`"+ Configuration.get("prefix")+"dm_answer "+ event.getAuthor().getId()+" `]");
        if (!event.getMessage().getAttachments().isEmpty()) {
            for (Message.Attachment attachment : event.getMessage().getAttachments()) {
                try {
                    File file = attachment.downloadToFile().get();
                    infoChannel.sendMessage(embChannel.build()).addFile(file).queue();
                    file.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            infoChannel.sendMessage(embChannel.build()).queue();
        }


        EmbedBuilder embPerson = new EmbedBuilder();
        embPerson.setColor(Color.CYAN);
        embPerson.setTitle("Message sent to bot developer!");
        embPerson.setDescription("I will be your communication with him, just like a chat\n" +
                "**Warning:** Bad usage of this feature can lead to your account being blacklisted on the bot");
        event.getChannel().sendMessage(embPerson.build()).delay(Duration.ofSeconds(5)).queue(
                (message) -> {
                    message.delete().queue();
                }
        );
    }
}
