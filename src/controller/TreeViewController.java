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
import javafx.event.EventHandler;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.xml.sax.SAXException;
import view.HSSProject;
/**
 *
 * @author weiyumou
 */
public class TreeViewController {
    public static final String TRIGGER_STRING = "添加...";
    
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
            File inputFile = new File("src/xml/errorType.xml");
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
        TreeView<String> errorTreeView = HSSProject.getErrorTreeView();
        errorTreeView.getSelectionModel().clearSelection();
    }
    
    public static EventHandler<MouseEvent> getMouseEventHandler() {
        return (MouseEvent mouseEvent) -> {
            if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                if(mouseEvent.getClickCount() == 1){
                    System.out.println("Single clicked");
//                    clearSelections();
                }
            }
        };
    }
    
    public static void addNewOtherItem(){
        TreeView<String> errorTreeView = HSSProject.getErrorTreeView();
        TreeItem<String> currNode = errorTreeView.getSelectionModel().getSelectedItem();
        currNode.getParent().getChildren().add(new TreeItem<>(TRIGGER_STRING));
    }
}
