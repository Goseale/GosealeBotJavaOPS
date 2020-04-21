package GosealeBot.Command.Commands.Fun;

import GosealeBot.Command.CommandContext;
import GosealeBot.Command.ICommand;
import GosealeBot.Util.GEmojiString;
import GosealeBot.Util.GReportError;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class C_Joke2 implements ICommand {
    @Override
    public void handle(CommandContext ctx) {

        final TextChannel channel = ctx.getChannel();
        final EmbedBuilder emb = new EmbedBuilder();
        emb.setColor(Color.yellow);
        emb.setTitle("Joking around... " + GEmojiString.EdownloadingFromWev);
        channel.sendMessage(emb.build()).queue(
                (m) -> {
                    try {
                        WebUtils.ins.getJSONObject("https://official-joke-api.appspot.com/jokes/random").async((json) -> {


                            final String setup = json.get("setup").asText();
                            final String type = json.get("type").asText();
                            final String id = json.get("id").asText();
                            final String punchline = json.get("punchline").asText();

                            emb.setTitle(setup);
                            emb.setDescription(punchline);
                            emb.addField("Category", type, true);
                            emb.addField("Id", id, true);
                            emb.setColor(Color.cyan);

                            m.editMessage(emb.build()).queue();
                        });
                    } catch (Exception e) {
                        emb.setColor(Color.red);
                        emb.setTitle("Oh uh, something went wrong, try again later");
                        new GReportError(ctx, ctx.getChannel(), ctx.getMember(), ctx.getMessage(), e);
                        m.editMessage(emb.build());
                    }

                }
        );

    }

    @Override
    public String getName() {
        return "joke";
    }

    @Override
    public String getHelp() {
        return "Wanna laugh? Run this command";
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
