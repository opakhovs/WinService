package Vizualization;


import Logic.CommandsGetter;
import Logic.Program;
import Logic.ServiceOperator;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Map;
import java.util.concurrent.*;

public class Main extends Application {

    private static int sec = 0;
    private static Program currentProgram;
    private static Stage stage;
    private static Stage stageOpened;

    @Override
    public void start(Stage primaryStage) throws Exception {
        CommandsGetter.LoadDB();
        CommandsGetter.controlTray();
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("WinServiceIDEA");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    public static void main(String[] args) {

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                CommandsGetter.updateDB();
            }
        });

        Thread work_time = new Thread() {

            @Override
            public void run() {
                int waitTime;
                try {
                    if (currentProgram == null) {
                        currentProgram = CommandsGetter.getService(ServiceOperator.getCurrentWindow());
                        sec = 0;
                    } else if (currentProgram.getName().compareTo(ServiceOperator.getCurrentWindow()) != 0) {
                        currentProgram = CommandsGetter.getService(ServiceOperator.getCurrentWindow());
                        sec = 0;
                    } else if (stage != null && stage.isShowing()) {
                        sec = 0;
                    } else {
                        waitTime = 0;
                        if (currentProgram.getWork_time() != 0)
                            waitTime = currentProgram.getWork_time() * 60;
                        if (waitTime != 0 && sec == waitTime) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        stage = new Stage();
                                        Group root = new Group();
                                        Scene scene = new Scene(root, 400, 150, Color.WHITE);

                                        GridPane gridpane = new GridPane();
                                        gridpane.setPadding(new Insets(5));
                                        gridpane.setHgap(10);
                                        gridpane.setVgap(10);

                                        Label label = new Label("You are using \"" + currentProgram.getName() + "\" too long, rest a couple of time :)");
                                        label.setTranslateY(7);
                                        label.setTranslateX(30);
                                        label.setTextAlignment(TextAlignment.CENTER);

                                        Label timeLab = new Label("At least " + Integer.toString(currentProgram.getRest_time() * 60) + " seconds");
                                        timeLab.setTranslateY(40);
                                        timeLab.setTranslateX(140);
                                        timeLab.setTextAlignment(TextAlignment.CENTER);

                                        Button okBtn = new Button("OK");
                                        okBtn.setOnAction(event -> {
                                            stage.close();
                                        });
                                        okBtn.setTranslateX(170);
                                        okBtn.setTranslateY(80);


                                        GridPane.setHalignment(label, HPos.CENTER);
                                        gridpane.add(label, 0, 0);
                                        gridpane.add(timeLab, 0, 0);
                                        gridpane.add(okBtn, 0, 0);


                                        root.getChildren().add(gridpane);
                                        stage.setTitle("WinServiceIDEA");
                                        stage.setScene(scene);
                                        stage.initModality(Modality.WINDOW_MODAL);
                                        stage.show();
                                    } catch (Exception e) {
                                        System.out.println(e);
                                    }
                                }
                            });
                            sec = -1;
                        }
                        sec++;
                    }

                } catch (Exception e) {
                }

            }
        };

        Thread not_opened = new Thread() {

            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                    if (stage == null || !stage.isShowing()) {
                        if (CommandsGetter.getProperties().get(2).getValue() > 0) {
                            CommandsGetter.makeTik();
                            String proramsNames = CommandsGetter.getNotOpenedPrograms();
                            if (proramsNames.compareTo("") != 0) {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {

                                            stageOpened = new Stage();
                                            Group root = new Group();
                                            Scene scene = new Scene(root, 400, 150, Color.WHITE);

                                            GridPane gridpane = new GridPane();
                                            gridpane.setPadding(new Insets(5));
                                            gridpane.setHgap(10);
                                            gridpane.setVgap(10);

                                            Label label = new Label("You are not opening \"" + proramsNames + "\" fot a long time :)");
                                            label.setTranslateY(35);
                                            label.setTranslateX(30);
                                            label.setTextAlignment(TextAlignment.CENTER);

                                            Button okBtn = new Button("OK");
                                            okBtn.setOnAction(event -> {
                                                stageOpened.close();
                                            });
                                            okBtn.setTranslateX(170);
                                            okBtn.setTranslateY(70);

                                            GridPane.setHalignment(label, HPos.CENTER);
                                            gridpane.add(label, 0, 0);
                                            gridpane.add(okBtn, 0, 0);


                                            root.getChildren().add(gridpane);
                                            stageOpened.setTitle("WinServiceIDEA");
                                            stageOpened.setScene(scene);
                                            stageOpened.initModality(Modality.WINDOW_MODAL);
                                            stageOpened.show();
                                        } catch (Exception e) {
                                            System.out.println(e);
                                        }
                                    }
                                });
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }
        };

        try {

            ScheduledExecutorService service = new ScheduledThreadPoolExecutor(2);
            service.scheduleAtFixedRate(work_time, 0, 1000, TimeUnit.MILLISECONDS);
            service.scheduleAtFixedRate(not_opened, 0, 1000, TimeUnit.MILLISECONDS);

            launch(args);

            service.shutdown();

        } catch (Exception e) {
        }

        System.exit(0);
    }
}