package uj.wmii.pwj.gvt;


import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;

import static uj.wmii.pwj.gvt.ConfigHandler.*;
import static uj.wmii.pwj.gvt.MessageHandler.*;

public class Gvt {
    //    private static final String MAIN_DIR_STR = ;
    static final Path MAIN_DIR_PATH = Paths.get(".gvt");
    static final Path CONFIG_FILE_PATH = MAIN_DIR_PATH.resolve("config.txt");
    static final String ACTIVE_VERSION = "active_version";
    static final String LAST_VERSION = "last_version";

//    private final ExitHandler exitHandler;
    static final ExitHandler exitHandler = new ExitHandler();

    ExitHandler getExitHandler() {
        return this.exitHandler;
    }

    public Gvt(ExitHandler exitHandler) {
//        this.exitHandler = exitHandler;
    }

    boolean isCorrectVersion(String str) {
        try {
            int number = Integer.parseInt(str);
            return number > 0 && number <= Integer.parseInt(getConfigData(LAST_VERSION));
        } catch (Exception e) {
            return false;
        }
    }

    String nextVersion(String type) {
        return Integer.toString(Integer.parseInt(getConfigData(type)) + 1);
    }






    public static void main(String... args) {
        System.out.println("----------------------------------");
        Gvt gvt = new Gvt(new ExitHandler());

        gvt.mainInternal();

//        gvt.mainInternal("init");

//        gvt.mainInternal("add", "a.txt");


//        gvt.mainInternal("add", "c.txt", "-m", "Adding C FILE");
//        gvt.mainInternal("version");
    }

    void mainInternal(String... args) {
        if (args.length == 0) {
            exitHandler.exit(1, "Please specify command.");
            return;
        }

        String command = args[0];

//         katalog .gvt nie istnieje i zadana komenda to nie init
        if (!Files.exists(MAIN_DIR_PATH) && !command.equals("init")) {
            exitHandler.exit(-2, "Current directory is not initialized. Please use init command to initialize.");
            return;
        }

        try {
            Command.valueOf(command.toUpperCase()).execute(args);
        } catch (IllegalArgumentException e) {
            exitHandler.exit(1, "Unknown command " + command);
        }

    }

}
