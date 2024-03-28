package com.example.battleship.Game;

import com.example.battleship.Construction.Coordinates;
import com.example.battleship.Construction.Field;
import com.example.battleship.Maps.Board;
import com.example.battleship.Maps.EnemyMap;
import com.example.battleship.Maps.MyMap;
import lombok.AccessLevel;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Random;

import static com.example.battleship.Construction.Field.*;

public abstract class PlayerMenager {


    @Getter(AccessLevel.PUBLIC)
    Socket clientSocket;
    @Getter(AccessLevel.PUBLIC)
    ServerSocket serverSocket;
    public static PrintWriter write;
    public static BufferedReader read;

    MyMap myMap;
    EnemyMap enemyMap;
    public static String myShot;
    public static String myResponse;

    public abstract void stop();

    public String evaluateEnemyShot(String enemyShotCoords) {
        Coordinates coords = new Coordinates(enemyShotCoords);
        Field responseField = myMap.getShotResponse(coords);
        return responseField.toString().replace("_", " ").toLowerCase();
    }


    public void play() {
        String gotMessage = "";
        gotMessage = getMessage().trim();

        while (true) {
            String response = gotMessage.split(";")[0].trim();
            if (!response.equals("start")) enemyMap.update(gotMessage);

            System.out.println("send: " + sendResponse(gotMessage, randomShot()) + "\n");
            myMap.update(gotMessage);
            gotMessage = getMessage();
            System.out.println("got: " + gotMessage);

            if (gotMessage == null) {
                System.out.println("\n" + "Przegrana");
                break;
            }
            if (gotMessage.equals("Błąd komunikacji")) exit();
            if (gotMessage.equals(OSTATNI_ZATOPIONY.toString())) {
                enemyMap.update(gotMessage);
                System.out.println("\n" + "Wygrana");
                break;
            }
        }
        myMap.getBoard().show();
        enemyMap.getBoard().show();
        stop();
    }


    public String sendResponse(String gotMessage, String shot) {
        String response;
        try {
            response = evaluateEnemyShot(gotMessage.split(";")[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            response = gotMessage;
        }
        if (response.equals(OSTATNI_ZATOPIONY.toString())) {
            write.println(response);
            return response;
        }
        myShot = shot;
        myResponse = response;
        response += ";" + myShot;
        write.println(response);
        return response;
    }

    public void reSendResponse() {
        String response = myResponse + ";" + myShot;
        write.println(response);
    }

    public void exit() {
        try {
            read.close();
            write.close();
            if (serverSocket != null) {
                serverSocket.close();
            }
            if (clientSocket != null) {
                clientSocket.close();
            }
        } catch (IOException e) {
            System.out.println("Error: exit closing");
        }
        System.exit(0);
    }

    boolean correctFieldResponse(String fieldResponse) {
        if (this.getClass() == Server.class && fieldResponse.equals("start")) return true;
        switch (fieldResponse) {
            case "pudło", "trafiony", "ostatni zatopiony", "trafiony zatopiony" -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    boolean correctCoordinates(String coords) {
        if (!Character.isUpperCase(coords.charAt(0))) return false;
        String number = coords.substring(1);
        try {
            Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return false;
        }
        return Board.inRange(new Coordinates(coords));
    }

    boolean correctCommunicate(String message) {
        if (message == null) return true;
        if (message.equals(OSTATNI_ZATOPIONY.toString())) return true;
        if (message.split(";").length < 2) return false;
        return message.split(";").length == 2 && correctCoordinates(message.split(";")[1]) && correctFieldResponse(message.split(";")[0]);
    }

    public String getMessage() {
        String message = "";
        int trials = 3;
        while (trials-- > 0) {
            try {
                message = read.readLine();
                if (correctCommunicate(message)) return message;
            } catch (SocketTimeoutException e) {
                System.out.println("waiting too long for response from other side");
            } catch (IOException e) {
                System.out.println("error in getMessage(): " + this.getClass().getSimpleName());
            }
            reSendResponse();
        }
        return "Błąd komunikacji";
    }


    public static Coordinates getCoordinates(String message) {
        return message.split(";").length < 2 ?
                new Coordinates(message) :
                new Coordinates(message.split(";")[1]);
    }

    public static Field getField(String message) {
        return message.split(";").length < 2 ?
                Field.valueOf(message.toUpperCase().replace(" ", "_")) :
                Field.valueOf(message.split(";")[0].toUpperCase().replace(" ", "_"));
    }

    public String randomShot() {
        Random random = new Random();
        char randomLetter = (char) ('A' + random.nextInt(10));
        int randomNumber = random.nextInt(10) + 1;
        return randomLetter + Integer.toString(randomNumber);
    }
}
