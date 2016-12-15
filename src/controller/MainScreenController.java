/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.File;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import view.MainScreen;

/**
 *
 * @author weiyumou
 */
public class MainScreenController {
    public static void showMainScreen(String user, String userType){
        Stage currentStage = MainScreen.getCurrentStage();
        FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser);
        MainScreen.setFileChooser(fileChooser);
        Scene scene = new Scene(MainScreen.buildUI(), 1280, 720);
        ToolbarController.adjustDisplay(user, userType);
        
        currentStage.setTitle("作文标注");
        currentStage.setScene(scene);
        currentStage.show();
    }
    
    private static void configureFileChooser(FileChooser fileChooser) {      
        fileChooser.setTitle("打开作文");
        fileChooser.setInitialDirectory(new File("src/essay"));
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("TXT", "*.txt")
        );
    }
}
