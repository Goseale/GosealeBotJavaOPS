package GosealeBot.Command.Commands.Normal;

import GosealeBot.Command.CommandContext;
import GosealeBot.Command.ICommand;
import GosealeBot.Util.GEmbedMissingArgs;
import GosealeBot.Util.GReportError;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import org.mariuszgromada.math.mxparser.Expression;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class C_Math implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();
        List<String> args = ctx.getArgs();
        EmbedBuilder emb = new EmbedBuilder();
        if (args.isEmpty()) {
            new GEmbedMissingArgs(ctx,channel,getName());
            return;
        }
        final String expression = String.join(" ", args.subList(0, args.size()));
        Expression exp = new Expression(expression);
        emb.setColor(Color.cyan);
        emb.setTitle("Result");
        try {
            emb.setDescription("```"+exp.calculate()+"```");
            channel.sendMessage(emb.build()).queue();
        } catch (Exception e) {
            emb.setDescription("There was an error while processing this calculation");
            new GReportError(ctx,channel,ctx.getMember(),ctx.getMessage(),e);
            channel.sendMessage(emb.build()).queue();
        }

    }

    @Override
    public String getName() {
        return "math";
    }

    @Override
    public String getHelp() {
        return "Returns the answer to a given math expression";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList(new String[]{"calculate,calc"});
    }
}
