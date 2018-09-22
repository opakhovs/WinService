package Vizualization;


import Logic.CommandsGetter;
import Logic.Program;
import Logic.ServiceOperator;
import javafx.application.Application;
import javafx.application.Platform;
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

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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

        Runnable work_time = new Runnable() {

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

                                        Label label = new Label("You are using \"" + currentProgram.getName() + "\" too long, wait a couple of time :)");
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
        Runnable not_opened = new Runnable() {

            @Override
            public void run() {
                int waitTime = CommandsGetter.getProperties().get(2).getValue() * 60;
                try {
                    if (waitTime != 0) {
                        CommandsGetter.makeTik();
                        if (CommandsGetter.getNotOpenedPrograms().compareTo("") == 0) {
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

                                        Label label = new Label("You are not opening \"" + CommandsGetter.getNotOpenedPrograms() + "\" fot a long time :)");
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
                } catch (Exception e) {
                }
            }
        };
        try {
            ScheduledExecutorService service = new ScheduledThreadPoolExecutor(1);
            service.scheduleWithFixedDelay(new Thread(work_time), 500, 500, TimeUnit.MILLISECONDS);
            service.scheduleWithFixedDelay(new Thread(not_opened), 500, 500, TimeUnit.MILLISECONDS);

            launch(args);
            service.shutdown();

            ExitThread t = new ExitThread();
            Runtime.getRuntime().addShutdownHook(t);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}

class ExitThread extends Thread {

    public void run() {
        CommandsGetter.updateDB();
    }
}