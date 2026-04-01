package net.potatocloud.node.command.arguments;

import net.potatocloud.node.command.ArgumentType;

public class BooleanArgument extends ArgumentType<Boolean> {

    public BooleanArgument(String name) {
        super(name);
    }

    @Override
    public ParseResult<Boolean> parse(String input) {
        try {
            return ParseResult.success(Boolean.parseBoolean(input));
        } catch (NumberFormatException e) {
            return ParseResult.error("&cInvalid boolean: &a" + input);
        }
    }
}
