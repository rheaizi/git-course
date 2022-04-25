package Networking;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Application {

    private static int genPin() {
        return 12345;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        StackPane root = new StackPane();

        root.setStyle("-fx-background-color: #3e147f");

        BorderPane borderPane = new BorderPane();

        Label lbl = new Label("Game PIN:\n" + genPin());
        lbl.setTextFill(Color.WHITE);
        lbl.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 20));

        lbl.setAlignment(Pos.CENTER);
        lbl.setMinWidth(600);
        borderPane.setTop(lbl);
        root.getChildren().addAll(borderPane);
        new Thread(() -> {
            try {
                ServerSocket server = new ServerSocket(2022);
                int clientNo = 1;
                while (true) {
                    try {
                        System.out.println("Waiting for incomes");
                        Socket socket = server.accept();
                        System.out.println(clientNo + " Client is Connected!");
                        new Thread(() -> {
                            try {
                                DataInputStream fromClient = new DataInputStream(socket.getInputStream());
                                DataOutputStream toClient = new DataOutputStream(socket.getOutputStream());
                                while (true) {
                                    int clientPin = fromClient.readInt();
//                            System.out.println(clientPin);
                                    if (clientPin != genPin()) {
                                        toClient.writeUTF("Wrong PIN!");
                                    } else {
                                        toClient.writeUTF("Success!");
                                    }
                                    String nickname = fromClient.readUTF();
                                    System.out.println(nickname);
                                    Label nLbl = new Label(nickname);
                                    nLbl.setTextFill(Color.WHITE);
                                    nLbl.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 20));

                                    Platform.runLater(() -> {
                                                borderPane.setCenter(nLbl);

                                            }
                                    );

                                    String clientChoice = fromClient.readUTF();
                                    System.out.println(clientChoice);
                                }

                            } catch (IOException e) {

                            }

                        }).start();
                        clientNo++;
//        }
                    } catch (IOException e) {

                    }
                }
            } catch (IOException e) {

            }

        }).start();

        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.setTitle("Server");
        primaryStage.show();

    }
}
