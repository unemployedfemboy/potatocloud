package net.potatocloud.core.migration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.potatocloud.api.utils.version.Version;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@RequiredArgsConstructor
public abstract class Migration {

    private final String name;
    private final Version from;
    private final Version to;

    public abstract void execute();

    protected Path createBackupsDirectory(Path path, String subDirectoryName) {
        try {
            final Path subDirectory = path.resolve(subDirectoryName);
            if (!Files.exists(subDirectory)) {
                Files.createDirectories(subDirectory);
            }

            final String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            final Path backupsDirectory = subDirectory.resolve(timestamp);
            Files.createDirectories(backupsDirectory);

            return backupsDirectory;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create backups directory!", e);
        }
    }
}
