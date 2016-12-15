/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.File;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import view.MainScreen;

/**
 *
 * @author weiyumou
 */
public class ToolbarController {
    public static EventHandler<ActionEvent> openFileEventHandler(){
        return (ActionEvent event) -> {
            File file = MainScreenController.showFileChooser();
            if (file != null) {
                TextAreaController.readEssay(file.getPath());
            }
        };
    }
    
    public static void adjustUserDisplay(String user, String userType){
        MainScreen.setUsernameLabel(user);
        MainScreen.setUsercategoryLabel(userType);
        if(userType.equals("管理员")){
            MainScreen.getAuthorinfoButton().setVisible(true);
        }
    }
}
