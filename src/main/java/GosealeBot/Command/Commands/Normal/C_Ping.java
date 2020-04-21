package GosealeBot.Command.Commands.Normal;

import GosealeBot.Command.CommandContext;
import GosealeBot.Command.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.List;

public class C_Ping implements ICommand {

    private int hours;
    private int minutes;
    private int seconds;
    private int milisec;

    private int actualH;
    private int actualM;
    private int actualS;
    private int actualMS;

    private int msActual;
    private int msTime;

    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();
        JDA jda = ctx.getJDA();
        EmbedBuilder emb = new EmbedBuilder();
        emb.setColor(Color.yellow);
        emb.setTitle("PONG!");

        OffsetDateTime oMessageTime = ctx.getEvent().getMessage().getTimeCreated();

        channel.sendMessage(emb.build()).queue(
                (message) -> {

                    OffsetDateTime msg = message.getTimeCreated();

                    long delay = msg.getLong(ChronoField.MILLI_OF_DAY) - oMessageTime.getLong(ChronoField.MILLI_OF_DAY);

                    final int[] restPing = {0};
                    long gtwPing = 0;
                    jda.getRestPing().queue(
                            (ping) -> {
                                restPing[0] = Math.toIntExact(ping);}
                    );
                    gtwPing = jda.getGatewayPing();
                    emb.setColor(Color.cyan);
                    emb.setTitle("Ping results");
                    emb.setDescription("Gateway ping: "+gtwPing+"ms\n" +
                            "Discord ping: "+delay+"ms");
                    message.editMessage(emb.build()).queue();
                }
        );
    }

    @Override
    public String getName() {
        return "ping";
    }

    public String getHelp() {
        return "Shows the current ping to the discord servers";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("latency", "pong");
    }

    @Override
    public int getCooldown() {
        return 3000;
    }
}
