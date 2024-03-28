package uj.wmii.pwj.gvt;

import java.io.IOException;
import java.nio.file.*;

import static uj.wmii.pwj.gvt.Gvt.*;
import static uj.wmii.pwj.gvt.ConfigHandler.*;
import static uj.wmii.pwj.gvt.MessageHandler.*;

public enum Command{

    INIT {
        @Override
        public void execute(String... args) {
            if (Files.exists(MAIN_DIR_PATH) && Files.isDirectory(MAIN_DIR_PATH))
                exitHandler.exit(10, "Current directory is already initialized.");

            try {
                Files.createDirectory(MAIN_DIR_PATH);
                Files.createFile(CONFIG_FILE_PATH);

                String text = """
                    active_version: 0
                    last_version: 0
                    """;
                Files.write(CONFIG_FILE_PATH, text.getBytes(), StandardOpenOption.WRITE);
                Files.createDirectory(MAIN_DIR_PATH.resolve("0"));

                addMessage("0", "GVT initialized.");
                exitHandler.exit(0, "Current directory initialized successfully.");

            } catch (Exception ignored) {
            }
        }
    },
    ADD {
        @Override
        public void execute(String... args) {
            if (args.length < 2)
                exitHandler.exit(20, "Please specify file to " + this.toString().toLowerCase() + ".");

            String fileName = args[1];
            Path filePath = Paths.get(fileName);
            // ???
            if (!Files.exists(filePath)) {
                exitHandler.exit(21, "File not found. File: " + fileName);
                return;
            }

            if (Files.exists(MAIN_DIR_PATH.resolve(fileName))) {
                exitHandler.exit(0, "File already added. File: " + fileName);
                return;
            }

            String version = gvt.nextVersion(ACTIVE_VERSION);
            setConfigData(ACTIVE_VERSION, version);
            setConfigData(LAST_VERSION, version);

            try {
                Files.copy(filePath, MAIN_DIR_PATH.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                Files.copy(filePath, MAIN_DIR_PATH.resolve(version).resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
            }
            // ewentualnie blad ze ktos poda cos dziwnego typu -t a nie -m
            handleCommitMessage(fileName, version, this.toString().toLowerCase(), args);
        }
    },
    DETACH {
        @Override
        public void execute(String... args) {
            if (args.length < 2) {
                exitHandler.exit(30, "Please specify file to " + this.toString().toLowerCase() + ".");
                return;
            }

            String fileName = args[1];
            if(!Files.exists(MAIN_DIR_PATH.resolve(fileName)))
                exitHandler.exit(0, "File is not added to gvt. File: " + fileName);

            try{
                Files.delete(MAIN_DIR_PATH.resolve(LAST_VERSION).resolve(fileName));
                String version = gvt.nextVersion(ACTIVE_VERSION);
                handleCommitMessage(fileName, version, this.toString().toLowerCase(), args);
                setConfigData(LAST_VERSION, version);
                setConfigData(ACTIVE_VERSION, version);
            }catch(Exception e){
                exitHandler.exit(31, "File cannot be detached, see ERR for details. File: " + fileName);
            }
        }
    },
    CHECKOUT {
        @Override
        public void execute(String... args) {

        }
    },
    COMMIT {
        @Override
        public void execute(String... args) {

        }
    },

    HISTORY {
        @Override
        public void execute(String... args) {

        }
    },
    VERSION {
        @Override
        public void execute(String... args) {
            if (args.length < 2) {
                exitHandler.exit(0, "Version: " + getConfigData(ACTIVE_VERSION) + "\n" + getMessage(getConfigData(ACTIVE_VERSION)));
                return;
            }

            String version = args[1];
            if (!gvt.isCorrectVersion(version)) exitHandler.exit(60, "Invalid version number: " + version + ".");

            exitHandler.exit(0, "Version: " + version + "\n" + getMessage(version));
        }
    };

    private static final Gvt gvt = new Gvt(new ExitHandler());
//    private static final ExitHandler exitHandler = new ExitHandler();

    public abstract void execute(String... args);
}
