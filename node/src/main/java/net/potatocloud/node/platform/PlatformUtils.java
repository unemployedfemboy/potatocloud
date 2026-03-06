package net.potatocloud.node.platform;

import lombok.experimental.UtilityClass;
import net.potatocloud.api.platform.Platform;
import net.potatocloud.api.platform.PlatformVersion;
import net.potatocloud.node.Node;

import java.nio.file.Path;

@UtilityClass
public class PlatformUtils {

    public Path getDirectoryOfPlatform(Platform platform, PlatformVersion version) {
        return Path.of(Node.getInstance().getConfig().getPlatformsFolder())
                .resolve(platform.getName())
                .resolve(version.getName());
    }

    public Path getPlatformJarPath(Platform platform, PlatformVersion version) {
        return getDirectoryOfPlatform(platform, version).resolve(version.getFullName() + ".jar");
    }
}

