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
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import model.Essay;
import model.Sentence;
import view.MainScreen;

/**
 *
 * @author weiyumou
 */
public class TextAreaController {
    
    private static Essay currentEssay;
    private static int currentSentenceNo;
    private static Sentence currentSentence;
    
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
            ToolbarController.adjustEssayDisplay(currentEssay.getTitle(), 
                currentEssay.getAuthorID());
            MainScreenController.enableViewAuthorInfo();
            currentSentenceNo = 1;
            displayEssay();
        }catch (IOException e) {
            e.printStackTrace();
	}
    }

    public static EventHandler<ScrollEvent> getScrollEventHandler(){
        return (ScrollEvent event) -> {
            if(event.getDeltaY() < 0){ //up
                scrollup();
            }else if(event.getDeltaY() > 0){ //down
                scrolldown();
            }
        };
    }

    public static EventHandler<KeyEvent> getScrollKeyEventHandler(){
        return (KeyEvent event) -> {
            if(event.getCode() == KeyCode.UP){
                scrolldown();
            }else if(event.getCode() == KeyCode.DOWN){
                scrollup();
            }
        };
    }
    
    private static void scrollup(){
        if(currentSentenceNo < currentEssay.getNumOfSentences()){
            ++currentSentenceNo;
            displayEssay();
        }
    }
    
    private static void scrolldown(){
       if(currentSentenceNo > 1){
            --currentSentenceNo;
            displayEssay();
        }
    }
    
    private static void displayEssay(){
        MainScreen.getPrevEssay().setText(currentEssay.getSegment(1, currentSentenceNo));
        MainScreen.getPrevEssay().setScrollTop(Double.MAX_VALUE);
        currentSentence = currentEssay.getSingleSentence(currentSentenceNo);
        MainScreen.getCurrEssay().setText("\t" + currentSentence.toString());
        MainScreen.getNextEssay().setText(currentEssay.getSegment(currentSentenceNo + 1));
        MainScreen.getNextEssay().setScrollTop(Double.MIN_VALUE);
    }
}
