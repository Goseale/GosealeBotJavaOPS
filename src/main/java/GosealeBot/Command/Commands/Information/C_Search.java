package GosealeBot.Command.Commands.Information;

import GosealeBot.Command.CommandContext;
import GosealeBot.Command.ICommand;
import GosealeBot.Configuration;
import GosealeBot.Util.GEmbedMissingArgs;
import GosealeBot.Util.GEmojiString;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.util.List;

public class C_Search implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        EmbedBuilder emb = new EmbedBuilder();
        List<String> args = ctx.getArgs();
        if (args.isEmpty()) {
            new GEmbedMissingArgs(ctx, ctx.getChannel(), getName());
            return;
        }

        final String search = String.join(" ", args.subList(0, args.size()));
        final String CSEkey = Configuration.get("CSE_KEY");
        final String CSEengine = Configuration.get("CSE_WEBSEARCH_ID");

        emb.setColor(Color.yellow);
        emb.setTitle(GEmojiString.EdownloadingFromWev + " Searching...");
        ctx.getChannel().sendMessage(emb.build()).queue(
                message -> {
                    WebUtils.ins.getJSONObject("https://www.googleapis.com/customsearch/v1?key=" + CSEkey + "&cx=" + CSEengine + "&q=" + search).async((json) -> {
                        int resultCount = json.get("searchInformation").get("totalResults").asInt();
                        if (resultCount > 0) {
                            emb.setColor(Color.BLUE);
                            emb.setTitle(json.get("items").get(0).get("title").asText());
                            try {
                                emb.setDescription(json.get("items").get(0).get("snippet").asText());
                            } catch (Exception e) {

                            }
                            try {
                                emb.setImage(json.get("items").get(0).get("pagemap").get("cse_image").get(0).get("src").asText());
                            } catch (Exception e) {

                            }

                            emb.addField("Url", json.get("items").get(0).get("link").asText(), false);
                        } else {
                            emb.setTitle("No result was found for the query "+search);
                            emb.setColor(Color.red);

                        }
                        emb.setFooter("Found "+resultCount +" results for "+search);
                        message.editMessage(emb.build()).queue();
                    });
                }
        );

    }

    @Override
    public String getName() {
        return "search";
    }

    @Override
    public String getHelp() {
        return "Search something on google\n" +
                "Usage: " + Configuration.get("prefix") + getName() + " [search query]";
    }

    @Override
    public int getCooldown() {
        return 15000;
    }

    @Override
    public String getCategory() {
        return "Information";
    }
}
