/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.File;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import view.MainScreen;

/**
 *
 * @author weiyumou
 */
public class ToolbarController {

    public static EventHandler<ActionEvent> getOpenFileEventHandler() {
        return (ActionEvent event) -> {
            List<File> files = MainScreenController.showOpenFileChooser();
            if (files != null) {
                File firstFile = files.get(0);
                String fileName = firstFile.getName();
                String extension = "";
                
                int i = fileName.lastIndexOf('.');
                if (i > 0) {
                    extension = fileName.substring(i+1);
                }
                
                if(extension.equals("dat")){
                    TextAreaController.readDatEssay(firstFile);
                    String markPath = firstFile.getPath().replace(".dat", ".mak");
                    String excelPath = firstFile.getPath().replace(".dat", ".csv");
                    File markFile = new File(markPath);
                    File excelFile = new File(excelPath);
                    TableViewController.setDataFile(markFile);
                    TableViewController.setExcelFile(excelFile);
                    if(markFile.exists() && !markFile.isDirectory()){
                        TableViewController.load();
                    }
                }else if(extension.equals("txt")){
                    TextAreaController.convertToDAT(files);
                }             
            }
        };
    }

    public static EventHandler<ActionEvent> getSaveFileEventHandler() {
        return (ActionEvent event) -> {
//            File file = MainScreenController.showSaveFileChooser();
//            if (file != null) {
                TableViewController.dump();
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("提示");
                alert.setHeaderText(null);
                alert.setContentText("已保存");
                alert.showAndWait();
//            }
        };
    }
    
    public static EventHandler<ActionEvent> getSaveToExcelEventHandler(){
        return (ActionEvent event) -> {
//            File file = MainScreenController.showSaveFileChooser();
            TableViewController.dumpToExcel();
        };
    }

    public static void adjustUserDisplay(String user, String userType) {
        MainScreen.setUsernameLabel(user);
        MainScreen.setUsercategoryLabel(userType);
        if (userType.equals("管理员")) {
            MainScreen.getAuthorinfoButton().setVisible(true);
        }
    }

    public static void adjustEssayDisplay(String title, String author) {
        resetEssayDisplay();
        MainScreen.setEssayTitle(title);
        MainScreen.setAuthorID(author);
    }

    public static void resetEssayDisplay() {
        MainScreen.clearEssayTitle();
        MainScreen.clearAuthorID();
        MainScreen.setEssayTitle("当前文章: ");
        MainScreen.setAuthorID("作者序号: ");
    }
}
