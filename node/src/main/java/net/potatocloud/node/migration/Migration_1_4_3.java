package net.potatocloud.node.migration;

import lombok.SneakyThrows;
import net.potatocloud.api.property.Property;
import net.potatocloud.api.utils.Version;
import net.potatocloud.core.migration.Migration;
import net.potatocloud.core.migration.MigrationManager;
import net.potatocloud.node.utils.YamlUtils;
import org.apache.commons.io.FileUtils;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Migration_1_4_3 extends Migration {

    private final Path groupsFolder;
    private final Path backupsFolder;

    public Migration_1_4_3(Path groupsFolder, Path backupsFolder, MigrationManager manager) {
        super("Migration 1.4.3", Version.of(1, 4, 2), Version.of(1, 4, 3));
        this.groupsFolder = groupsFolder;
        this.backupsFolder = backupsFolder;
        manager.registerMigration(this);
    }

    @Override
    public void execute() {
        backupGroupFiles();
        migrateGroupFiles();
    }

    @SneakyThrows
    private void backupGroupFiles() {
        final Path groupBackupFolder = backupsFolder.resolve("groups");
        if (!Files.exists(groupBackupFolder)) {
            Files.createDirectories(groupBackupFolder);
        }

        final String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        final Path currentBackupFolder = groupBackupFolder.resolve(timestamp);
        Files.createDirectories(currentBackupFolder);

        final Collection<File> files = FileUtils.listFiles(groupsFolder.toFile(), new String[]{"yml"}, false);
        for (File file : files) {
            final Path target = currentBackupFolder.resolve(file.getName());
            Files.copy(file.toPath(), target);
        }
    }

    @SneakyThrows
    private void migrateGroupFiles() {
        final Collection<File> files = FileUtils.listFiles(groupsFolder.toFile(), new String[]{"yml"}, false);

        for (File file : files) {
            final YamlFile config = new YamlFile(file);
            config.load();

            final String name = config.getString("name");
            final String platformFullName = config.getString("platform");
            final String[] platformParts = platformFullName.split("-", 2);
            final String platform = platformParts[0];
            final String platformVersion = platformParts[1];

            final List<String> templates = config.getStringList("templates");
            final int minOnlineCount = config.getInt("minOnlineCount");
            final int maxOnlineCount = config.getInt("maxOnlineCount");
            final int maxPlayers = config.getInt("maxPlayers");
            final int maxMemory = config.getInt("maxMemory");
            final boolean fallback = config.getBoolean("fallback");
            final boolean staticServer = config.getBoolean("static");
            final int startPriority = config.getInt("startPriority");
            final int startPercentage = config.getInt("startPercentage");
            final String javaCommand = config.getString("javaCommand");
            final List<String> customJvmFlags = config.getStringList("customJvmFlags");
            final Map<String, Property<?>> properties = YamlUtils.getProperties(config);

            final boolean setJvmFlags = config.isSet("customJvmFlags");

            YamlUtils.clear(config);

            config.set("name", name);
            config.set("platform", platform);
            config.set("platform-version", platformVersion);
            config.set("java-command", javaCommand);
            if (setJvmFlags) {
                config.set("jvm-flags", customJvmFlags);
            }

            config.set("max-players", maxPlayers);
            config.set("max-memory", maxMemory);
            config.set("min-online-count", minOnlineCount);
            config.set("max-online-count", maxOnlineCount);

            config.set("static", staticServer);
            config.set("fallback", fallback);
            config.set("start-priority", startPriority);
            config.set("start-percentage", startPercentage);

            config.set("templates", templates);

            if (!properties.isEmpty()) {
                for (Property<?> property : properties.values()) {
                    config.set("properties." + property.getName() + ".value", property.getValue());
                    config.set("properties." + property.getName() + ".default", property.getDefaultValue());
                }
            }

            config.save();
        }
    }
}