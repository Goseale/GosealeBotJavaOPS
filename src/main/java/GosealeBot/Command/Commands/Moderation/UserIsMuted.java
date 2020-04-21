package GosealeBot.Command.Commands.Moderation;

import GosealeBot.Bot;
import GosealeBot.Util.Values;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.List;

public class UserIsMuted extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        Member member = event.getMember();
        try {
        List<Role> role = event.getGuild().getRolesByName(Values.mutedRoleName,false);
        if (role != null || !role.isEmpty() || Bot.helper.getList().contains(member.getId())) {
            if (member.getRoles().contains(role.get(0)) || Bot.helper.getList().contains(member.getId())) {
                event.getMessage().delete().queue();
            }
        }
        } catch (Exception e) {

        }

    }
}
