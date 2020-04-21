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

public class C_Mute implements ICommand {
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
            Role ServerRole = null;
            final boolean[] serverHasRole = {false};
            for (Role role : ctx.getGuild().getRoles()) {
                if (role.getName().equalsIgnoreCase(Values.mutedRoleName)) {
                    ServerRole = role;
                    serverHasRole[0] = true;
                }
            }
            if (!serverHasRole[0]) {
                Role finalServerRole = ServerRole;
                guild.createRole()
                        .setColor(Color.BLACK)
                        .setName(Values.mutedRoleName)
                        .setPermissions()
                        .setMentionable(false)
                        .queue(
                                (role) -> {
                                    emb.setColor(Color.green);
                                    emb.setTitle("Created server role "+Values.mutedRoleName+" as it didnt exist");
                                    serverHasRole[0] = true;
                                    channel.sendMessage(emb.build()).queue();
                                    guild.addRoleToMember(target, role).queue(
                                            (__) -> {
                                                emb.setColor(Color.green);
                                                emb.setTitle("Member muted!");
                                                emb.addField("Victim", VicTag, true);
                                                emb.addField("Moderator", MemTag, true);
                                                emb.addField("Reason", reason, true);
                                                Bot.helper.addUser(target.getId());
                                                channel.sendMessage(emb.build()).queue();
                                            }
                                    );
                                },
                                (__) -> {
                                    emb.setColor(Color.green);
                                    emb.setTitle("Failed to create a "+ Values.mutedRoleName +" role, create it and run this command again");
                                    channel.sendMessage(emb.build());
                                    new GReportError(ctx, channel, member, ctx.getMessage(), (Exception) __);
                                }
                        );
            }
            if (!serverHasRole[0]) {return;}
            guild.addRoleToMember(target, ServerRole).queue(
                    (__) -> {
                        emb.setColor(Color.green);
                        emb.setTitle("Member muted!");
                        emb.addField("Victim", VicTag, true);
                        emb.addField("Moderator", MemTag, true);
                        emb.addField("Reason", reason, true);
                        Bot.helper.addUser(target.getId());
                        channel.sendMessage(emb.build()).queue();
                    }
            );
        } catch (Exception e) {
            emb.setColor(Color.red);
            emb.setTitle("An error was found");
            emb.setDescription("Error:\n" + e.getMessage());
            channel.sendMessage(emb.build()).queue();
        }
    }

    @Override
    public String getName() {
        return "mute";
    }

    @Override
    public String getHelp() {
        return "Mutes a user and prevents it from talking\n" +
                "Usage: " + Configuration.get("prefix") + getName() + " @user reason";
    }

    @Override
    public String getCategory() {
        return "Moderation";
    }
}
