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
    private static String user;
    private static String userType;
    
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
        MainScreenController.user = user;
        MainScreenController.userType = userType;
        currentStage = new Stage();
        
        FileChooser openFileChooser = new FileChooser();
//        FileChooser saveFileChooser = new FileChooser();
        configureOpenFileChooser(openFileChooser);
//        configureSaveFileChooser(saveFileChooser);
        MainScreen.setOpenFileChooser(openFileChooser);
//        MainScreen.setSaveFileChooser(saveFileChooser);
        
        Scene scene = new Scene(MainScreen.buildUI(), 1280, 720);
        scene.getStylesheets().add("file:src/css/stylesheet.css");
        ToolbarController.adjustUserDisplay(user, userType);
        
        currentStage.setTitle("作文标注");
        currentStage.setScene(scene);
        currentStage.show();
    }
    
    private static void configureOpenFileChooser(FileChooser fileChooser) {      
        fileChooser.setTitle("打开作文");
        fileChooser.setInitialDirectory(new File("src/essay"));
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("TXT", "*.txt")
        );
    }
    
//    private static void configureSaveFileChooser(FileChooser fileChooser) {      
//        fileChooser.setTitle("保存标注");
//        fileChooser.setInitialDirectory(new File("src/essay"));
//        fileChooser.getExtensionFilters().addAll(
//            new FileChooser.ExtensionFilter("DAT", "*.dat")
//        );
////        fileChooser.setInitialFileName(TextAreaController.getEssayTitle() + ".dat");
//    }
    
    public static EventHandler<ActionEvent> getLogoutEventHandler(){
        return (ActionEvent e) -> {
            logout();
        };
    }
    
    public static File showOpenFileChooser() {
        return MainScreen.getOpenFileChooser().showOpenDialog(currentStage);
    }
    
//    public static File showSaveFileChooser() {
//        return MainScreen.getSaveFileChooser().showOpenDialog(currentStage);
//    }

    public static String getUser() {
        return user;
    }

    public static String getUserType() {
        return userType;
    }
    
    public static void enableViewAuthorInfo(){
        if(userType.equals("管理员")){
            MainScreen.getAuthorinfoButton().setDisable(false);
        }
    }
}
