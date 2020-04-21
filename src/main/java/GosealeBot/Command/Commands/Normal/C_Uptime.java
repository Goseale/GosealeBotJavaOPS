package GosealeBot.Command.Commands.Normal;

import GosealeBot.Command.CommandContext;
import GosealeBot.Command.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

public class C_Uptime implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        long time = 0;
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        EmbedBuilder emb = new EmbedBuilder();
        TextChannel channel = ctx.getChannel();
        time = runtime.getUptime();
        long ms = time;
        long seconds = ms / 1000;
        long minutes = (long) Math.floor(seconds / 60);
        long hours = (long) Math.floor(minutes / 60);
        String helper = "0";
        seconds %= 60;
        minutes %= 60;
        if (seconds > 9) {
            helper = "";
        }
        emb.setColor(Color.BLUE);
        emb.setTitle("Bot uptime");
        emb.addField("Hours", "```"+hours+"```", true);
        emb.addField("Minutes", "```"+minutes+"```", true);
        emb.addField("Seconds", "```"+seconds+"```", true);
        channel.sendMessage(emb.build()).queue();
    }

    @Override
    public String getName() {
        return "uptime";
    }

    @Override
    public String getHelp() {
        return "Shows how much time the bot was online";
    }
}
