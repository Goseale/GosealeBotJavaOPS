package GosealeBot.Util;

import GosealeBot.Configuration;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import org.discordbots.api.client.DiscordBotListAPI;

import java.awt.*;

public class TOP_GG {
    JDA jda;
    DiscordBotListAPI api;
    public TOP_GG(JDA jda) {
        DiscordBotListAPI api = new DiscordBotListAPI.Builder()
                .token(Configuration.get("TOPGG"))
                .botId(jda.getSelfUser().getId())
                .build();
        this.jda = jda;
        this.api = api;
    }

    public void postGuildCount() {
        int guilsImOn = jda.getGuilds().size();
        api.setStats(guilsImOn);
        System.out.println("=[Member count of "+guilsImOn+" posted to TOP.GG!]=");
    }

    public boolean hasVoted(String userID) {
        System.out.println("Checking "+userID);
        if(!Values.voted.contains(userID)) {
            String userId = userID; // ID of the user you're checking
            final boolean[] voted = {false};
            new Thread(() -> {
                api.hasVoted(userId).whenComplete((hasVoted, e) -> {
                    voted[0] = hasVoted;

                    if (voted[0]) {
                        Values.voted.add(userID);
                        System.out.println("The user has voted, added to the list");
                    }

                    System.out.println("Requested data and it returned " + voted[0]);

                });
            }).start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {

            }
            return voted[0];
        } else {
            System.out.println("User already voted, not bothering the API");
            return true;
        }
    }

    public void voteMSG(TextChannel channel) {
        EmbedBuilder emb = new EmbedBuilder();
        emb.setColor(Color.orange);
        emb.setTitle("Im sorry but this command is locked :closed_lock_with_key:");
        emb.setDescription("You can unlock it by running **"+Configuration.get("prefix")+"vote** and voting\n" +
                "If you voted and see this, wait some seconds and try again");
        channel.sendMessage(emb.build()).queue();
    }

}
