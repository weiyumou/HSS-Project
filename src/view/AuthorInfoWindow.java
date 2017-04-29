/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author weiyumou
 */
public class AuthorInfoWindow{

    public AuthorInfoWindow(Stage primaryStage, String authorInfo) {
        ScrollPane root = new ScrollPane();
        Scene scene = new Scene(root, 500, 350);
        
        Text info = new Text(authorInfo);
        info.wrappingWidthProperty().bind(scene.widthProperty());
        root.setFitToWidth(true);
        root.setContent(info);
        primaryStage.setScene(scene);
    }
}
