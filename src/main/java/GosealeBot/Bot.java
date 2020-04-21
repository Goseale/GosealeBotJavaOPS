package GosealeBot;

import GosealeBot.Command.Commands.Moderation.UserIsMuted;
import GosealeBot.Events.BotDMEvent;
import GosealeBot.Events.ErrorEvent;
import GosealeBot.Util.Database.database;
import GosealeBot.Util.GBlockedUsers;
import GosealeBot.Util._MuteHelper_;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.net.URISyntaxException;
import java.util.EventListener;

public class Bot implements EventListener {
    private static JDA jda;
    public static database db;
    public static GBlockedUsers blkUsers;
    public static _MuteHelper_ helper;

    public static void main(String[] args) throws LoginException, InterruptedException {
        EventWaiter waiter = new EventWaiter();
        blkUsers = new GBlockedUsers();
        helper = new _MuteHelper_();
        ChunkingFilter.include(348649372449243137L);
        ChunkingFilter chkF = ChunkingFilter.NONE;
        System.out.println("Starting and logging in...");
        try {
            db = new database();
        } catch (URISyntaxException e) {
            System.out.println("! Error while logging in to database !");
        }
        try {
                jda = new JDABuilder(AccountType.BOT)
                        .setToken(Configuration.get("token"))
                        .disableCache(CacheFlag.ACTIVITY, CacheFlag.MEMBER_OVERRIDES)
                        .setChunkingFilter(chkF)
                        .build();
                System.out.println("Logged in!");
            } catch (Exception e) {
                System.out.println("Error while logging in\n" + e.getMessage());
                System.exit(404);
            }

        System.out.println("Adding events...");
        jda.addEventListener(waiter);
        jda.addEventListener(new Listener(waiter));
        jda.addEventListener(new ErrorEvent());
        jda.addEventListener(new UserIsMuted());
        jda.addEventListener(new BotDMEvent());
        System.out.println("GosealeBot.Events added!");
    }

}

