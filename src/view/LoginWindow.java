/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.LoginController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author weiyumou
 */
public class LoginWindow{

    private final TextField userTextField;
    private final PasswordField pwBox;
    private Text loginInfoText;
    
    public LoginWindow(Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        Text scenetitle = new Text("欢迎");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("用户名:");
        grid.add(userName, 0, 1);

        userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Label pw = new Label("密码:");
        grid.add(pw, 0, 2);

        pwBox = new PasswordField();
        grid.add(pwBox, 1, 2);
        
        Button btn = new Button("登录");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);
        
        
        HBox hbText = new HBox(10);
        hbText.setAlignment(Pos.BOTTOM_RIGHT);
        loginInfoText = new Text();
        hbText.getChildren().add(loginInfoText);        
        grid.add(hbText, 1, 6);
        
        btn.setOnAction(LoginController.getLoginEventHandler());
        
        Scene scene = new Scene(grid, 300, 275);
        scene.setOnKeyReleased(LoginController.getLoginKeyEventHandler());
        
        primaryStage.setScene(scene);
    }

    public String getUserName() {
        return userTextField.getText();
    }

    public String getPassword() {
        return pwBox.getText();
    }

    public void clearUserName() {
        this.userTextField.clear();
    }

    public void clearPassword() {
        this.pwBox.clear();
    }

    public void setLoginInfo(String loginInfo) {
        loginInfoText.setFill(Color.FIREBRICK);
        loginInfoText.setText(loginInfo);
    }
}
