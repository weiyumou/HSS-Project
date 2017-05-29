/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.File;
import java.util.List;
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
        
    public static void applicationClose(){
        TreeViewController.saveCurrentTreeView();
        AuthorInfoController.closeAuthorInfoScreen();
        currentStage.close();
    }
    
    public static void showMainScreen(){
        currentStage = new Stage();
        
        FileChooser openFileChooser = new FileChooser();
        FileChooser saveFileChooser = new FileChooser();
        configureOpenFileChooser(openFileChooser);
        configureSaveFileChooser(saveFileChooser);
        MainScreen.setOpenFileChooser(openFileChooser);
        MainScreen.setSaveFileChooser(saveFileChooser);
        
        Scene scene = new Scene(MainScreen.buildUI(), 1200, 700);
        scene.getStylesheets().add("resources/css/stylesheet.css");
        
        currentStage.setTitle("NTU-EA");
        currentStage.setScene(scene);
        currentStage.show();
    }
    
    private static void configureOpenFileChooser(FileChooser fileChooser) {      
        fileChooser.setTitle("打开作文");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("DAT", "*.dat"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT", "*.txt"));
    }
    
    private static void configureSaveFileChooser(FileChooser fileChooser) {      
        fileChooser.setTitle("保存标注至Excel");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("CSV", "*.csv")
        );
    }
    
    public static List<File> showOpenFileChooser() {
        return MainScreen.getOpenFileChooser().showOpenMultipleDialog(currentStage);
    }
    
    public static File showSaveFileChooser() {
        return MainScreen.getSaveFileChooser().showOpenDialog(currentStage);
    }

    public static void enableViewAuthorInfo(){
        MainScreen.getAuthorinfoButton().setDisable(false);
    }
    
    public static void enableSaveButton(){
        MainScreen.getSaveButton().setDisable(false);
    }
    
    public static void enableSaveToExcelButton(){
        MainScreen.getSaveToExcelButton().setDisable(false);
    }
    public static void enableSaveToXMLButton(){
        MainScreen.getSaveToXMLButton().setDisable(false);
    }
}
