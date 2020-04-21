package GosealeBot.Command.Commands.Normal;

import GosealeBot.Command.CommandContext;
import GosealeBot.Command.ICommand;
import GosealeBot.Util.GMarkdown;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class C_Invite implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();
        EmbedBuilder emb = new EmbedBuilder();
        emb.setColor(Color.GREEN);
        emb.setTitle("Click this link to invite me to your server");
        emb.addField("Link", new GMarkdown().getMarkdown("Invite link","https://discordapp.com/api/oauth2/authorize?client_id=338367109136646154&permissions=2117430390&scope=bot"), true);
        channel.sendMessage(emb.build()).queue();
    }

    @Override
    public String getName() {
        return "invite";
    }

    @Override
    public String getHelp() {
        return "Gives you the invite link to invite mw to your guild";
    }
}
