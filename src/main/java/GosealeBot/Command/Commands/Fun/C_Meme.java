package GosealeBot.Command.Commands.Fun;

import GosealeBot.Command.CommandContext;
import GosealeBot.Command.ICommand;
import GosealeBot.Util.GEmojiString;
import GosealeBot.Util.GReportErrorApi;
import com.fasterxml.jackson.databind.JsonNode;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class C_Meme implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        EmbedBuilder emb = new EmbedBuilder();
        if (!channel.isNSFW()) {
            emb.setColor(Color.orange);
            emb.setTitle("Protection");
            emb.setDescription("This channel needs to be set to NSFW as i can't moderate what could be shown after this command is ran");
            channel.sendMessage(emb.build()).queue();
            return;
        }


        emb.setColor(Color.yellow);
        emb.setTitle("Memeing around... "+ GEmojiString.EdownloadingFromWev);
        channel.sendMessage(emb.build()).queue(
                (m) -> {
                    WebUtils.ins.getJSONObject("https://apis.duncte123.me/meme?nsfw=false").async((json) -> {
                                if (!json.get("success").asBoolean()) {
                                    emb.setColor(Color.red);
                                    emb.setTitle("Something went wrong, try again later");
                                    new GReportErrorApi(ctx, ctx.getChannel(), ctx.getMember(), ctx.getMessage(), json.asText());
                                    m.editMessage(emb.build()).queue();
                                    System.out.println(json);
                                    return;
                                }

                                final JsonNode data = json.get("data");
                                final String title = data.get("title").asText();
                                final String url = data.get("url").asText();
                                final String image = data.get("image").asText();
                                emb.setTitle("Meme title: "+title, url);
                                emb.setColor(Color.cyan);
                                emb.setImage(image);

                                m.editMessage(emb.build()).queue();
                            }
                    );

                });
    }

    @Override
    public String getName() {
        return "meme";
    }

    @Override
    public String getHelp() {
        return "Gives you a fresh new meme";
    }

    @Override
    public int getCooldown() {
        return 10000;
    }

    @Override
    public String getCategory() {
        return "Fun";
    }
}
