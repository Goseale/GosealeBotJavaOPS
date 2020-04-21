package GosealeBot.Command.Commands.Information;

import GosealeBot.Command.CommandContext;
import GosealeBot.Command.ICommand;
import GosealeBot.Util.GEmojiString;
import com.fasterxml.jackson.databind.JsonNode;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class C_GoogleTrends implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        EmbedBuilder emb = new EmbedBuilder();
        emb.setColor(Color.yellow);
        emb.setTitle(GEmojiString.EdownloadingFromWev + "Getting info...");
        ctx.getChannel().sendMessage(emb.build()).queue(
                message -> {
                    WebUtils.ins.getJSONObject("https://trends.google.com/trends/hottrends/visualize/internal/data").async((json) -> {
                        List<String> selectedCountry = Arrays.asList("argentina", "portugal", "mexico", "brazil", "united_states", "colombia", "new zealand", "canada", "australia", "netherlands", "chile", "sweden");
                        for (String country : selectedCountry) {
                            StringBuilder builder = new StringBuilder();
                            int count = 0;
                            for (JsonNode jsonNode : json.withArray(country)) {
                                if (count >= 5) {
                                    break;
                                }
                                builder.append("-").append(jsonNode.asText()).append("\n");
                                count++;

                            }
                            emb.addField(country, builder.toString(), true);
                        }

                        System.out.println();
                        emb.setTitle("Google trend searches");
                        emb.setColor(Color.GREEN);
                        message.editMessage(emb.build()).queue();
                    });
                }
        );
    }

    @Override
    public String getName() {
        return "google_trends";
    }

    @Override
    public String getHelp() {
        return "Returns the most searched terms on Google";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("google_searches");
    }

    @Override
    public int getCooldown() {
        return 60000;
    }

    @Override
    public String getCategory() {
        return "Information";
    }
}
