package com.example.battleship.Game;

import com.example.battleship.Maps.Board;
import com.example.battleship.Maps.EnemyMap;
import com.example.battleship.Maps.MyMap;

import java.net.*;
import java.io.*;
import java.nio.file.Path;

import static com.example.battleship.Game.Player.CLIENT;
import static com.example.battleship.Game.Player.SERVER;

public class Server extends PlayerMenager {
    public Server() {
        enemyMap = new EnemyMap();
    }

    public Server(String fileName) {
        this();
        myMap = new MyMap(fileName);
    }

    public void runServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
//            serverSocket.setSoTimeout(3000);
            clientSocket = serverSocket.accept();
            write = new PrintWriter(clientSocket.getOutputStream(), true);
            read = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.out.println("Error: socket connection in runServer " + this.getClass().getSimpleName());
        } catch (SocketTimeoutException e) {
            System.out.println("Error: waiting too long to accept " + this.getClass().getSimpleName());
        } catch (IOException e) {
            System.out.println("Error: IO " + this.getClass().getSimpleName());
        }
    }


    @Override
    public void stop() {
        try {
            read.close();
            write.close();
            clientSocket.close();
            serverSocket.close();
        } catch (Exception e) {
            System.out.println("Error: closing server");
        }
    }

    public static void main(String[] args) {
        String file = "/home/maisho/java/MajaKwiatek-09-battleships/src/test/java/resources/Server/myMap.txt";
        Server server = new Server(file);
        server.runServer(8080);
        server.play();
    }
}


