package GosealeBot.Command.Commands.Normal;

import GosealeBot.Command.CommandContext;
import GosealeBot.Command.ICommand;
import GosealeBot.Util.GMarkdown;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class C_Vote implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();
        EmbedBuilder emb = new EmbedBuilder();
        emb.setColor(Color.CYAN);
        emb.setTitle("Thank you");
        emb.addField("Link", new GMarkdown().getMarkdown("Vote link", "https://top.gg/bot/338367109136646154/vote"), true);
        channel.sendMessage(emb.build()).queue();
    }

    @Override
    public String getName() {
        return "vote";
    }

    @Override
    public String getHelp() {
        return "Help the bot be known and help in the development of new features\n" +
                "Also its FREE and will only take a couple of seconds";
    }

    @Override
    public String getCategory() {
        return "top.gg";
    }
}
