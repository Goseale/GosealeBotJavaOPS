package GosealeBot.Command.Commands.Normal;

import GosealeBot.Command.CommandContext;
import GosealeBot.Command.ICommand;
import GosealeBot.CommandManager;
import GosealeBot.Configuration;
import net.dv8tion.jda.api.EmbedBuilder;
import org.menudocs.paste.PasteClient;
import org.menudocs.paste.PasteClientBuilder;
import org.menudocs.paste.PasteHost;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class C_helpfile implements ICommand {

    private final CommandManager manager;

    public C_helpfile(CommandManager manager) {
        this.manager = manager;
    }


    @Override
    public void handle(CommandContext ctx) {
        PasteClient client = new PasteClientBuilder()
                .setUserAgent("GosealeBot / Goseale#6992")
                .setDefaultExpiry("15s")
                .setPasteHost(PasteHost.MENUDOCS) // Optional
                .build();
        EmbedBuilder emb = new EmbedBuilder();
        emb.setColor(Color.BLUE);
        emb.setTitle("Generating link...");
        ctx.getChannel().sendMessage(emb.build()).queue(
                (m) -> {
                    StringBuilder builder = new StringBuilder();
                    builder.append("List of commands\n");
                    final String[] cmd = new String[1];
                    manager.getCommands().stream().map(ICommand::getName).forEach(
                            (it) -> {
                                cmd[0] = it;
                                if (manager.getCommand(cmd[0]).getCategory().equals("Owner")) {
                                    return;
                                }
                                builder
                                        .append("Command:[")
                                        .append(Configuration.get("prefix"))
                                        .append(it)
                                        .append("] - Cooldown:[")
                                        .append(manager.getCommand(it).getCooldown());

                                builder.append(" ms] - Description:[");
                                if (manager.getCommand(cmd[0]).getCategory().equals("Owner")) {
                                    builder.append("Description is hidden");
                                } else {
                                    builder.append(manager.getCommand(cmd[0])
                                            .getHelp());
                                }

                                builder.append("]\n\n");
                            });
                    String pasteID = client.createPaste("text", builder.toString()).execute();
                    String pasteUrl = client.getPasteUrl(pasteID);
                    emb.setColor(Color.GREEN);
                    emb.setTitle("Generated");
                    emb.setDescription("Click this link to see the file\n" + pasteUrl + "\nThe link is valid for 15 seconds");
                    m.editMessage(emb.build()).queue(
                            (m3) -> {
                                emb.setTitle("Timeout");
                                emb.setDescription("**Puf**\nThe paste is gone");
                                emb.setColor(Color.orange);
                                m3.editMessage(emb.build()).queueAfter(15, TimeUnit.SECONDS);
                            }
                    );
                }
        );

    }


    @Override
    public String getName() {
        return "helpfile";
    }

    @Override
    public String getHelp() {
        return "Gives the link to a file with all commands + descriptions of this bot";
    }

    @Override
    public int getCooldown() {
        return 30000;
    }
}
