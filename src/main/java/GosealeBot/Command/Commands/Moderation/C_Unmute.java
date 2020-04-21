package GosealeBot.Command.Commands.Moderation;

import GosealeBot.Bot;
import GosealeBot.Command.CommandContext;
import GosealeBot.Command.ICommand;
import GosealeBot.Configuration;
import GosealeBot.Util.GEmbedMissingArgs;
import GosealeBot.Util.GReportError;
import GosealeBot.Util.Values;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

import java.awt.*;
import java.util.List;

public class C_Unmute implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final List<String> args = ctx.getArgs();
        final List<Member> mentions = ctx.getMessage().getMentionedMembers();
        final Message message = ctx.getMessage();
        final TextChannel channel = ctx.getChannel();
        final Member member = ctx.getMember();
        Guild guild = ctx.getGuild();
        EmbedBuilder emb = new EmbedBuilder();
        if (args.isEmpty() || args.size() < 2 || mentions.isEmpty()) {
            new GEmbedMissingArgs(ctx, channel, this.getName());
            return;
        }
        if (!ctx.getSelfMember().hasPermission(Permission.MANAGE_CHANNEL)) {
            emb.setTitle("Error");
            emb.setColor(Color.red);
            emb.setDescription("Missing permissions.\nI need the manage channel permission");
            ctx.getChannel().sendMessage(emb.build()).queue();
            return;
        }

        final Member target = mentions.get(0);
        String VicTag = target.getUser().getAsTag();
        String MemTag = member.getUser().getAsTag();

        if (!member.hasPermission(Permission.MANAGE_CHANNEL) && !member.getUser().getId().equals(Configuration.get("owner_id"))) {
            emb.setTitle("Error");
            emb.setDescription("You don't have the required permissions to perform this command");
            emb.setColor(Color.red);
            channel.sendMessage(emb.build()).queue();
            return;
        }
        final String reason = String.join(" ", args.subList(1, args.size()));
        try {
            List<Role> role = ctx.getEvent().getGuild().getRolesByName(Values.mutedRoleName, false);
            if (role != null || !role.isEmpty()) {
                if (target.getRoles().contains(role.get(0)) || Bot.helper.getList().contains(target.getId())) {
                    try {
                        guild.removeRoleFromMember(target, role.get(0)).queue();
                    } catch (Exception e) {

                    }
                    emb.setColor(Color.green);
                    emb.setTitle("User unmuted");
                    emb.addField("Muted user", VicTag, true);
                    emb.addField("Moderator", MemTag, true);
                    emb.addField("Reason", reason, true);
                    Bot.helper.removeUser(target.getId());
                    channel.sendMessage(emb.build()).queue();
                } else {
                    emb.setColor(Color.red);
                    emb.setTitle("The specified user is not muted by this bot");
                    channel.sendMessage(emb.build()).queue();
                }
            }
        } catch (Exception e) {
            emb.setColor(Color.red);
            emb.setTitle("An error was found");
            new GReportError(ctx, ctx.getChannel(), ctx.getMember(), ctx.getMessage(), e);
            emb.setDescription("Error:\n" + e.getMessage());
            channel.sendMessage(emb.build()).queue();
        }
    }

    @Override
    public String getName() {
        return "unmute";
    }

    @Override
    public String getHelp() {
        return "unmutes a user muted by this bot\n" +
                "Usage: " + Configuration.get("prefix") +getName()+ " @user reason";
    }

    @Override
    public String getCategory() {
        return "Moderation";
    }
}
