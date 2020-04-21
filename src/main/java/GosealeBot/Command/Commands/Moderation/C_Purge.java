package GosealeBot.Command.Commands.Moderation;

import GosealeBot.Command.CommandContext;
import GosealeBot.Command.ICommand;
import GosealeBot.Configuration;
import GosealeBot.Util.GEmbedMissingArgs;
import GosealeBot.Util.GEmbedTitle;
import GosealeBot.Util.GReportError;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class C_Purge implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        GuildMessageReceivedEvent event = ctx.getEvent();
        TextChannel channel = event.getChannel();
        Member member = event.getMember();
        Member selfMember = event.getGuild().getSelfMember();
        List<String> args = ctx.getArgs();
        EmbedBuilder emb = new EmbedBuilder();

        if (!member.hasPermission(Permission.MESSAGE_MANAGE) && !member.getUser().getId().equals(Configuration.get("owner_id"))) {
            new GEmbedTitle(ctx, channel, "You need the `Manage Messages` permission to use this command", Color.red);

            return;
        }

        if (!selfMember.hasPermission(Permission.MESSAGE_MANAGE)) {
            new GEmbedTitle(ctx, channel, "I need the `Manage Messages` permission for this command", Color.red);

            return;
        }

        if (args.isEmpty()) {
            new GEmbedMissingArgs(ctx, channel, this.getName());
            return;
        }

        int amount;
        String arg = args.get(0);

        try {
            amount = Integer.parseInt(arg);
        } catch (NumberFormatException ignored) {
            new GEmbedTitle(ctx, channel, arg + " is not a valid number", Color.red);
            return;
        }


            if (amount < 1 || amount > 300) {
                new GEmbedTitle(ctx, channel, "Amount must be at least 1 to 300", Color.red);
                return;
            }

        channel.getIterableHistory()
                .takeAsync(amount)
                .thenApplyAsync((messages) -> {
                    List<Message> goodMessages = messages.stream()
                            .filter((m) -> m.getTimeCreated().isBefore(
                                    OffsetDateTime.now().plus(2, ChronoUnit.WEEKS)
                            ))
                            .collect(Collectors.toList());
                    channel.purgeMessages(goodMessages);

                    return goodMessages.size();
                })
                .whenCompleteAsync(
                        (count, thr) -> {
                            emb.setColor(Color.green);
                            emb.setTitle("Deleted "+count +" messages");
                            channel.sendMessage(emb.build()).delay(Duration.ofSeconds(3)).queue(
                                    (m2) -> {
                                        m2.delete().queue();
                                    }
                            );
                        }
                )
                .exceptionally((thr) -> {
                    String cause = "";

                    if (thr.getCause() != null) {
                        cause = "Caused by: " + thr.getCause().getMessage();
                    }
                    emb.setColor(Color.red);
                    emb.setTitle("Error");
                    emb.setDescription("Error: " + thr.getMessage()+"\n"+cause);
                    new GReportError(ctx, ctx.getChannel(), ctx.getMember(), ctx.getMessage(), (Exception) thr);
                    channel.sendMessage(emb.build()).queue();

                    return 0;
                });
    }

    @Override
    public String getName() {
        return "purge";
    }

    @Override
    public String getHelp() {
        return "Bulk delete messages in a text channel\n" +
                "Usage: " + Configuration.get("prefix") +getName()+ " [amount]";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("clear","bulk","bulkdelete");
    }

    @Override
    public int getCooldown() {
        return 5000;
    }

    @Override
    public String getCategory() {
        return "Moderation";
    }
}
