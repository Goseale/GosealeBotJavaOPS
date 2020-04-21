package GosealeBot.Command.Commands.Moderation;

import GosealeBot.Command.CommandContext;
import GosealeBot.Command.ICommand;
import GosealeBot.Configuration;
import GosealeBot.Util.GEmbedTitle;
import GosealeBot.Util.GMarkdown;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ClientType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.time.OffsetDateTime;
import java.util.List;

public class C_UserInfo implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();
        List<Member> mentions = ctx.getMessage().getMentionedMembers();
        List<String> args = ctx.getArgs();
        Member member;
        if (args.isEmpty()) {
            member = ctx.getMember();
        } else {
            if (!mentions.isEmpty()) {
                member = mentions.get(0);
            } else {
                try {
                    member = ctx.getGuild().getMemberById(args.get(0));
                } catch (Exception e) {
                    new GEmbedTitle(ctx,channel,"Member could not be found in this server", Color.red);
                    return;
                }

            }
        }





        EmbedBuilder emb = new EmbedBuilder();
        emb.setTitle("Info about " + member.getUser().getAsTag());

        StringBuilder builder = new StringBuilder();
        builder.append("Joined discord: " + parseDateAndTime(member.getUser().getTimeCreated()) + "\n");
        builder.append("Id: " + member.getUser().getId() + "\n");
        builder.append("Bot user: " + (member.getUser().isBot() ? ":white_check_mark:" : ":x:") + "\n");
        builder.append("Fake user: " + (member.getUser().isFake() ? ":white_check_mark:" : ":x:") + "\n");


        String statuses = "Active clients: ";
        for (ClientType activeClient : member.getActiveClients()) {

            switch(activeClient.name().toLowerCase()) {
                case "desktop":
                    statuses = statuses + ":desktop: ";
                    break;
                case "mobile":
                    statuses = statuses + ":iphone: ";
                    break;
                case "web":
                    statuses = statuses + ":globe_with_meridians: ";
                    break;
                default:
                    statuses = statuses + activeClient.name() +" ";
            }

        }

        builder.append(statuses+"\n");

        String status = "";
        if (member.getOnlineStatus().equals(OnlineStatus.ONLINE)) {
            status = ":green_square:";
        }
        if (member.getOnlineStatus().equals(OnlineStatus.DO_NOT_DISTURB)) {
            status = ":red_square:";
        }
        if (member.getOnlineStatus().equals(OnlineStatus.IDLE)) {
            status = ":yellow_square:";
        }
        if (member.getOnlineStatus().equals(OnlineStatus.INVISIBLE)) {
            status = ":white_square_button:";
        }
        if (member.getOnlineStatus().equals(OnlineStatus.OFFLINE)) {
            status = ":black_large_square:";
        }
        if (member.getOnlineStatus().equals(OnlineStatus.UNKNOWN)) {
            status = ":question:";
        }

        builder.append("Status: " + status);

        emb.addField("Discord info", builder.toString(), true);

        builder = new StringBuilder();
        builder.append("Joined the guild: " + parseDateAndTime(member.getTimeJoined()) + "\n");
        if (member.getNickname() != null) {
            builder.append("Nickname: " + member.getNickname() + "\n");
        }

        String permissions = "";
        int count=0;
        for (Permission permission : member.getPermissions()) {
            if (count%2 == 0) {
                permissions += "`"+permission.getName()+"` -";
            } else {
                permissions += "`"+permission.getName()+"`\n";
            }
            count++;
        }
        builder.append("Permissions: `("+count+")`\n"+permissions);

        emb.addField("Guild info", builder.toString(), true);
        String roles = "";
        for (Role role : member.getRoles()) {
            roles = roles + role.getAsMention() + "\n";
        }

        emb.addField("Guild roles", roles, false);

        emb.setColor(member.getColor());
        try {
          emb.setImage(member.getUser().getAvatarUrl());
          emb.addField("Profile picture Url:", new GMarkdown().getMarkdown("Link", member.getUser().getAvatarUrl()),true);
        } catch (Exception e) {}

        channel.sendMessage(emb.build()).queue();
    }

    public String parseDateAndTime(OffsetDateTime time) {
        String helper = "";
        if (time.getMinute() < 10) {
            helper = "0";
        }
        return time.getDayOfMonth() + "/" + time.getMonthValue() + "/" + time.getYear() + " At: " +time.getHour()+":"+helper + time.getMinute();
    }

    @Override
    public String getName() {
        return "userinfo";
    }

    @Override
    public String getHelp() {
        return "Returns information about that user" +
                "Usage: " + Configuration.get("prefix") + getName() + "[@User or User´s ID] if the param is omited it will target the member who runs it";
    }

    @Override
    public String getCategory() {
        return "Moderation";
    }
}
