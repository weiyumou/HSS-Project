/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import view.LoginWindow;

/**
 *
 * @author weiyumou
 */
public class LoginController {
    
    private static LoginWindow loginWindow;
    private static Stage stage;
    
    public static void showLoginScreen(){
        stage = new Stage();
        stage.setTitle("登录");
        
        loginWindow = new LoginWindow(stage);
        stage.centerOnScreen();
        stage.show();
    }
    
    private static void closeLoginScreen(){
        stage.close();
    }
    
    private static void executeAuthetication(){
        String username = loginWindow.getUserName();
        String password = loginWindow.getPassword();
        Tuple<Boolean,String> auth_res = authenticate(username, password);
        if(auth_res.first){
            closeLoginScreen();
            MainScreenController.showMainScreen(username, auth_res.second);
        }else{
            loginWindow.setLoginInfo("用户名或密码错误");
        }
    }
    
    public static EventHandler<ActionEvent> getLoginEventHandler (){
        return (ActionEvent e) -> {
            executeAuthetication();
        };
    }
    
    public static EventHandler<KeyEvent> getLoginKeyEventHandler(){
        return (KeyEvent e) ->{
            if(e.getCode() == KeyCode.ENTER){
                executeAuthetication();
            }
        };
    }
    
    private static Tuple<Boolean,String> authenticate(String username, String password){
        String userType = "";
        boolean auth_res = false;
        if(username.equals("weiyumou") && password.equals("weiyumou")){
            userType = "管理员";
            auth_res = true;
        }else if(username.equals("user1") && password.equals("user1")){
            userType = "普通用户";
            auth_res = true;
        }else if(username.equals("user2") && password.equals("user2")){
            userType = "普通用户";
            auth_res = true;
        }else if(username.equals("user3") && password.equals("user3")){
            userType = "普通用户";
            auth_res = true;
        }
        return new Tuple(auth_res, userType);
    }
    
    private static class Tuple<T,P>{
        private final T first;
        private final P second;
        
        public Tuple(T first, P second){
            this.first = first;
            this.second = second;
        }
    }
}
