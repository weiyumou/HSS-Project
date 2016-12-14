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
import org.xml.sax.SAXException;
/**
 *
 * @author weiyumou
 */
public class TreeViewController {
    
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
}
