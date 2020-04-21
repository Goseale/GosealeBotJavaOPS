package GosealeBot.Command.Commands.Information;

import GosealeBot.Command.CommandContext;
import GosealeBot.Command.ICommand;
import GosealeBot.Configuration;
import GosealeBot.Util.GEmbedMissingArgs;
import GosealeBot.Util.GReportError;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.Arrays;
import java.util.List;

public class C_BotStatus implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        EmbedBuilder emb = new EmbedBuilder();
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

            TextChannel channel = ctx.getChannel();

            // Get current size of heap in bytes
            long heapSize = Runtime.getRuntime().totalMemory();

            // Get maximum size of heap in bytes. The heap cannot grow beyond this size.// Any attempt will result in an OutOfMemoryException.
            long heapMaxSize = Runtime.getRuntime().maxMemory();

            // Get amount of free memory within the heap in bytes. This size will increase // after garbage collection and decrease as new objects are created.
            long heapFreeSize = Runtime.getRuntime().freeMemory();

            emb.setTitle("Status");
            String message = "```bash\n";
            message+="$Ram usage   : \""+formatSize(heapSize) + "/"+formatSize(heapMaxSize) +" Free "+ formatSize(heapFreeSize)+"\"\n";
            message+="$Cpu usage   : \""+osBean.getSystemLoadAverage()+"%\"\n";
            message+="- - - - - - -|\n";
            message+="$Guilds im on: \""+ctx.getJDA().getGuilds().size()+"\"\n";
            message+="$Serving     : \""+ctx.getJDA().getUsers().size()+" Users\"\n";
            message+="```";
            emb.setDescription(message);
            channel.sendMessage(emb.build()).queue();
    }

    public static String formatSize(long v) {
        if (v < 1024) return v + " B";
        int z = (63 - Long.numberOfLeadingZeros(v)) / 10;
        return String.format("%.1f %sB", (double)v / (1L << (z*10)), " KMGTPE".charAt(z));
    }

    @Override
    public String getName() {
        return "bot_status";
    }

    @Override
    public String getHelp() {
        return "Returns the current status of the bot";
    }

    @Override
    public String getCategory() {
        return "Information";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList(new String[]{"bot_info","bot_stats"});
    }
}
