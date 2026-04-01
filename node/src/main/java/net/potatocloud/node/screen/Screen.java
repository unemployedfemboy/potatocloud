package net.potatocloud.node.screen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Screen {

    public static final String NODE_SCREEN = "node_screen";

    private final String name;
    private final List<String> cachedLogs;

    public Screen(String name) {
        this.name = name;
        this.cachedLogs = new ArrayList<>(); // TODO: Check if Concurrent Modification Issues
    }

    public String name() {
        return name;
    }

    public List<String> cachedLogs() {
        return Collections.unmodifiableList(cachedLogs);
    }

    public void addLog(String log) {
        cachedLogs.add(log);
    }

    public void clearLogs() {
        cachedLogs.clear();
    }
}
