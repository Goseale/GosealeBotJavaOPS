package GosealeBot.Command.Commands.Moderation;

import GosealeBot.Command.CommandContext;
import GosealeBot.Command.ICommand;
import GosealeBot.Configuration;
import GosealeBot.Util.GEmbedMissingArgs;
import GosealeBot.Util.GEmbedTitle;
import GosealeBot.Util.GReportError;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.awt.*;
import java.util.List;

public class C_BulkMove implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        List<String> args = ctx.getArgs();
        TextChannel channel = ctx.getChannel();
        Member author = ctx.getMember();
        Guild guild = ctx.getGuild();
        EmbedBuilder emb = new EmbedBuilder();

        if (args.isEmpty() || args.size() < 2) {
            new GEmbedMissingArgs(ctx,channel,getName());
            return;
        }

        if (!ctx.getSelfMember().hasPermission(Permission.VOICE_MOVE_OTHERS)) {
            new GEmbedTitle(ctx,channel,"Im missing the permission to move users", Color.red);
            return;
        }

        if (!author.hasPermission(Permission.VOICE_MOVE_OTHERS) && !author.getUser().getId().equals(Configuration.get("owner_id"))) {
            new GEmbedTitle(ctx,channel,"You are missing the permission to move users", Color.red);
            return;
        }

        VoiceChannel origin;
        try {
            origin = guild.getVoiceChannelById(args.get(0));
        } catch (Exception e) {
            emb.setColor(Color.red);
            emb.setTitle("Argument " + args.get(0) + " is an invalid voice channel");
            channel.sendMessage(emb.build()).queue();
            return;
        }

        VoiceChannel destination;
        try {
            destination = guild.getVoiceChannelById(args.get(1));
        } catch (Exception e) {
            emb.setColor(Color.red);
            emb.setTitle("Argument " + args.get(1) + " is an invalid voice channel");
            channel.sendMessage(emb.build()).queue();
            return;
        }
        int count = origin.getMembers().size();
        for (Member member : origin.getMembers()) {
            guild.moveVoiceMember(member,destination).queue(
                    (__) -> {},
                    (error) -> {
                        new GReportError(ctx, channel, author, ctx.getMessage(), (Exception) error);
                    }
            );
        }
        emb.setColor(Color.GREEN);
        emb.setTitle("Moved `"+count+ "` users from `"+origin.getName() + "` to `"+ destination.getName()+"`");
        emb.setDescription("");
        channel.sendMessage(emb.build()).queue();

    }

    @Override
    public String getName() {
        return "bulk_move";
    }

    @Override
    public String getHelp() {
        return "Moves all users present in a voice channel to another one\n" +
                "Usage: `" + Configuration.get("prefix") + getName() + " [ID of origin channel] [ID of destination]`";
    }

    @Override
    public int getCooldown() {
        return 10000;
    }

    @Override
    public String getCategory() {
        return "Moderation";
    }
}
