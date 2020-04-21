package GosealeBot;

import GosealeBot.Command.CommandContext;
import GosealeBot.Command.Commands.Fun.*;
import GosealeBot.Command.Commands.Information.C_BotStatus;
import GosealeBot.Command.Commands.Information.C_GoogleTrends;
import GosealeBot.Command.Commands.Information.C_MinecraftServer;
import GosealeBot.Command.Commands.Information.C_Search;
import GosealeBot.Command.Commands.Moderation.*;
import GosealeBot.Command.Commands.Music.C_Music;
import GosealeBot.Command.Commands.Normal.*;
import GosealeBot.Command.Commands.Owner.*;
import GosealeBot.Command.ICommand;
import GosealeBot.Util.GCommandLogger;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.annotation.Nullable;
import java.awt.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CommandManager {
    private final List<ICommand> commands = new ArrayList<>();

    public static List<String> cooldown = new ArrayList<>();

    public CommandManager(EventWaiter waiter) {
        addCommand(new C_Ping());
        addCommand(new C_Help(this));
        addCommand(new C_Kick());
        addCommand(new C_Mute());
        addCommand(new C_Unmute());
        addCommand(new C_helpfile(this));
        addCommand(new C_Meme());
        addCommand(new C_Joke2());
        addCommand(new C_Joke());
        addCommand(new C_8Ball());
        addCommand(new C_Shutdown());
        addCommand(new C_Music(this,waiter));
        addCommand(new C_Uptime());
        addCommand(new C_Eval());
        addCommand(new C_Purge());
        addCommand(new C_UserPurge());
        addCommand(new C_MatchPurge());
        addCommand(new C_Timer());
        addCommand(new C_Cat());
        addCommand(new C_Dog());
        addCommand(new C_GoogleTrends());
        addCommand(new C_MinecraftServer());
        addCommand(new C_EmbedCreator(waiter));
        addCommand(new C_UserInfo());
        addCommand(new C_Random());
        addCommand(new C_WebsiteScreenshot());
        addCommand(new C_Restart());
        addCommand(new C_Logs());
        addCommand(new C_DManswer());
        addCommand(new C_Math());
        addCommand(new C_ServerList(this));
        addCommand(new C_BotStatus());
        addCommand(new C_Search());
        addCommand(new C_Vote());
        addCommand(new C_Invite());
        addCommand(new C_CopyMsgs());
        addCommand(new C_GosePurge());
        addCommand(new C_SendHook());
        addCommand(new C_BulkMove());
        addCommand(new C_RoleInfo());
        addCommand(new C_Quote());
        addCommand(new C_AddBlockedUser());
        addCommand(new C_RemoveBlockedUser());


    }

    private void addCommand(ICommand cmd) {
        boolean nameFound = this.commands.stream().anyMatch((it) -> it.getName().equalsIgnoreCase(cmd.getName()));

        if (nameFound) {
            throw new IllegalArgumentException("A command with this name is already present");
        }

        commands.add(cmd);
    }

    public List<ICommand> getCommands() {
        return commands;
    }

    @Nullable
    public ICommand getCommand(String search) {
        String searchLower = search.toLowerCase();

        for (ICommand cmd : this.commands) {
            if (cmd.getName().equals(searchLower) || cmd.getAliases().contains(searchLower)) {
                return cmd;
            }
        }

        return null;
    }

    void handle(GuildMessageReceivedEvent event) {

        if (Bot.helper.getList().contains(event.getAuthor().getId())) {
            return;
        }


        String[] split = event.getMessage().getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(Configuration.get("prefix")), "")
                .split("\\s+");

        String invoke = split[0].toLowerCase();
        ICommand cmd = this.getCommand(invoke);
        new GCommandLogger(event);
        if (cmd != null) {

            if (cooldown.contains(event.getAuthor().getId() + invoke)) {
                if (event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_MANAGE)) {
                    event.getMessage().delete().queue();
                }
                EmbedBuilder emb = new EmbedBuilder();
                emb.setColor(Color.red);
                emb.setTitle(invoke + " is still on cooldown!");
                if (invoke.equalsIgnoreCase("timer")) {
                    emb.setDescription("Only one instance of timer is allowed per user");
                }
                event.getChannel().sendMessage(emb.build()).delay(Duration.ofSeconds(3)).queue(
                        (message -> {
                            message.delete().queue();
                        })
                );
                return;
            }
            if (cmd.getCooldown() > 0 && !cmd.getName().equalsIgnoreCase("timer")) {
                cooldown.add(event.getAuthor().getId() + invoke);
                setTimeout(() -> cooldown.remove(event.getAuthor().getId() + invoke), cmd.getCooldown());
            }
            event.getChannel().sendTyping().queue(
                    (__) -> {
                        List<String> args = Arrays.asList(split).subList(1, split.length);

                        CommandContext ctx = new CommandContext(event, args);

                        if (Bot.blkUsers.isBlocked(ctx,false)) {
                            return;
                        }

                        cmd.handle(ctx);
                    }
            );

        } else {

        }
    }

    public static void setTimeout(Runnable runnable, int delay){
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            }
            catch (Exception e){
                System.err.println(e);
            }
        }).start();
    }

    public static void addCooldown(CommandContext ctx, int timeInMS, String command) {
        cooldown.add(ctx.getAuthor().getId() + command);
        setTimeout(() -> cooldown.remove(ctx.getAuthor().getId() + command), timeInMS);
    }

}