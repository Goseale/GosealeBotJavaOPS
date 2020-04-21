package GosealeBot.Util;

import GosealeBot.Command.CommandContext;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class GEmbedTitle {
    public GEmbedTitle(CommandContext ctx, TextChannel channel, String message, Color color) {
        EmbedBuilder emb = new EmbedBuilder()
                .setColor(color)
                .setTitle(message);
        channel.sendMessage(emb.build()).queue();
    }
}
