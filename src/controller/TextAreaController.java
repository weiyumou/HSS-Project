/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 *
 * @author weiyumou
 */
public class TextAreaController {
    public static ChangeListener<Boolean> getChangeListener(){
        return (ObservableValue<? extends Boolean> arg0, 
            Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (newPropertyValue){
//                System.out.println("Textfield on focus");
                TreeViewController.clearSelections();
            }
        };
    }
}
