/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.File;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import view.HSSProject;

/**
 *
 * @author weiyumou
 */
public class ToolbarController {
    public static EventHandler<ActionEvent> openFileEventHandler(){
        return (ActionEvent event) -> {
            File file = HSSProject.showFileChooser();
            if (file != null) {
                TextAreaController.readEssay(file.getPath());
            }
        };
    }
}
