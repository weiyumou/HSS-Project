/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import javafx.stage.Stage;
import view.AuthorInfoWindow;

/**
 *
 * @author weiyumou
 */
public class AuthorInfoController {
    private static Stage stage;

    public static void showAuthorInfoScreen(String authorInfo) {
        stage = new Stage();
        stage.setTitle("作者信息");
        
        AuthorInfoWindow authorInfoWindow = new AuthorInfoWindow(stage, authorInfo);
        stage.centerOnScreen();
        stage.show();
    }
    
    public static void closeAuthorInfoScreen(){
        stage.close();
    }
    
    
}
