package GosealeBot.Command.Commands.Moderation;

import GosealeBot.Command.CommandContext;
import GosealeBot.Command.ICommand;
import GosealeBot.Configuration;
import GosealeBot.Util.GEmbedMissingArgs;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.List;

public class C_RoleInfo implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        List<String> args = ctx.getArgs();
        TextChannel channel = ctx.getChannel();
        Guild guild = ctx.getGuild();
        EmbedBuilder emb = new EmbedBuilder();
        if (args.isEmpty() || args.size() < 1) {
            new GEmbedMissingArgs(ctx, channel, getName());
            return;
        }
        final String joined = String.join(" ", args.subList(0, args.size()));
        Role role = null;

        for (Role guildRole : guild.getRoles()) {
            if (guildRole.getName().toLowerCase().contains(joined.toLowerCase())) {
                role = guildRole;
            }
        }

        if (role == null) {
            emb.setColor(Color.red);
            emb.setTitle("Role not found");
            emb.setDescription("Could not find any role named: "+joined);
            channel.sendMessage(emb.build()).queue();
            return;
        }

        int usersWithRole = 0;
        for (Member guildMember : guild.getMembers()) {
            if (guildMember.getRoles().contains(role)) {
                usersWithRole++;
            }
        }

        emb.setTitle("Infomation about: " + role.getName());
        emb.setColor(role.getColor());
        String s = "";
        s+="ID:[`"+role.getId()+"`] ["+role.getAsMention()+"]\n\n";
        s+="**Info**\n";
        s+="Position: `"+role.getPosition()+"`/`"+guild.getRoles().size()+"`\n";
        s+="Users with role: `"+usersWithRole+"`\n";
        s+="Is Public? "+ (role.isPublicRole() ? "<:tickYes:315009125694177281>":"<:tickNo:315009174163685377>")+"\n";
        s+="Managed by 3rd party? "+ (role.isManaged() ? "<:tickYes:315009125694177281>":"<:tickNo:315009174163685377>")+"\n";
        s+="Can be mentioned? "+ (role.isMentionable() ? "<:tickYes:315009125694177281>":"<:tickNo:315009174163685377>")+"\n";
        s+="Is hoisted? "+ (role.isHoisted() ? "<:tickYes:315009125694177281>":"<:tickNo:315009174163685377>")+"\n";
        emb.addField("", s,false);
        String permissions = "";
        int count=0;
        for (Permission permission : role.getPermissions()) {
            if (count%2 == 0) {
                permissions += "`"+permission.getName()+"` -";
            } else {
                permissions += "`"+permission.getName()+"`\n";
            }
            count++;
        }
        emb.addField("Permissions", permissions, false);
        channel.sendMessage(emb.build()).queue();
    }

    @Override
    public String getName() {
        return "roleinfo";
    }

    @Override
    public String getHelp() {
        return "Returns information about a role on the server\n" +
                "Usage: `" + Configuration.get("prefix") + getName() + "[search]`";
    }

    @Override
    public String getCategory() {
        return "Moderation";
    }
}
