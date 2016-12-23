/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
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

    private static Essay currentEssay = null;
    private static int currentSentenceNo;
    private static Sentence currentSentence;

    //Textfield on focus
    public static ChangeListener<Boolean> getChangeListener() {
        return (ObservableValue<? extends Boolean> arg0,
                Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (newPropertyValue) {
                TreeViewController.clearSelections();
                TableViewController.clearSelections();
            }
        };
    }

    public static void readDatEssay(File file) {
        try (ObjectInputStream ois
                = new ObjectInputStream(new FileInputStream(file))) {
            currentEssay = (Essay) ois.readObject();
            ToolbarController.adjustEssayDisplay(currentEssay.getTitle(),
                    currentEssay.getAuthorID());
            MainScreenController.enableViewAuthorInfo();
            MainScreenController.enableSaveButton();
            MainScreenController.enableSaveToExcelButton();
            currentSentenceNo = 1;
            displayEssay();
        } catch (IOException | ClassNotFoundException ex) {
        }
    }

    private static Essay readTextEssay(File file) {
        List<String> lines = new ArrayList<>();
        Essay essay = null;
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            essay = new Essay(lines);
        } catch (IOException e) {
        }
        return essay;
    }

    public static EventHandler<ScrollEvent> getScrollEventHandler() {
        return (ScrollEvent event) -> {
            System.out.println("Scroll");
            if (event.getDeltaY() < 0) { //up
                scrollup();
            } else if (event.getDeltaY() > 0) { //down
                scrolldown();
            } 
        };
    }

    public static EventHandler<KeyEvent> getScrollKeyEventHandler() {
        return (KeyEvent event) -> {
            if (event.getCode() == KeyCode.UP) {
                scrolldown();
            } else if (event.getCode() == KeyCode.DOWN) {
                scrollup();
            }
        };
    }

    public static EventHandler<KeyEvent> getNoCopyKeyEventHandler() {
        return (KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.C && keyEvent.isShortcutDown()) {
                keyEvent.consume();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("提示");
                alert.setHeaderText("错误");
                alert.setContentText("无法复制内容");
                alert.showAndWait();
            }
        };
    }

    private static void scrollup() {
        if (currentSentenceNo < currentEssay.getNumOfSentences()) {
            ++currentSentenceNo;
            displayEssay();
        }
    }

    private static void scrolldown() {
        if (currentSentenceNo > 1) {
            --currentSentenceNo;
            displayEssay();
        }
    }

    public static void scrollTo(int sentenceNo) {
        currentSentenceNo = sentenceNo;
        displayEssay();
    }

    private static void displayEssay() {
        MainScreen.getPrevEssay().setText(currentEssay.getSegment(1, currentSentenceNo));
        MainScreen.getPrevEssay().setScrollTop(Double.MAX_VALUE);
        currentSentence = currentEssay.getSingleSentence(currentSentenceNo);
//        MainScreen.getCurrEssay().setText("\t" + currentSentence.toString());
        MainScreen.getCurrEssay().replaceText("\t" + currentSentence.toString());
        
        MainScreen.getNextEssay().setText(currentEssay.getSegment(currentSentenceNo + 1));
        MainScreen.getNextEssay().setScrollTop(Double.MIN_VALUE);

        TableViewController.highlightMarks(currentSentence);
        TableViewController.highlightErrors(currentSentence);
    }

    public static String getAuthorID() {
        return currentEssay.getAuthorID();
    }

    public static String getEssayTitle() {
        if (currentEssay == null) {
            return "";
        }
        return currentEssay.getTitle();
    }

    public static Sentence getCurrentSentence() {
        return currentSentence;
    }

//    public static void highlightText(String segment) {
//        MainScreen.getCurrEssay().selectRange(0, 0);
//        int start = MainScreen.getCurrEssay().getText().indexOf(segment);
//        if (start != -1) {
//            MainScreen.getCurrEssay().selectRange(start, start + segment.length());
//        }
//    }

    public static void convertToDAT(List<File> files) {
        for (File file : files) {
            String datPath = file.getPath().replace(".txt", ".dat");
            File datFile = new File(datPath);
            Essay essay = readTextEssay(file);
            try (ObjectOutputStream oos
                    = new ObjectOutputStream(new FileOutputStream(datFile))) {
                oos.writeObject(essay);
            } catch (IOException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("提示");
                alert.setHeaderText("转换出现错误");
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
                return;
            }
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("提示");
        alert.setHeaderText(null);
        alert.setContentText("转换成功");
        alert.showAndWait();
    }
    
}
