package GosealeBot.Command.Commands.Fun;

import GosealeBot.Command.CommandContext;
import GosealeBot.Command.ICommand;
import GosealeBot.Util.GEmbedMissingArgs;
import GosealeBot.Util.GReportError;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class C_8Ball implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final String[] responses = {
                // Affirmative
                "It is certain.",
                "It is decidedly so.",
                "Without a doubt.",
                "Yes, definitely.",
                "You may rely on it.",
                "You can count on it.",
                "As I see it, yes.",
                "Most likely.",
                "Outlook good.",
                "Yes.",
                "Signs point to yes.",
                "Absolutely.",
                "No wonder",
                "Of curse!",
                "Yes, yes, YES!!",

                // Non-commital
                "Reply hazy, try again.",
                "Ask again later.",
                "Better not tell you now.",
                "Cannot predict now.",
                "Concentrate and ask again.",
                "Hummmm i don't have it clear",

                // Negative
                "Don't count on it.",
                "My reply is no.",
                "My sources say no,",
                "Outlook not so good.",
                "Very doubtful.",
                "I don't think so",
                "Chances aren't good.",
                "As in your should no",
                "Evaled chances are 0/1",
                "no......"
        };
        final List<String> args = ctx.getArgs();
        final String argsJ = String.join(" ", args.subList(0, args.size()));
        final Member member = ctx.getMember();
        final TextChannel channel = ctx.getChannel();
        final EmbedBuilder emb = new EmbedBuilder();
        if (args.isEmpty() || !argsJ.contains("?")) {
            new GEmbedMissingArgs(ctx, channel, this.getName());
            return;
        }
        Random rnd = new Random();
        int numberChosen = rnd.nextInt(responses.length);
        int wait = 1 + rnd.nextInt(2);
        emb.setColor(0x7822a1);
        emb.setTitle(member.getUser().getName() + " asked the mighty 8ball");
        emb.setDescription(":8ball: Looking intro the future...");
        channel.sendMessage(emb.build()).delay(Duration.ofSeconds(wait)).queue(
                (m) -> {
                    emb.setDescription(":8ball:");
                    emb.addField("Question", argsJ, true);
                    emb.addField("Answer:", responses[numberChosen], true);
                    m.editMessage(emb.build()).queue(
                            (__) -> {},
                            (__) -> {new GReportError(ctx, ctx.getChannel(), ctx.getMember(), ctx.getMessage(), (Exception) __);}
                    );
                }

        );
    }

    @Override
    public String getName() {
        return "8ball";
    }

    @Override
    public String getHelp() {
        return "Ask a yes or no question to the 8ball (Proper questions have ?)";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList(":8ball:");
    }

    @Override
    public int getCooldown() {
        return 5000;
    }

    @Override
    public String getCategory() {
        return "Fun";
    }
}
