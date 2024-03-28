package com.example.battleship;

import com.example.battleship.Game.Client;
import com.example.battleship.Game.Player;
import com.example.battleship.Game.Server;

import org.junit.*;
import org.junit.jupiter.api.AfterAll;
import org.mockito.Mock;

import java.io.*;

import static com.example.battleship.Construction.Field.*;
import static com.example.battleship.Game.Player.CLIENT;
import static com.example.battleship.Game.Player.SERVER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SocketTest {
    @Mock
    static Client client;
    @Mock
    Server server;
    @Mock
    InputStream inputStream;
    @Mock
    OutputStream outputStream;

    static final int PORT = 8080;
    static final String IP = "127.0.0.1";
    ProcessBuilder pb;
    static Process process;

    static final String FILE = "/home/maisho/java/MajaKwiatek-09-battleships/src/test/java/resources/Client/myMap.txt";

    public SocketTest() {
    }

    @AfterAll
    public static void finish() throws IllegalArgumentException {
        process.destroy();
    }

    void buildProcess(Player player) {
        pb = new ProcessBuilder("/usr/bin/java", "com.example.battleship.Game." + player);
        pb.directory(new File("/home/maisho/java/MajaKwiatek-09-battleships/target/classes/"));
        try {
            process = pb.start();
            Thread.sleep(1000);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    @Test
    public void sendingMessages() {
        buildProcess(SERVER);

        client = new Client(FILE);
        client.runServer(IP, PORT);
        client.startGame("A1");
        String gotMessage = client.getMessage();
        assertEquals(PUDŁO.toString(), gotMessage.split(";")[0]);
        client.sendResponse(gotMessage, "B2");
        assertEquals(PUDŁO.toString(), client.getMessage().split(";")[0]);
        client.sendResponse(gotMessage, "E10");
        assertEquals(TRAFIONY_ZATOPIONY.toString(), client.getMessage().split(";")[0]);
        client.sendResponse(gotMessage, "J1");
        assertEquals(TRAFIONY.toString(), client.getMessage().split(";")[0]);
        client.write.println("ostatni zatopiony");
        client.stop();
    }

    @Test
    public void communicationErrors() {
        buildProcess(SERVER);

        client = new Client(FILE);
        client.runServer(IP, PORT);
        client.startGame("A1");
        String firstMessage = client.getMessage();

        client.sendResponse(firstMessage, "B1111");
        assertEquals(firstMessage, client.getMessage());

        client.sendResponse("wrong command", "A1");
        assertEquals(firstMessage, client.getMessage());

        client.sendResponse(firstMessage, "djew");
        assertEquals(firstMessage, client.getMessage());

        client.sendResponse("gsquwgtsyq", "B2");
        client.stop();
    }

    @Test
    public void continuousCommunication() {
        buildProcess(SERVER);
        client = new Client(FILE);
        client.runServer(IP, PORT);
        client.startGame(client.randomShot());
        client.play();
    }


}
