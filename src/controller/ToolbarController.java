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
                File currFile = files.get(0);
                String fileName = currFile.getName();
                String extension = "";
                int i = fileName.lastIndexOf('.');
                if (i > 0) {
                    extension = fileName.substring(i+1);
                }
                
                if(extension.equals("txt")){
                    currFile = TextAreaController.convertToDAT(currFile);
                }
                
                //                if(extension.equals("dat")){
                String excelPath = currFile.getPath().replace(".dat", ".csv");
                String xmlPath = currFile.getPath().replace(".dat", ".xml");

                TextAreaController.readDatEssay(currFile, new File(excelPath), new File(xmlPath));
               
                
//                TableViewController.setDataFile(markFile);
//                TableViewController.setExcelFile(excelFile);
//                if(markFile.exists() && !markFile.isDirectory()){
//                TableViewController.load(marks);
//                }
//                }            
            }
        };
    }

    public static EventHandler<ActionEvent> getSaveFileEventHandler() {
        return (ActionEvent event) -> {
            TextAreaController.saveEssay();
        };
    }
    
    public static EventHandler<ActionEvent> getSaveToExcelEventHandler(){
        return (ActionEvent event) -> {
              TextAreaController.saveToExcel();
        };
    }
    
    public static EventHandler<ActionEvent> getSaveToXMLEventHandler(){
        return (ActionEvent event) -> {
            TextAreaController.saveToXML();
        };
    }
    
    public static EventHandler<ActionEvent> getViewAuthorInfoEventHandler(){
        return (ActionEvent event) -> {
            AuthorInfoController.showAuthorInfoScreen(TextAreaController.getAuthorInfo());
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
