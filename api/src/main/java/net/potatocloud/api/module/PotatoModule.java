package net.potatocloud.api.module;

import net.potatocloud.api.utils.version.Version;

public interface PotatoModule {

    void onEnable();

    void onDisable();

    String getName();

    Version version();

}
