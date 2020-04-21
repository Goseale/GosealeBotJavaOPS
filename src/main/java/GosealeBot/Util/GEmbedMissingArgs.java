package GosealeBot.Util;

import GosealeBot.Command.CommandContext;
import GosealeBot.Configuration;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class GEmbedMissingArgs {
    public GEmbedMissingArgs(CommandContext ctx, TextChannel channel, String cmd) {
        EmbedBuilder emb = new EmbedBuilder()
                .setColor(Color.red)
                .setTitle("Missing parameters")
                .setDescription("Check "+ Configuration.get("prefix")+"help "+cmd+" to know how to use this command");
        channel.sendMessage(emb.build()).queue();
    }
}
