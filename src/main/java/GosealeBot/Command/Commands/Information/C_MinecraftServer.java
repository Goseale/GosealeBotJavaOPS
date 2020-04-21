package GosealeBot.Command.Commands.Information;

import GosealeBot.Command.CommandContext;
import GosealeBot.Command.ICommand;
import GosealeBot.Configuration;
import GosealeBot.Util.GEmbedMissingArgs;
import GosealeBot.Util.GEmojiString;
import com.fasterxml.jackson.databind.JsonNode;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class C_MinecraftServer implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();
        List<String> args = ctx.getArgs();

        if (args.isEmpty()) {
            new GEmbedMissingArgs(ctx, channel, this.getName());
        }

        EmbedBuilder emb = new EmbedBuilder();
        emb.setColor(Color.yellow);
        emb.setTitle(GEmojiString.EdownloadingFromWev + "Getting info...");
        channel.sendMessage(emb.build()).queue(
                message -> {
                    WebUtils.ins.getJSONObject("https://mcapi.us/server/status?ip="+args.get(0)).async((json) -> {
                        if (!json.get("status").asText().equalsIgnoreCase("success")) {
                            emb.setColor(Color.red);
                            emb.setTitle("Error");
                            emb.setDescription("The provided ip is not valid");
                            message.editMessage(emb.build()).queue();
                            return;
                        }

                        if (json.get("online").asText().equals("true")) {
                            emb.addField("Online",":white_check_mark:",true);
                        } else {
                            emb.addField("Online",":x:",true);
                        }



                        emb.addField("MOTD","```"+json.get("motd").asText()+"\u200B```",false);
                        JsonNode json1 = json.get("players");
                        emb.addField("Players","```"+json1.get("now")+"/"+json1.get("max")+"```",true);
                        JsonNode json2 = json.get("server");
                        emb.addField("Version","```"+json2.get("name")+"```",true);
                        try {
                            long lastOnline = Long.parseLong(json.get("last_online").asText());
                            emb.addField("Last online","```"+convertToTime(lastOnline)+"```",true);
                        } catch (Exception e){
                            emb.addField("Last online","```"+"Missing data"+"```",true);
                        }
                        try {
                            long lastUpdated = Long.parseLong(json.get("last_updated").asText());
                            emb.addField("Last updated","```"+convertToTime(lastUpdated)+"```",true);
                        } catch (Exception e) {
                            emb.addField("Last online","```"+"Missing data"+"```",true);
                        }

                        emb.setTitle("Information about: "+args.get(0));
                        emb.setColor(Color.GREEN);
                        message.editMessage(emb.build()).queue();
                    });
                }
        );
    }

    @Override
    public String getName() {
        return "minecraft_server";
    }

    @Override
    public String getHelp() {
        return "Returns some information of minecraft server\n"+
                "Usage "+ Configuration.get("prefix")+getName()+" [ip]";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("mc_server", "mcserver");
    }

    @Override
    public int getCooldown() {
        return 30000;
    }

    @Override
    public String getCategory() {
        return "Information";
    }

    public String convertToTime(long time) {
        long ms = time;
        long seconds = (long) Math.floor(ms / 1000);
        ms = ms % 1000;
        long minutes = (long) Math.floor(seconds / 60);
        long hours = (long) Math.floor(minutes / 60);
        long days = (long) (Math.floor(hours) / 60);
        String helper = "0";
        String helper2 = "0";
        seconds = seconds % 60;
        if (seconds > 9) {
            helper = "";
        }
        minutes = minutes % 60;
        if (minutes > 9) {
            helper2 = "";
        }
        hours = hours % 24;
        return hours+":"+helper2+minutes+":"+helper+seconds;
    }
}
