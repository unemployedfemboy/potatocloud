package net.potatocloud.core.migration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.potatocloud.api.utils.Version;

import java.io.IOException;

@Getter
@RequiredArgsConstructor
public abstract class Migration {

    private final String name;
    private final Version from;
    private final Version to;

    public abstract void execute();

}
