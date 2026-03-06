package net.potatocloud.node.config;

import lombok.Getter;
import net.potatocloud.core.utils.ResourceFileUtils;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Getter
public class NodeConfig {

    private static final String CONFIG_FILE_NAME = "config.yml";

    private final String prompt;
    private final boolean enableBanner;
    private final int primaryColorCode;
    private final boolean logPlayerConnections;

    private final int serviceStartPort;
    private final int proxyStartPort;
    private final String splitter;
    private final boolean platformAutoUpdate;

    private final String groupsFolder;
    private final String staticFolder;
    private final String tempServicesFolder;
    private final String templatesFolder;
    private final String platformsFolder;
    private final String logsFolder;
    private final String dataFolder;
    private final String backupsFolder;

    private final String nodeHost;
    private final int nodePort;

    public NodeConfig() {
        final Path configPath = Path.of(CONFIG_FILE_NAME);

        if (!Files.exists(configPath)) {
            ResourceFileUtils.copyResourceFile(
                    CONFIG_FILE_NAME,
                    configPath
            );
        }

        final YamlFile config = new YamlFile(configPath.toFile());
        try {
            config.load();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read " + CONFIG_FILE_NAME, e);
        }

        prompt = config.getString("console.prompt");
        enableBanner = config.getBoolean("console.enable-banner");
        primaryColorCode = config.getInt("console.primary-color");
        logPlayerConnections = config.getBoolean("console.log-player-connections");

        serviceStartPort = config.getInt("service.service-start-port");
        proxyStartPort = config.getInt("service.proxy-start-port");
        splitter = config.getString("service.service-splitter");
        platformAutoUpdate = config.getBoolean("service.auto-update-platforms");

        groupsFolder = config.getString("folders.groups");
        staticFolder = config.getString("folders.static");
        tempServicesFolder = config.getString("folders.temp-services");
        templatesFolder = config.getString("folders.templates");
        platformsFolder = config.getString("folders.platforms");
        logsFolder = config.getString("folders.logs");
        dataFolder = config.getString("folders.data");
        backupsFolder = config.getString("folders.backups");

        nodeHost = config.getString("node.host");
        nodePort = config.getInt("node.port");
    }
}
