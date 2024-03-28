package com.example.battleship;

import com.example.battleship.Game.Client;
import com.example.battleship.Game.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BattleshipApplication {

    public static void main(String[] args) {
        String mode, ip, map;
        int port;
        mode = args[1];
        System.out.println("mode: " + mode);

        if (mode.equals("server")) {
            port = Integer.parseInt(args[3]);
            System.out.println("port: " + port);
            map = args[5];
            System.out.println("map: " + map);
            Server server = new Server(map);
            server.runServer(port);
            server.play();
            server.stop();
        } else {
            ip = args[3];
            port = Integer.parseInt(args[5]);
            map = args[7];
            Client client = new Client(map);
            client.runServer(ip, port);
            client.startGame(client.randomShot());
            client.play();
            client.stop();
        }
    }

}
