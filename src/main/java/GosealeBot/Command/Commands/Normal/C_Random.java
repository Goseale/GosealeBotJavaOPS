package GosealeBot.Command.Commands.Normal;

import GosealeBot.Command.CommandContext;
import GosealeBot.Command.ICommand;
import GosealeBot.Configuration;
import GosealeBot.Util.GEmbedMissingArgs;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class C_Random implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();
        List<String> args = ctx.getArgs();
        EmbedBuilder emb = new EmbedBuilder();
        if (args.size() < 1) {
            new GEmbedMissingArgs(ctx, channel, getName());
            return;
        }

        try {
            int number = Integer.parseInt(args.get(0));
            if (number > 128) {
                number = 128;
                emb.setDescription("Number cramped at 128");
            }
            if (number < 1) {
                number = 1;
            }
            emb.setTitle("Generated");
            emb.addField("String", "```"+generateString(number)+"```", true);
            emb.addField("Number", "```"+generateNumber(number)+"```", true);
            emb.setColor(Color.cyan);
            channel.sendMessage(emb.build()).queue();
        } catch (Exception e) {
            emb.setColor(Color.red);
            emb.setTitle("Number " + args.get(0) + " is invalid");
            channel.sendMessage(emb.build()).queue();
        }
        return;


    }

    @Override
    public String getName() {
        return "random";
    }

    @Override
    public String getHelp() {
        return "Generates a random amount of number or text\n" +
                "Usage: " + Configuration.get("prefix") + getName() + " [number, text] [amount]";
    }

    private String generateString(int numberOfChars) {
        String generated = "";
        for (int i = 0; i < numberOfChars; i++) {
            Random random = new Random();
            int number;
            if (random.nextBoolean()) {
                number = 65 + random.nextInt(90 - 65);
            } else {
                number = 97 + random.nextInt(122 - 97);
            }
            generated = generated + (char) number + "";
        }
        return generated;
    }

    private String generateNumber(int numberOfChars) {
        String generated = "";
        for (int i = 0; i < numberOfChars; i++) {
            Random random = new Random();
            if (i == 0) {
                generated = generated + (1 + random.nextInt(8));
            } else {
                generated = generated + random.nextInt(9);
            }
        }
        return generated;
    }
}


