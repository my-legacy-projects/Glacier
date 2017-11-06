package me.marcsteiner.glacier;

import com.google.common.eventbus.EventBus;
import com.virtlink.commons.configuration2.jackson.JsonConfiguration;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.marcsteiner.glacier.bootstrap.BootstrapOptions;
import me.marcsteiner.glacier.commands.CommandManager;
import me.marcsteiner.glacier.database.Database;
import me.marcsteiner.glacier.database.impl.MySQLDatabase;
import org.apache.commons.cli.*;
import org.apache.commons.configuration2.builder.DefaultReloadingDetectorFactory;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.kohsuke.MetaInfServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.pippo.core.Application;
import ro.pippo.core.Pippo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@MetaInfServices
public class Glacier extends Application {

    @Getter(lazy = true)
    private static final Glacier instance = new Glacier();

    @Getter(lazy = true)
    private final Logger logger = LoggerFactory.getLogger(Glacier.class);

    @Getter @Setter(onParam = @__(@NonNull))
    private CommandLine cmdArgs;

    @Getter @Setter(onParam = @__(@NonNull))
    private JsonConfiguration config;

    @Getter @Setter(onParam = @__(@NonNull))
    private Pippo pippo;

    @Getter @Setter(onParam = @__(@NonNull))
    private Database database;

    @Getter @Setter(onParam = @__(@NonNull))
    private CommandManager commandManager;

    @Getter(lazy = true)
    private final EventBus eventBus = new EventBus();

    @Getter @Setter(onParam = @__(@NonNull))
    private Scanner consoleScanner;

    @Getter @Setter
    private boolean running = false;

    public static void main(String[] args) {
        getInstance().setRunning(true);
        getInstance().getLogger().info("Starting Glacier...");

        Options options = BootstrapOptions.getOptions();
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            getInstance().setCmdArgs(parser.parse(options, args));
        } catch (ParseException ex) {
            formatter.printHelp("argument", options);
            System.exit(-1);
        }

        String confPath = getInstance().getCmdArgs().getOptionValue("config");
        if(confPath == null) {
            confPath = "config.json";
        }
        if(!confPath.endsWith(".json")) {
            confPath += ".json";
        }

        File confFile = new File(Paths.get(".").toAbsolutePath().normalize().toFile(), confPath);
        if(!confFile.exists()) {
            try {
                Files.copy(Glacier.class.getResourceAsStream("/config.json"), confFile.toPath());
            } catch (IOException ex) {
                getInstance().getLogger().error("Could not create default config.json file.", ex);
                System.exit(-1);
            }
        }

        try {
            getInstance().setConfig(new FileBasedConfigurationBuilder<>(JsonConfiguration.class)
                    .configure(new Parameters().properties()
                            .setFileName(confPath)
                            .setThrowExceptionOnMissing(false)
                            .setListDelimiterHandler(new DefaultListDelimiterHandler(';'))
                            .setReloadingDetectorFactory(new DefaultReloadingDetectorFactory())
                            .setEncoding("UTF-8"))
            .getConfiguration());
        } catch (ConfigurationException ex) {
            getInstance().getLogger().error("Could not load config.json file. " +
                    "Please run it the config.json file thought a JSON validator and check for missing keys.", ex);
            System.exit(-1);
        }

        String address = getInstance().getCmdArgs().getOptionValue("address");
        if(address == null) {
            address = getInstance().getConfig().getString("server.address");

            if(address == null) {
                getInstance().getLogger().error("Could not find config key `address\'. Please check for missing keys.");
                System.exit(-1);
            }
        }

        int port = -1;
        try {
            String input = getInstance().getCmdArgs().getOptionValue("port");
            if(input != null) {
                port = Integer.parseInt(input);
            }
        } catch (NumberFormatException ex) {
            getInstance().getLogger().warn("Could not parse provided port to valid integer. Using config value...");
        }

        if(port == -1) {
            port = getInstance().getConfig().getInt("server.port");
        }

        String mode = getInstance().getCmdArgs().getOptionValue("mode");
        if(mode == null) {
            mode = getInstance().getConfig().getString("server.mode");
        }
        mode = mode.toLowerCase();

        switch(mode) {
            case "dev":
            case "test":
                break;
            default:
                mode = "prod";
                break;
        }

        System.setProperty("pippo.mode", mode);
        System.setProperty("pippo.reload.enabled", String.valueOf(
                mode.equalsIgnoreCase("dev") || getInstance().getConfig().getBoolean("server.reload")
        ));

        String dbAddress = getInstance().getConfig().getString("database.address");
        int dbPort = getInstance().getConfig().getInt("database.port");
        String dbDatabase = getInstance().getConfig().getString("database.database");
        String dbUsername = getInstance().getConfig().getString("database.username");
        String dbPassword = getInstance().getConfig().getString("database.password");

        if(dbAddress == null || dbDatabase == null || dbUsername == null || dbPassword == null) {
            getInstance().getLogger().error("Could not find database keys in config.json. Check for missing keys.");
            System.exit(-1);
        }

        try {
            getInstance().setDatabase(new MySQLDatabase(dbAddress, dbPort, dbDatabase, dbUsername, dbPassword));
            getInstance().getDatabase().connect();
            getInstance().getDatabase().setup();
        } catch (SQLException ex) {
            getInstance().getLogger().error("Could not connect to the MySQL database!", ex);
            System.exit(-1);
        }

        getInstance().setCommandManager(new CommandManager());
        getInstance().getCommandManager().scan();

        getInstance().setPippo(new Pippo(getInstance()));
        getInstance().getPippo().getServer().getSettings().host(address);
        getInstance().getPippo().getServer().getSettings().port(port);
        getInstance().getPippo().start();

        Runtime.getRuntime().addShutdownHook(new Thread(Glacier::shutdown));

        getInstance().getLogger().info("Hello and welcome to Glacier. Listening on https://{}:{}/", address, port);
        getInstance().getLogger().info("Type `help\' for a list of commands.");
        System.out.print("\n> ");

        getInstance().setConsoleScanner(new Scanner(System.in));

        // Continuously wait for input on another thread
        // This doesn't interfere with Pippo as this and Pippo are both running on another thread
        // The ConsoleInputEvent listeners on the other hand are running on the main thread
        Thread inputThread = new Thread(() -> {
            while(getInstance().isRunning()) {
                String input = getInstance().getConsoleScanner().nextLine();
                String[] instruction = input.split(" ");

                String cmdLabel = instruction[0];
                String[] cmdArgs = new String[0];

                if(instruction.length >= 2) {
                    List<String> tempArgs = new ArrayList<>();
                    tempArgs.addAll(Arrays.asList(instruction));
                    tempArgs.remove(0);
                    cmdArgs = tempArgs.toArray(new String[tempArgs.size()]);
                }

                getInstance().getCommandManager().execute(cmdLabel, cmdArgs);
            }
        });
        inputThread.setName("Input Thread");
        inputThread.setPriority(Thread.MIN_PRIORITY);
        inputThread.run();
    }

    @Override
    protected void onInit() {
        // Called when Jetty is initialized
    }

    private static void shutdown() {

    }

    @Override
    protected void onDestroy() {
        if(getInstance().getDatabase() != null) {
            getInstance().getDatabase().disconnect();
        }

        System.exit(0);

        // Called when Jetty is destroyed
    }

}
