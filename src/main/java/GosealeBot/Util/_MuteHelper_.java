package GosealeBot.Util;

import GosealeBot.Configuration;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class _MuteHelper_ {
    private List<String> list = new LinkedList<>(Arrays.asList(new String[]{""}));
    public _MuteHelper_() {
    }

    public void addUser(String id) {
        if (list.contains(id) || id.equals(Configuration.get("owner_id"))) {
            return;
        }
        list.add(id);
    }

    public void removeUser(String id) {
        list.remove(id);
    }

    public List<String> getList() {
        return list;
    }
}
