/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import model.Essay;
import model.Paragraph;
import model.Sentence;
import view.HSSProject;

/**
 *
 * @author weiyumou
 */
public class TextAreaController {
    
    private static Essay currentEssay;
    
    public static ChangeListener<Boolean> getChangeListener(){
        return (ObservableValue<? extends Boolean> arg0, 
            Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (newPropertyValue){
//                System.out.println("Textfield on focus");
                TreeViewController.clearSelections();
            }
        };
    }
    
    public static void readEssay(String path){
        List<String> lines;
        try(BufferedReader br = Files.newBufferedReader(Paths.get(path))) {
            lines = br.lines().collect(Collectors.toList());
            currentEssay = new Essay(lines);
            displayEssay();
        }catch (IOException e) {
            e.printStackTrace();
	}
    }
    
    private static void displayEssay(){
        HSSProject.getCurrEssay().setText(currentEssay.toString());
    }
}
