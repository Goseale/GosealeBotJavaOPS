package GosealeBot;

import GosealeBot.Util.Gebhook;
import GosealeBot.Util.TOP_GG;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Listener extends ListenerAdapter {
    private CommandManager manager;
    public Listener(EventWaiter waiter) {
        final CommandManager manager = new CommandManager(waiter);
        this.manager = manager;
    }



    @Override
    public void onReady(ReadyEvent event) {
        System.out.println();
        System.out.println("-----Running start script-----");
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        Random rnd = new Random();
        int numberChosen = rnd.nextInt(10000);

        System.out.println("GosealeBot.Bot ready!");
        event.getJDA().getPresence().setStatus(OnlineStatus.ONLINE);
        event.getJDA().getPresence().setActivity(Activity.streaming("Prefix "+Configuration.get("prefix") +" | Rng:" + numberChosen, "https://twitch.tv/goseale"));
        TextChannel infoChannel = event.getJDA().getGuildById("348649372449243137").getTextChannelById("693512353643233340");
        TextChannel channel= infoChannel;
        int amount = 200;
        final EmbedBuilder emb2 = new EmbedBuilder();

        channel.getIterableHistory()
                .takeAsync(amount)
                .thenApplyAsync((messages) -> {
                    List<Message> goodMessages = messages.stream()
                            .filter((m) -> m.getTimeCreated().isBefore(
                                    OffsetDateTime.now().plus(2, ChronoUnit.WEEKS)
                            ))
                            .collect(Collectors.toList());
                    channel.purgeMessages(goodMessages);

                    return goodMessages.size();
                })
                .whenCompleteAsync(
                        (count, thr) -> {
                            emb2.setColor(Color.green);
                            emb2.setTitle("Deleted "+count +" messages");
                            channel.sendMessage(emb2.build()).delay(Duration.ofSeconds(3)).queue(
                                    (m2) -> {
                                        m2.delete().queue();
                                    }
                            );
                        }
                )
                .exceptionally((thr) -> {
                    String cause = "";

                    if (thr.getCause() != null) {
                        cause = "Caused by: " + thr.getCause().getMessage();
                    }
                    emb2.setColor(Color.red);
                    emb2.setTitle("Error");
                    emb2.setDescription("Error: " + thr.getMessage()+"\n"+cause);
                    channel.sendMessage(emb2.build()).queue();

                    return 0;
                });

        EmbedBuilder emb = new EmbedBuilder();
        emb.setColor(Color.GREEN);
        emb.setTitle("The bot has been started!");
        emb.addField("Guilds im on", event.getGuildTotalCount()+"", true);
        new TOP_GG(event.getJDA()).postGuildCount();
        emb.addField("Current time", date+"", true);
        long ping = event.getJDA().getRestPing().complete();
        if (ping > 500) {
            emb.addField("WARNING", "```Ping value is high: "+ping+" ms```",false);
        }
        infoChannel.getGuild().getEmotes().forEach( (e) -> {
            System.out.println(e.getAsMention() + " " +e.getName() + " " + e);
        });
        infoChannel.sendMessage(emb.build()).flatMap(m -> m.addReaction("😜")).queue(
                (__) -> {},
                (__) -> {
                }
        );
        System.out.println("-----Start script  ended-----");
        System.out.println("Logged in as: " + event.getJDA().getSelfUser().getAsTag());


    }






    @Override
    public void onShutdown(@Nonnull ShutdownEvent event) {
        event.getJDA().getPresence().setStatus(OnlineStatus.OFFLINE);
        TextChannel infoChannel = event.getJDA().getGuildById("348649372449243137").getTextChannelById("693512353643233340");
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        String prefix = Configuration.get("prefix");
        String raw = event.getMessage().getContentRaw();
        String[] msgArgs = raw.split("\\s+");
        User user = event.getAuthor();
        TextChannel msgChannel = event.getChannel();
        JDA jda = event.getJDA();
        TextChannel infoChannel = event.getJDA().getGuildById("348649372449243137").getTextChannelById("693512353643233340");

        if (user.isBot() || event.isWebhookMessage()) {
            return;
        }

        if (raw.startsWith(prefix)) {
            manager.handle(event);
        }

    }

    @Override
    public void onGuildJoin(@Nonnull GuildJoinEvent event) {
        WebhookEmbedBuilder web = new WebhookEmbedBuilder();
        web.setColor(Color.GREEN.getRGB());
        web.setTitle(new WebhookEmbed.EmbedTitle("I joined a new guild!", ""));
        web.addField(new WebhookEmbed.EmbedField(true, "Guild name", event.getGuild().getName()));
        web.addField(new WebhookEmbed.EmbedField(true, "Guild owner", event.getGuild().getOwner().getUser().getName()+"\n"+event.getGuild().getOwnerId()));
        web.addField(new WebhookEmbed.EmbedField(true, "Guild member count", event.getGuild().getMemberCount()+""));
        new Gebhook().sendInfo(web.build());
    }

    @Override
    public void onGuildLeave(@Nonnull GuildLeaveEvent event) {
        WebhookEmbedBuilder web = new WebhookEmbedBuilder();
        web.setColor(Color.RED.getRGB());
        web.setTitle(new WebhookEmbed.EmbedTitle("I left a guild!", ""));
        web.addField(new WebhookEmbed.EmbedField(true, "Guild name", event.getGuild().getName()));
        web.addField(new WebhookEmbed.EmbedField(true, "Guild owner", event.getGuild().getOwner().getUser().getName()+"\n"+event.getGuild().getOwnerId()));
        web.addField(new WebhookEmbed.EmbedField(true, "Guild member count", event.getGuild().getMemberCount()+""));
        new Gebhook().sendInfo(web.build());
    }

}
