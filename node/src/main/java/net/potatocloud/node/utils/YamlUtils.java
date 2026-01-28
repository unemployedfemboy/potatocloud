package net.potatocloud.node.utils;

import net.potatocloud.api.property.Property;
import org.simpleyaml.configuration.file.YamlFile;

import java.util.HashMap;
import java.util.Map;

public final class YamlUtils {

    private YamlUtils() {
    }

    public static void clear(YamlFile config) {
        config.getKeys(false).forEach(key -> config.set(key, null));
    }

    public static Map<String, Property<?>> getProperties(YamlFile config) {
        final Map<String, Property<?>> properties = new HashMap<>();
        if (config.isSet("properties")) {
            for (String key : config.getConfigurationSection("properties").getKeys(false)) {
                final Object value = config.get("properties." + key + ".value");
                Object defaultValue = config.get("properties." + key + ".default");
                if (defaultValue == null) {
                    defaultValue = value;
                }

                properties.put(key, Property.of(key, defaultValue, value));
            }
        }
        return properties;
    }
}
