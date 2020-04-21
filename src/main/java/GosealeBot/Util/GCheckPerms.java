package GosealeBot.Util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class GCheckPerms {
    TextChannel channel;
    Member me;
    EmbedBuilder emb;
    public GCheckPerms(TextChannel channel, Member me) {
        this.channel = channel;
        this.me = me;
        EmbedBuilder emb = new EmbedBuilder()
                .setColor(Color.red)
                .setTitle("Missing permissions");
        this.emb = emb;
    }

    public boolean permsMusic() {
        if (!me.hasPermission(Permission.MESSAGE_ADD_REACTION, Permission.VOICE_SPEAK, Permission.VOICE_CONNECT)) {
            emb.setDescription("I need the folowing permissions to function with the music command:\n" +
                    "Add reactions to messages\n" +
                    "(Connect, speak) to the channel you are on\n" +
                    "(for better usage) Manage messages (its used for clearing reactions)");
            channel.sendMessage(emb.build()).queue();
            return false;
        }
        return true;
    }
}
