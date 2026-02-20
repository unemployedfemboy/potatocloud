package net.potatocloud.node.console;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.potatocloud.node.Node;
import net.potatocloud.node.screen.Screen;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class Logger {

    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final String LATEST_LOG_FILENAME = "latest.log";
    private static final Pattern COLOR_PATTERN = Pattern.compile("(&.)|\u001B\\[[;\\d]*m");

    private final Console console;
    private final Path logsDirectory;
    private final List<String> cachedLogs = new ArrayList<>();

    public Logger(Console console, Path logsDirectory) {
        this.console = console;
        this.logsDirectory = logsDirectory;

        new ExceptionMessageHandler(this);

        final File latestLogFile = logsDirectory.resolve(LATEST_LOG_FILENAME).toFile();
        if (latestLogFile.exists()) {
            latestLogFile.delete();
        }
    }

    @Getter
    @RequiredArgsConstructor
    public enum Level {
        INFO("&a"),
        WARN("&e"),
        ERROR("&c"),
        COMMAND("&7");

        private final String colorCode;

    }

    public void info(String message) {
        log(Level.INFO, message);
    }

    public void warn(String message) {
        log(Level.WARN, message);
    }

    public void error(String message) {
        log(Level.ERROR, message);
    }

    public void logCommand(String command) {
        log(Level.COMMAND, command);
    }

    public List<String> getCachedLogs() {
        return Collections.unmodifiableList(cachedLogs);
    }

    @SneakyThrows
    private void log(Level level, String message) {
        final Date now = new Date();
        final String formattedTime = TIME_FORMAT.format(now);
        final String formattedDate = DATE_FORMAT.format(now);

        String uncoloredMessage;
        String coloredMessage;

        if (level.equals(Level.COMMAND)) {
            coloredMessage = console.getPrompt() + message;
            uncoloredMessage = removeColorCodes(coloredMessage);
        } else {
            uncoloredMessage = "[" + formattedTime + " " + level.name() + "] " + removeColorCodes(message);
            coloredMessage = "&8[&7" + formattedTime + " " + level.getColorCode() + level.name() + "&8] &7" + message;
        }

        final File dayLog = logsDirectory.resolve(formattedDate + ".log").toFile();
        final File latestLog = logsDirectory.resolve(LATEST_LOG_FILENAME).toFile();

        FileUtils.writeStringToFile(dayLog, uncoloredMessage + System.lineSeparator(), StandardCharsets.UTF_8, true);
        FileUtils.writeStringToFile(latestLog, uncoloredMessage + System.lineSeparator(), StandardCharsets.UTF_8, true);

        cachedLogs.add(coloredMessage);

        final boolean isNodeScreen = Node.getInstance().getScreenManager().getCurrentScreen().getName().equals(Screen.NODE_SCREEN);
        if (!level.equals(Level.COMMAND) && isNodeScreen) {
            console.println(coloredMessage);
        }
    }

    private String removeColorCodes(String input) {
        return COLOR_PATTERN.matcher(input).replaceAll("");
    }
}
