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
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import model.Essay;
import model.Mark;
import model.Paragraph;
import model.Sentence;
import view.MainScreen;
import model.Error;
/**
 *
 * @author weiyumou
 */
public class TextAreaController {

    private static Essay currentEssay = null;
    private static int currentSentenceNo;
    private static Sentence currentSentence;
    private static File essayFile;
    private static File excelFile;
    private static File xmlFile;

    
    public static File convertToDAT(List<File> txtFiles) {
        List<File> converted = new ArrayList<>();
        for (File txtFile : txtFiles) {
            String datPath = txtFile.getPath().replace(".txt", ".dat");
            File datFile = new File(datPath);
            Essay essay = readTextEssay(txtFile);
            try (ObjectOutputStream oos
                    = new ObjectOutputStream(new FileOutputStream(datFile))) {
                oos.writeObject(essay);
                converted.add(datFile);
            } catch (IOException ex) {
                showAlert("提示", "转换出现错误", ex.getMessage(), Alert.AlertType.ERROR);
            }
        }
        return converted.isEmpty() ? null : converted.get(0);
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
        } catch (IOException ex) {
            showAlert("提示", "打开出现错误", ex.getMessage(), Alert.AlertType.ERROR);
        }
        return essay;
    }
    
    public static void readDatEssay(File esfile, File exFile, File xlFile) {
        try (ObjectInputStream ois
                = new ObjectInputStream(new FileInputStream(esfile))) {
            currentEssay = (Essay) ois.readObject();
            TableViewController.load(currentEssay.getMarks());
            ToolbarController.adjustEssayDisplay(currentEssay.getTitle(),
                    currentEssay.getAuthorID());
            MainScreenController.enableViewAuthorInfo();
            MainScreenController.enableSaveButton();
            MainScreenController.enableSaveToExcelButton();
            MainScreenController.enableSaveToXMLButton();
            currentSentenceNo = 1;
            essayFile = esfile;
            excelFile = exFile;
            xmlFile = xlFile;
            displayEssay();
        } catch (IOException | ClassNotFoundException ex) {
            showAlert("提示", "打开出现错误", ex.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private static void displayEssay() {
        MainScreen.getPrevEssay().setText(currentEssay.getSegment(1, currentSentenceNo));
        MainScreen.getPrevEssay().setScrollTop(Double.MAX_VALUE);
        currentSentence = currentEssay.getSingleSentence(currentSentenceNo);
        MainScreen.getCurrEssay().replaceText("\t" + currentSentence.toString());
        MainScreen.getNextEssay().setText(currentEssay.getSegment(currentSentenceNo + 1));
        MainScreen.getNextEssay().setScrollTop(Double.MIN_VALUE);
        TableViewController.highlightMarks(currentSentence);
        TableViewController.highlightErrors(currentSentence);
    }

    public static void saveEssay(){
        try (ObjectOutputStream oos
                = new ObjectOutputStream(new FileOutputStream(essayFile))) {
            currentEssay.setMarks(TableViewController.getMarkList());
            oos.writeObject(currentEssay);
            oos.close();
            showAlert("提示", "", "已保存", Alert.AlertType.INFORMATION);
        } catch (IOException ex) {
            showAlert("提示", "保存出现错误", ex.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    public static void saveToExcel() {
        currentEssay.setMarks(TableViewController.getMarkList());
        final String[] colNames = MainScreen.getTableColNames();
        try (PrintWriter w = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(excelFile.getPath()), "UTF-8"));) {
            w.print("\uFEFF");
            int i;
            for (i = 0; i != colNames.length - 1; ++i) {
                w.print(colNames[i] + ",");
            }
            w.println(colNames[i]);
            currentEssay.getMarks().forEach((item) -> {
                w.println(item.toString());
            });
            w.close();
            showAlert("提示", "", "已保存", Alert.AlertType.INFORMATION);
        } catch (IOException ex) {
            showAlert("提示", "保存出现错误", ex.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private static void saveXMLMarks(Map.Entry<Integer, List<Error>> curr, 
            TreeMap<Integer, List<Error>> markMaps, 
            XMLStreamWriter xMLStreamWriter) throws XMLStreamException, IOException{
        
        Error currError = curr.getValue().remove(0);
        xMLStreamWriter.writeStartElement(currError.getLowestError());
        
        int end = curr.getKey() + currError.getSegment().length();
        int last_pos = curr.getKey();
        
        if(!curr.getValue().isEmpty()){
            Error nextError = curr.getValue().get(0);
            saveXMLMarks(curr, markMaps, xMLStreamWriter);
            last_pos += nextError.getSegment().length();
        }

        Map.Entry<Integer, List<Error>> next = markMaps.higherEntry(curr.getKey());
        while(next != null && next.getKey() < end){
            xMLStreamWriter.writeCharacters(currError.getSegment()
                    .substring(last_pos - curr.getKey(), next.getKey() - curr.getKey()));
            last_pos = next.getKey() + next.getValue().get(0).getSegment().length();
            saveXMLMarks(next, markMaps, xMLStreamWriter);
            next = markMaps.higherEntry(next.getKey());
        }
        xMLStreamWriter.writeCharacters(currError.getSegment()
                    .substring(last_pos - curr.getKey(), end - curr.getKey()));
        xMLStreamWriter.writeCharacters("|" + currError.getModification());
        markMaps.remove(curr.getKey());
        xMLStreamWriter.writeEndElement();
    }
    
    public static void saveToXML(){
        List<Mark> markList = TableViewController.getMarkList();
        try (StringWriter stringWriter = new StringWriter()) {
            XMLOutputFactory xMLOutputFactory = XMLOutputFactory.newInstance();
            XMLStreamWriter xMLStreamWriter = xMLOutputFactory.createXMLStreamWriter(stringWriter);
            xMLStreamWriter.writeStartDocument();
            xMLStreamWriter.writeCharacters("\n");
            xMLStreamWriter.writeStartElement("作文");
            xMLStreamWriter.writeAttribute("序号", currentEssay.getAuthorID());
            xMLStreamWriter.writeCharacters("\n");
            xMLStreamWriter.writeStartElement("背景");
            xMLStreamWriter.writeCharacters("\n" + currentEssay.getBackground());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeCharacters("\n");
            xMLStreamWriter.writeStartElement("正文");

            int para_count = 1;
            for(Paragraph para : currentEssay.getParagraphs()){
                xMLStreamWriter.writeCharacters("\n");
                xMLStreamWriter.writeStartElement("自然段");
                xMLStreamWriter.writeAttribute("段落号", Integer.toString(para_count));
                for(Sentence stnc : para.getSentences()){
                    xMLStreamWriter.writeCharacters("\n");
                    xMLStreamWriter.writeStartElement("句子");
                    xMLStreamWriter.writeAttribute("全文序号", Integer.toString(stnc.getIdInEssay()));
                    xMLStreamWriter.writeAttribute("本段序号", Integer.toString(stnc.getIdInParagraph()));
                    
                    String sentence = stnc.getContent();
                    TreeMap<Integer, List<Error>> markMaps = new TreeMap<>();
                    for(Mark mark : markList){
                        if(mark.getSentence() == stnc){
                            int start = sentence.indexOf(mark.getError().getSegment());
                            if(markMaps.containsKey(start)){
                                markMaps.get(start).add(mark.getError());
                            }else{
                                markMaps.put(start, new ArrayList<>(Arrays.asList(mark.getError())));
                            }
                        }
                    }
                    for (Map.Entry<Integer, List<Error>> entry : markMaps.entrySet()) {
                        Collections.sort(entry.getValue(), (Error e1, Error e2) -> {
                            if(e1.getSegment().length() > e2.getSegment().length()){
                                return -1;
                            }else if(e1.getSegment().length() < e2.getSegment().length()){
                                return 1;
                            }
                            return 0;
                        });
                    }
                    int last_pos = 0;
                    while(!markMaps.isEmpty()){
                        Map.Entry<Integer, List<Error>> curr = markMaps.firstEntry();
                        xMLStreamWriter.writeCharacters(sentence.substring(last_pos, curr.getKey()));
                        last_pos = curr.getKey() + curr.getValue().get(0).getSegment().length();
                        saveXMLMarks(curr, markMaps, xMLStreamWriter);
                    }
                    xMLStreamWriter.writeCharacters(sentence.substring(last_pos, sentence.length()));
                    xMLStreamWriter.writeEndElement();
                }
                xMLStreamWriter.writeCharacters("\n");
                xMLStreamWriter.writeEndElement();
                ++para_count;
            }
            
            xMLStreamWriter.writeCharacters("\n");
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeCharacters("\n");
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeEndDocument();
            xMLStreamWriter.flush();
            xMLStreamWriter.close();
            String xmlString = stringWriter.getBuffer().toString();

            PrintWriter pwriter = new PrintWriter(new OutputStreamWriter(
                    new FileOutputStream(xmlFile.getPath()), "UTF-8"));
            
            pwriter.println(xmlString);
            pwriter.close();
            showAlert("提示", "", "已保存", Alert.AlertType.INFORMATION);
        } catch (XMLStreamException | IOException ex) {
            showAlert("提示", "保存出现错误", ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private static void showAlert(String title, String header, 
            String text, Alert.AlertType type){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(text);
        alert.showAndWait();
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

    public static String getAuthorID() {
        return currentEssay != null ? currentEssay.getAuthorID() : "";
    }

    public static String getEssayTitle() {
        return currentEssay != null ? currentEssay.getTitle() : "";
    }
    
    public static String getAuthorInfo(){
        return currentEssay != null ? currentEssay.getBackground() : "";
    }

    public static Sentence getCurrentSentence() {
        return currentSentence;
    }
    
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
                showAlert("提示", "错误", "无法复制内容", Alert.AlertType.ERROR);
            }
        };
    }
}
