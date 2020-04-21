package GosealeBot.Command.Commands.Moderation;

import GosealeBot.Command.CommandContext;
import GosealeBot.Command.ICommand;
import GosealeBot.Configuration;
import GosealeBot.Util.GEmbedMissingArgs;
import GosealeBot.Util.GReportError;
import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import java.awt.*;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class C_CopyMsgs implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        List<String> args = ctx.getArgs();
        TextChannel channel = ctx.getChannel();
        Guild guild = ctx.getGuild();
        EmbedBuilder emb = new EmbedBuilder();

        if (args.isEmpty() || args.size() < 3) {
            new GEmbedMissingArgs(ctx, channel, getName());
            return;
        }

        TextChannel target;
        try {
            target = guild.getTextChannelById(args.get(0));
        } catch (Exception e) {
            emb.setColor(Color.red);
            emb.setTitle("Argument " + args.get(0) + " is an invalid channel");
            channel.sendMessage(emb.build()).queue();
            return;
        }

        TextChannel destination;
        try {
            destination = guild.getTextChannelById(args.get(1));
        } catch (Exception e) {
            emb.setColor(Color.red);
            emb.setTitle("Argument " + args.get(1) + " is an invalid channel");
            channel.sendMessage(emb.build()).queue();
            return;
        }

        int amount;
        try {
            amount = Integer.parseInt(args.get(2));
        } catch (Exception e) {
            emb.setColor(Color.red);
            emb.setTitle("Argument " + args.get(2) + " is an invalid number");
            channel.sendMessage(emb.build()).queue();
            return;
        }

        if (!ctx.getSelfMember().hasPermission(Permission.MANAGE_WEBHOOKS)) {
            emb.setColor(Color.red);
            emb.setTitle("I lack the permission to create webhooks");
            emb.setDescription("Manage_webhooks");
            channel.sendMessage(emb.build()).queue();
            return;
        }

        new Thread(() -> {
        final Webhook[] webhook = new Webhook[1];
        destination.createWebhook("Copying messages from "+target.getName() +" to "+ destination.getName()).queue(
                webhooks -> {
                    webhook[0] = webhooks;

                    WebhookClientBuilder builder = new WebhookClientBuilder(webhook[0].getUrl()); // or id, token
                    builder.setThreadFactory((job) -> {
                        Thread thread = new Thread(job);
                        thread.setName("Creation of webhook");
                        thread.setDaemon(true);
                        return thread;
                    });
                    builder.setWait(true);

                    WebhookClient client = builder.build();


                    target.getIterableHistory()
                            .takeAsync(amount)
                            .thenApplyAsync((messages) -> {
                                List<Message> goodMessages = messages.stream()
                                        .filter((m) -> m.getTimeCreated().isBefore(
                                                OffsetDateTime.now().plus(2, ChronoUnit.WEEKS)
                                        ))
                                        .collect(Collectors.toList());
                                Collections.reverse(goodMessages);
                                final int[] msgsRemaining = {goodMessages.size()};
                                final Message[] messageInfo = new Message[1];

                                for (Message tmessage : goodMessages) {
                                    msgsRemaining[0] -= 1;
                                    WebhookMessageBuilder msg = new WebhookMessageBuilder();
                                    msg.setUsername(tmessage.getAuthor().getAsTag()); // use this username
                                    msg.setAvatarUrl(tmessage.getAuthor().getAvatarUrl()); // use this avatar
                                    String send = tmessage.getContentRaw();
                                    if (tmessage.getContentRaw().length() == 0) {
                                        WebhookMessageBuilder embBuilder = new WebhookMessageBuilder();
                                        List<MessageEmbed> embed = tmessage.getEmbeds();
                                        if (!embed.isEmpty()) {
                                            for (MessageEmbed messageEmbed : embed) {
                                                WebhookEmbedBuilder msgE = new WebhookEmbedBuilder();
                                                try {
                                                    msgE.setDescription(messageEmbed.getDescription());
                                                } catch (Exception e) {
                                                }
                                                try {
                                                    msgE.setColor(messageEmbed.getColor().getRGB());
                                                } catch (Exception e) {
                                                }
                                                try {
                                                    msgE.setTitle(new WebhookEmbed.EmbedTitle(messageEmbed.getTitle(), ""));
                                                } catch (Exception e) {
                                                }
                                                try {
                                                    msgE.setTimestamp(messageEmbed.getTimestamp());
                                                } catch (Exception e) {
                                                }
                                                try {
                                                    msgE.setFooter(new WebhookEmbed.EmbedFooter(messageEmbed.getFooter().getText(), messageEmbed.getFooter().getIconUrl()));
                                                } catch (Exception e) {
                                                    try {
                                                        msgE.setFooter(new WebhookEmbed.EmbedFooter(messageEmbed.getFooter().getText(), ""));
                                                    } catch (Exception e2) {
                                                    }
                                                }
                                                try {
                                                    msgE.setAuthor(new WebhookEmbed.EmbedAuthor(messageEmbed.getAuthor().getName(), messageEmbed.getAuthor().getIconUrl(), ""));
                                                } catch (Exception e) {
                                                    try {
                                                        msgE.setAuthor(new WebhookEmbed.EmbedAuthor(messageEmbed.getAuthor().getName(), "", ""));
                                                    } catch (Exception e2) {
                                                    }
                                                }
                                                try {
                                                    msgE.setImageUrl(messageEmbed.getImage().getUrl());
                                                } catch (Exception e) {
                                                }
                                                try {
                                                    msgE.setThumbnailUrl(messageEmbed.getThumbnail().getUrl());
                                                } catch (Exception e) {
                                                }
                                                try {
                                                    for (MessageEmbed.Field field : messageEmbed.getFields()) {
                                                        msgE.addField(new WebhookEmbed.EmbedField(field.isInline(), field.getName(), field.getValue()));
                                                    }
                                                } catch (Exception e) {
                                                }
                                                WebhookEmbed msgEs = msgE.build();

                                                embBuilder.addEmbeds(msgEs);
                                                msg.addEmbeds(msgEs);

                                            }
                                        } else {
                                            String attachmentURLS = "";
                                            for (Message.Attachment attachment : tmessage.getAttachments()) {
                                                attachmentURLS = attachmentURLS + attachment.getUrl() + "\n";
                                            }
                                            msg.setContent("**Message has attachments**\n" + attachmentURLS);
                                        }
                                    } else {
                                        msg.setContent(send);
                                    }
                                    EmbedBuilder trgt = new EmbedBuilder();
                                    trgt.setColor(Color.orange);
                                    trgt.setTitle("Copying messages... " + msgsRemaining[0] + " remaining");
                                    trgt.setDescription("This process is slow because of discord rate-limit");
                                    final boolean[] isDone = {false};
                                    client.send(msg.build()).thenRun(
                                            () -> {
                                                isDone[0] = true;
                                            }
                                    );

                                    if ((msgsRemaining[0] % 3) < 1) {
                                        try {
                                            Thread.sleep(500);
                                        } catch (InterruptedException e) { }
                                        isDone[0] = true;
                                    }

                                    while (!isDone[0]) {
                                        try {
                                            Thread.sleep(10);
                                        } catch (InterruptedException e) {

                                        }
                                    }
                                    try {
                                        messageInfo[0].delete().queue(
                                                (___) -> {
                                                    destination.sendMessage(trgt.build()).queue(
                                                            messageQ -> {
                                                                messageInfo[0] = messageQ;
                                                            }
                                                    );
                                                },
                                                (____) -> {
                                                    destination.sendMessage(new EmbedBuilder().setColor(Color.red).setTitle("Information message not found, stopping copy").build());
                                                    return;
                                                }
                                        );
                                    } catch (Exception e) {
                                        destination.sendMessage(trgt.build()).queue(
                                                messageQ -> {
                                                    messageInfo[0] = messageQ;
                                                }
                                        );
                                    }
                                    if (msgsRemaining[0] == 0) {
                                        try {
                                            Thread.sleep(500);
                                        } catch (InterruptedException e) {

                                        }
                                        messageInfo[0].delete().queue();
                                    }
                                }
                                try { webhooks.delete().queue(); } catch (Exception e) { }


                                return goodMessages.size();
                            })
                            .whenCompleteAsync(
                                    (count, thr) -> {
                                        emb.setColor(Color.green);
                                        emb.setTitle("Copied " + count + " messages");
                                        channel.sendMessage(emb.build()).delay(Duration.ofSeconds(3)).queue(
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
                                emb.setColor(Color.red);
                                emb.setTitle("Error");
                                emb.setDescription("Error: " + thr.getMessage() + "\n" + cause);
                                new GReportError(ctx, ctx.getChannel(), ctx.getMember(), ctx.getMessage(), (Exception) thr);
                                channel.sendMessage(emb.build()).queue();
                                thr.printStackTrace();
                                try { webhooks.delete().queue(); } catch (Exception e) { }


                                return 0;
                            });


                },
                (__) -> {
                    return;
                }
        );
    }).start();
        // Using the builder.

    }

    @Override
    public String getName() {
        return "copy_messages";
    }

    @Override
    public String getHelp() {
        return "Copies messages from one channel to another in the same guild\n" +
                "Usage: `" + Configuration.get("prefix") + getName() +" [ChannelID Target] [ChannelID Destination] [amount of messages]`\n";
    }

    @Override
    public String getCategory() {
        return "Moderation";
    }
}
