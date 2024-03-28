package com.example.battleship.Game;

import com.example.battleship.Maps.EnemyMap;
import com.example.battleship.Maps.MyMap;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.file.Path;

import static com.example.battleship.Game.Player.CLIENT;
import static com.example.battleship.Game.Player.SERVER;

public class Client extends PlayerMenager {

    public Client() {
        enemyMap = new EnemyMap();
    }

    public Client(String filePath) {
        this();
        myMap = new MyMap(filePath);
    }

    public void runServer(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
//            clientSocket.setSoTimeout(3000);
            write = new PrintWriter(clientSocket.getOutputStream(), true);
            read = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.out.println("Error: socket connection in startSerer " + this.getClass().getSimpleName());
        } catch (IOException e) {
            System.out.println("Error: IO " + this.getClass().getSimpleName());
        }
    }

    public void startGame(String myShotCoords) {
        myShot = myShotCoords;
        write.println("start" + ";" + myShot);
    }

    @Override
    public void stop() {
        try {
            read.close();
            write.close();
            clientSocket.close();
        } catch (IOException e) {
            System.out.println("Error: socket or IO " + this.getClass().getSimpleName());
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.runServer("127.0.0.1", 8080);
        client.startGame("A1");
        client.play();
    }
}