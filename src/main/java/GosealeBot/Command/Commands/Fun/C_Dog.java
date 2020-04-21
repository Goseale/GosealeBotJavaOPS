package GosealeBot.Command.Commands.Fun;

import GosealeBot.Command.CommandContext;
import GosealeBot.Command.ICommand;
import GosealeBot.Util.GEmojiString;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

public class C_Dog implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        EmbedBuilder emb = new EmbedBuilder();
        emb.setColor(Color.yellow);
        emb.setTitle(GEmojiString.EdownloadingFromWev+":dog:");
        ctx.getChannel().sendMessage(emb.build()).queue(
                message -> {
                    WebUtils.ins.getJSONObject("https://random.dog/woof.json").async( (json) -> {
                        String url = json.get("url").asText();
                        emb.setImage(url);
                        emb.setTitle("Dog");
                        emb.setColor(Color.GREEN);
                        message.editMessage(emb.build()).queue();
                    });
                }
        );

    }

    @Override
    public String getName() {
        return "dog";
    }

    @Override
    public String getHelp() {
        return "Dogs!";
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
