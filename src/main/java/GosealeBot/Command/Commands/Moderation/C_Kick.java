package GosealeBot.Command.Commands.Moderation;

import GosealeBot.Command.CommandContext;
import GosealeBot.Command.ICommand;
import GosealeBot.Configuration;
import GosealeBot.Util.GEmbedMissingArgs;
import GosealeBot.Util.GReportError;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class C_Kick implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final List<String> args = ctx.getArgs();
        final List<Member> mentions = ctx.getMessage().getMentionedMembers();
        final Message message = ctx.getMessage();
        final TextChannel channel = ctx.getChannel();
        final Member member = ctx.getMember();
        EmbedBuilder emb = new EmbedBuilder();
        if (args.isEmpty() || args.size() < 2 || mentions.isEmpty()) {
            new GEmbedMissingArgs(ctx, channel, this.getName());
            return;
        }
        if (!ctx.getSelfMember().hasPermission(Permission.KICK_MEMBERS)) {
            emb.setTitle("Error");
            emb.setColor(Color.red);
            emb.setDescription("Missing permissions.\nI need the kick members permission");
            ctx.getChannel().sendMessage(emb.build()).queue();
            return;
        }
        final Member target = mentions.get(0);

        if (!member.hasPermission(Permission.KICK_MEMBERS) && !member.getUser().getId().equals(Configuration.get("owner_id"))) {
            emb.setTitle("Error");
            emb.setDescription("You don't have the required permissions to perform this command");
            emb.setColor(Color.red);
            channel.sendMessage(emb.build()).queue();
        }
        final String reason = String.join(" ", args.subList(1, args.size()));
        try {
            ctx.getGuild().kick(target, reason).reason(member.getUser().getAsTag() + "has kicked this user for: " + reason).queue(
                    (__) -> {
                        emb.setColor(Color.GREEN);
                        emb.setTitle("Success");
                        emb.setDescription("The member was kicked");
                        emb.addField("Victim", target.getUser().getAsTag(), true);
                        emb.addField("Reason", reason, true);
                        emb.addField("Moderator", member.getUser().getAsTag(), true);
                        channel.sendMessage(emb.build()).queue();
                    }

            );
        } catch (Exception e) {
            emb.setTitle("Kick failed");
            emb.setColor(Color.red);
            emb.setDescription("This was the reason:\n" + e.getMessage());
            new GReportError(ctx, ctx.getChannel(), ctx.getMember(), ctx.getMessage(), e);
            channel.sendMessage(emb.build()).queue();
        }
    }

    @Override
    public String getName() {
        return "kick";
    }

    @Override
    public String getHelp() {
        return "Kick a user from the server\n" +
                "Usage " + Configuration.get("prefix") +getName()+ " @User Reason";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("i_don't_want_you_here");
    }

    @Override
    public String getCategory() {
        return "Moderation";
    }
}
