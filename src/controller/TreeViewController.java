/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import javafx.scene.control.TreeItem;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.xml.sax.SAXException;
import view.MainScreen;
import model.Error;
import model.Mark;
/**
 *
 * @author weiyumou
 */
public class TreeViewController {
    public static final String TRIGGER_STRING = "添加...";
    private static final String PATH = "src/xml/errorType.xml";
    
    private static TreeItem<String> constructTree(TreeItem<String> treeItemRoot, 
            Node xmlRoot){
        
        treeItemRoot.setExpanded(true);
        NodeList children = xmlRoot.getChildNodes();
        
        for(int i = 0; i != children.getLength(); ++i){
            Node xmlNode = children.item(i);
            if (xmlNode.getNodeType() == Node.ELEMENT_NODE) {
                Element xmlElement = (Element) xmlNode;
                TreeItem<String> treeItem = new TreeItem<> (xmlElement.getAttribute("name"));
                treeItem = constructTree(treeItem, xmlNode);
                treeItemRoot.getChildren().add(treeItem);
            }
            
        }

        return treeItemRoot;
    }
    public static TreeItem<String> buildTreeView(){
        
        TreeItem<String> treeItemRoot = new TreeItem<>();
        
        try {
            File inputFile = new File(PATH);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            
            Element xmlRoot = doc.getDocumentElement();
            xmlRoot.normalize();
            treeItemRoot.setValue(xmlRoot.getAttribute("name"));
            constructTree(treeItemRoot, xmlRoot);
        }catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
        return treeItemRoot;
    }
    
    public static void clearSelections(){
        TreeView<String> errorTreeView = MainScreen.getErrorTreeView();
        errorTreeView.getSelectionModel().clearSelection();
    }
    
    public static EventHandler<MouseEvent> getMouseEventHandler() {
        return (MouseEvent mouseEvent) -> {
            if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                if(mouseEvent.getClickCount() == 1){
                    TreeView<String> errorTreeView = MainScreen.getErrorTreeView();
                    TreeItem<String> currNode = errorTreeView.getSelectionModel().getSelectedItem();
                    String selectedText = MainScreen.getCurrEssay().getSelectedText();
                    
                    if(!(currNode.getChildren().size() > 1 || 
                        currNode.getValue().equals(TRIGGER_STRING) || 
                        selectedText.isEmpty())){
                        
                        List<String> errorTypes = new ArrayList<>();
                        while(currNode.getParent() != null){
                            errorTypes.add(currNode.getValue());
                            currNode = currNode.getParent();
                        }
                        Collections.reverse(errorTypes);
                        
                        Error error = new Error(errorTypes, selectedText, "");
                        Mark mark = new Mark(TextAreaController.getAuthorID(), 
                            TextAreaController.getCurrentSentence(), error);
                        TableViewController.load(mark);
                    }
                }
            }
        };
    }
    
    public static void addNewOtherItem(){
        TreeView<String> errorTreeView = MainScreen.getErrorTreeView();
        TreeItem<String> currNode = errorTreeView.getSelectionModel().getSelectedItem();
        currNode.getParent().getChildren().add(new TreeItem<>(TRIGGER_STRING));
    }
    
    private static void deconstructTree(TreeItem<String> treeItemRoot, 
            XMLStreamWriter xMLStreamWriter, int depth) throws XMLStreamException{
        
        switch (depth) {
            case 0:
                xMLStreamWriter.writeStartElement("ErrorType");
                break;
            case 1:
                xMLStreamWriter.writeStartElement("TypeI");
                break;
            case 2:
                xMLStreamWriter.writeStartElement("TypeII");
                break;
            case 3:
                xMLStreamWriter.writeStartElement("TypeIII");
                break;
            default:
                break;
        }
        xMLStreamWriter.writeAttribute("name", treeItemRoot.getValue());
        ObservableList<TreeItem<String>> children = treeItemRoot.getChildren();
        for(int i = 0; i != children.size(); ++i){
            deconstructTree(children.get(i), xMLStreamWriter, depth + 1);
        }
        xMLStreamWriter.writeEndElement();
    }
    
    
    public static void saveCurrentTreeView(){
        TreeView<String> errorTreeView = MainScreen.getErrorTreeView();
        if(errorTreeView == null){
            return;
        }
        TreeItem<String> treeItemRoot = errorTreeView.getRoot();
        String xmlString;
        try (StringWriter stringWriter = new StringWriter()) {
            XMLOutputFactory xMLOutputFactory = XMLOutputFactory.newInstance();
            XMLStreamWriter xMLStreamWriter = xMLOutputFactory.createXMLStreamWriter(stringWriter);
            xMLStreamWriter.writeStartDocument();

            deconstructTree(treeItemRoot, xMLStreamWriter, 0);
            xMLStreamWriter.writeEndDocument();
            xMLStreamWriter.flush();
            xMLStreamWriter.close();
            xmlString = stringWriter.getBuffer().toString();
            
            List<String> lines = Arrays.asList(xmlString);
            Path file = Paths.get(PATH);
            Files.write(file, lines, Charset.forName("UTF-8"));
        }catch (XMLStreamException | IOException e) {
            e.printStackTrace();
        }
    }
}
