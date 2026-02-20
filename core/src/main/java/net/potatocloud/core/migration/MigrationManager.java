package net.potatocloud.core.migration;

import lombok.RequiredArgsConstructor;
import net.potatocloud.api.utils.version.Version;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
public class MigrationManager {

    private final Version previousVersion;
    private final List<Migration> migrations = new ArrayList<>();

    public void registerMigration(Migration migration) {
        migrations.add(migration);

        migrations.sort(Comparator.comparing(Migration::getFrom));
    }

    public void migrate() {
        Version versionToMigrate = previousVersion;

        while (true) {
            final Migration next = findNextMigration(versionToMigrate);
            if (next == null) {
                break;
            }

            next.execute();

            versionToMigrate = next.getTo();
        }
    }

    private Migration findNextMigration(Version current) {
        return migrations.stream()
                .filter(m -> m.getFrom().equals(current))
                .findFirst()
                .orElse(null);
    }
}
