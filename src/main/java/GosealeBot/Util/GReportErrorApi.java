package GosealeBot.Util;

import GosealeBot.Command.CommandContext;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class GReportErrorApi {
    public GReportErrorApi(CommandContext ctx, TextChannel channel, Member member, Message m, String e) {
        TextChannel infoChannel = ctx.getJDA().getGuildById("348649372449243137").getTextChannelById("357596301522632715");
        EmbedBuilder emb = new EmbedBuilder();
        emb.setColor(Color.BLUE);
        emb.setTitle("Error report2");
        emb.setDescription("An error happened when "+member.getUser().getName() + " run a command");
        emb.addField("Message sent", "`"+m.getContentRaw()+"`\n\n"+m.getJumpUrl(),true);
        emb.addField("Member[Nickname]", member.getUser().getName()+"["+member.getNickname()+"]\n"+member.getAsMention(),true);
        emb.addField("Channel-Guild", channel.getName()+"-"+m.getGuild().getName(),true);
        String perms = "";
        for (Permission permission : ctx.getSelfMember().getPermissions()) {
            perms = perms + permission.getName()+"\n";
        }
        emb.addField("My perms", perms,true);
        emb.addField("The error", "```"+e+"```",true);
        infoChannel.sendMessage(emb.build()).queue();
    }
}
