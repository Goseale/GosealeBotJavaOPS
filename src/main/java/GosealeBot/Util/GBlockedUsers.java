package GosealeBot.Util;

import GosealeBot.Command.CommandContext;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class GBlockedUsers {
    private List<String> blocked = new LinkedList<>(Arrays.asList(new String[]{"696053879435034725"}));
    CommandContext ctx;
    public GBlockedUsers() {
    }

    public boolean isBlocked(CommandContext ctx,boolean silent) {
        this.ctx = ctx;
        for (String b : blocked) {
            if (ctx.getAuthor().getId().equals(b)) {
                if (!silent) {
                    EmbedBuilder emb = new EmbedBuilder();
                    emb.setColor(Color.red);
                    emb.setTitle("Not allowed");
                    emb.setDescription("You have been blocked from using this bot");
                    ctx.getChannel().sendMessage(emb.build()).queue();
                }
                return true;
            }
        }
        return false;
    }

    public boolean isBlockedDM(String id) {
        for (String b : blocked) {
            if (id.equals(b)) {
                return true;
            }
        }
        return false;
    }

    public void addUser(String id) {
        blocked.add(id);
    }

    public void removeUser(String id) {
        blocked.remove(id);
    }

    public List<String> getBlocked() {
        return blocked;
    }
}
