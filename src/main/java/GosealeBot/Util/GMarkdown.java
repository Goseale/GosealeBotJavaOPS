package GosealeBot.Util;

public class GMarkdown {
    public GMarkdown() {
    }

    public String getMarkdown(String text, String link) {
        return "["+text+"]("+link+")";
    }
}
