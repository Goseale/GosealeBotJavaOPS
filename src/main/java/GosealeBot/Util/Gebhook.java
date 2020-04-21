package GosealeBot.Util;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;

public class Gebhook {
    WebhookClient client;
    public Gebhook() {

        WebhookClientBuilder builder = new WebhookClientBuilder("https://discordapp.com/api/webhooks/700472713583657030/3-FbmUZjhbshVjF5G-hsZzZV9Ree4GBHAuzP_C6TsAngheQNZX5uQkOQnZQ5hNkXcsha"); // or id, token
        builder.setThreadFactory((job) -> {
            Thread thread = new Thread(job);
            thread.setName("Creation of webhook");
            thread.setDaemon(true);
            return thread;
        });
        builder.setWait(true);

        WebhookClient client = builder.build();
        this.client = client;
    }

    public void sendInfo(WebhookEmbed emb) {
        WebhookMessageBuilder msg = new WebhookMessageBuilder();
        msg.setUsername("GosealeBot Webhook"); // use this username
        msg.setAvatarUrl("https://cdn.discordapp.com/avatars/338367109136646154/3c7c690ac890f20f68991827faebe65b.png"); // use this avatar
        msg.addEmbeds(emb);
        client.send(msg.build());
    }

}
