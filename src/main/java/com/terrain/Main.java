package com.terrain;

import com.terrain.ui.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main( String[] args )
    {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        MainView mainView = new MainView();

        Scene scene = new Scene(mainView, 1200, 600);

        stage.setTitle("Terrain Generator");
        stage.setScene(scene);
        stage.show();
    }
}
