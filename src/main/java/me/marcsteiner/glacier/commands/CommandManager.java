package me.marcsteiner.glacier.commands;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.marcsteiner.glacier.Glacier;
import org.reflections.Reflections;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

public class CommandManager {

    @Getter
    private Map<Command, CommandExecutor> commands = new HashMap<>();

    @Getter @Setter
    private ThreadPoolExecutor threadPoolService;

    public void scan() {
        Reflections reflections = new Reflections("me.marcsteiner.glacier.commands.impl");
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Command.class);

        for(Class<?> clazz : classes) {
            Object instance;
            try {
                instance = clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                Glacier.getInstance().getLogger().error("Could not initialize class.", ex);
                continue;
            }

            register(clazz.getAnnotation(Command.class), (CommandExecutor) instance);
        }
    }

    private void register(@NonNull Command command, @NonNull CommandExecutor executor) {
        for(Map.Entry<Command, CommandExecutor> entry : commands.entrySet()) {
            if(entry.getKey().value().equalsIgnoreCase(command.value()) ||
               Arrays.asList(entry.getKey().aliases()).contains(command.value())) {
                Glacier.getInstance().getLogger().error("Found a command with the similar name: `{}\'", command.value());
                return;
            }

            for (String s : command.aliases()) {
                if(entry.getKey().value().equalsIgnoreCase(s) ||
                   Arrays.asList(entry.getKey().aliases()).contains(s)) {
                    Glacier.getInstance().getLogger().error("Found a alias with the similar name: `{}\'", command.value());
                    return;
                }
            }
        }

        Glacier.getInstance().getLogger().info("Found clean command: `" + command.value() + "\'. Registering...");
        commands.put(command, executor);
    }

    public void execute(@NonNull String command, @NonNull String[] args) {
        boolean commandFound = false;

        for(Map.Entry<Command, CommandExecutor> entry : commands.entrySet()) {
            if(entry.getKey().value().equalsIgnoreCase(command) ||
               Arrays.asList(entry.getKey().aliases()).contains(command)) {

                if(entry.getKey().args() != -1 && args.length != entry.getKey().args()) {
                    Glacier.getInstance().getLogger().error("Syntax: " + entry.getKey().usage());
                    return;
                }

                threadPoolService.submit(() -> {
                    try {
                        boolean result = entry.getValue().execute(command, args);
                        if(!result) {
                            Glacier.getInstance().getLogger().error("Syntax: " + entry.getKey().usage());
                        }

                    } catch (CommandExecuteException ex) {
                        Glacier.getInstance().getLogger().error(
                                "A error occurred while executing `{}\': {}", command, ex.getMessage()
                        );
                    } finally {
                        System.out.print("\n> ");
                    }
                });

                commandFound = true;
            }
        }

        if (!commandFound) {
            Glacier.getInstance().getLogger().error("Command `{}\' could not be found.", command);
            System.out.print("\n> ");
        }
    }

}
