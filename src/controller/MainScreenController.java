/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.File;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import view.MainScreen;

/**
 *
 * @author weiyumou
 */
public class MainScreenController {
    
    private static Stage currentStage;
    
    public static void login(){
        LoginController.showLoginScreen();
    }
    
    public static void logout(){
        applicationClose();
        login();
    }
    
    public static void applicationClose(){
        TreeViewController.saveCurrentTreeView();
        currentStage.close();
    }
    
    public static void showMainScreen(String user, String userType){
        currentStage = new Stage();
        FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser);
        MainScreen.setFileChooser(fileChooser);
        Scene scene = new Scene(MainScreen.buildUI(), 1280, 720);
        ToolbarController.adjustUserDisplay(user, userType);
        
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
    
    public static EventHandler<ActionEvent> getLogoutEventHandler(){
        return (ActionEvent e) -> {
            logout();
        };
    }
    
    public static File showFileChooser() {
        return MainScreen.getFileChooser().showOpenDialog(currentStage);
    }
}
