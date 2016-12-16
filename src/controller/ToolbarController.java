/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.File;

import java.io.IOException;

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

    public static EventHandler<ActionEvent> openFileEventHandler() {
        return (ActionEvent event) -> {
            File file = MainScreenController.showOpenFileChooser();
            if (file != null) {
                TextAreaController.readEssay(file.getPath());
                String datPath = file.getPath().replace(".txt", ".dat");
                File datFile = new File(datPath);
                if(datFile.exists() && !datFile.isDirectory()){
                    TableViewController.setDataFile(datFile);
                    TableViewController.load();
                }else{
                    datFile.getParentFile().mkdirs();
                    try {
                        datFile.createNewFile();
                        TableViewController.setDataFile(datFile);
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            }
        };
    }

    public static EventHandler<ActionEvent> saveFileEventHandler() {
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
