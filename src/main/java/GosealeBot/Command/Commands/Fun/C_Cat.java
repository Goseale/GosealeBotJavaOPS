package GosealeBot.Command.Commands.Fun;

import GosealeBot.Command.CommandContext;
import GosealeBot.Command.ICommand;
import GosealeBot.Util.GEmojiString;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

public class C_Cat implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        EmbedBuilder emb = new EmbedBuilder();
        emb.setColor(Color.yellow);
        emb.setTitle(GEmojiString.EdownloadingFromWev +":cat:");
        ctx.getChannel().sendMessage(emb.build()).queue(
                message -> {
                    WebUtils.ins.scrapeWebPage("https://api.thecatapi.com/api/images/get?format=xml&results_per_page=1").async( (document) -> {
                        String url = document.getElementsByTag("url").first().html();
                        emb.setImage(url);
                        emb.setTitle("Cat");
                        emb.setColor(Color.GREEN);
                        message.editMessage(emb.build()).queue();
                    });
                }
        );

    }

    @Override
    public String getName() {
        return "cat";
    }

    @Override
    public String getHelp() {
        return "Cats!";
    }

    @Override
    public int getCooldown() {
        return 15000;
    }

    @Override
    public String getCategory() {
        return "Fun";
    }
}
