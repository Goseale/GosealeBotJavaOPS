package GosealeBot.Command.Commands.Normal;

import GosealeBot.Command.CommandContext;
import GosealeBot.Command.ICommand;
import GosealeBot.CommandManager;
import GosealeBot.Configuration;
import GosealeBot.Util.GEmbedMissingArgs;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class C_Timer implements ICommand {


    @Override
    public void handle(CommandContext ctx) {
        new Thread(() -> {
            final int[] numero = {0};
            int rate = 0;
            TextChannel channel = ctx.getChannel();
            EmbedBuilder emb = new EmbedBuilder();
            List<String> args = ctx.getArgs();
            if (args.size() < 2) {
                new GEmbedMissingArgs(ctx, channel, this.getName());
                return;
            }
            try {
                numero[0] += (Integer.parseInt(args.get(0)) * 60);
            } catch (Exception e) {
                emb.setColor(Color.red);
                emb.setTitle(args.get(0) + " its not a valid number");
                channel.sendMessage(emb.build()).queue();
                return;
            }

            try {
                numero[0] += Integer.parseInt(args.get(1));
            } catch (Exception e) {
                emb.setColor(Color.red);
                emb.setTitle(args.get(0) + " its not a valid number");
                channel.sendMessage(emb.build()).queue();
                return;
            }
            CommandManager.cooldown.add(ctx.getAuthor().getId() + getName());
            emb.setColor(Color.BLUE);
            emb.setTitle("Timer");
            emb.setDescription(getTime(numero[0]));

            rate = 2;
            numero[0] += rate;


            int finalRate = rate;
            channel.sendMessage(emb.build()).queue(
                    (m) -> {
                        Timer t = new Timer();
                        t.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                numero[0] -= finalRate;
                                if (numero[0] <= 0) {
                                    t.cancel();
                                    t.purge();
                                    emb.setColor(Color.green);
                                    emb.setDescription("Timer ended");
                                    CommandManager.cooldown.remove(ctx.getAuthor().getId() + getName());
                                    m.editMessage(emb.build()).queue();
                                    channel.sendMessage(ctx.getMember().getAsMention() + "Time is up!").queue();
                                    return;
                                }
                                emb.setDescription(getTime(numero[0]));
                                m.editMessage(emb.build()).queue(
                                        (__) -> {
                                        },
                                        (__) -> {
                                            t.cancel();
                                            t.purge();
                                            emb.setColor(Color.red);
                                            emb.setDescription("Timer message was deleted.\nTimer stopped");
                                            CommandManager.cooldown.remove(ctx.getAuthor().getId() + getName());
                                            channel.sendMessage(emb.build()).queue();
                                        }
                                );

                            }
                        }, 0, finalRate * 1000);
                    }
            );

        }).start();


    }

    public String getTime(int numero) {
        int minutes = (int) Math.floor(numero / 60);
        String helper = "0";
        int seconds = numero % 60;
        if (seconds > 9) {
            helper = "";
        }

        return minutes + ":" + helper + seconds;
    }

    @Override
    public String getName() {
        return "timer";
    }

    @Override
    public String getHelp() {
        return "Sets a timer and when the time is up, pings you\n" +
                "Usage: " + Configuration.get("prefix") + "timer [minutes] [seconds]\nBoth parameters are necessary";
    }
}
