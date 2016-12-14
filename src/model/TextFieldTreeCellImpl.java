/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import controller.TreeViewController;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


/**
 *
 * @author weiyumou
 */
public class TextFieldTreeCellImpl extends TreeCell<String>{
    private TextField textField;
    private boolean addNewOtherItem;
    
    
    public TextFieldTreeCellImpl() {
        setOnMouseClicked(TreeViewController.getMouseEventHandler());
        addNewOtherItem = false;
    }

    @Override
    public void startEdit() {
        super.startEdit();
        
        String currText = (String) getItem();
        addNewOtherItem =  currText.equals(TreeViewController.TRIGGER_STRING);
        
        if (textField == null) {
            createTextField();
        }
        setText(null);
        setGraphic(textField);
        textField.selectAll();
        setVisible(false);
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText((String) getItem());
        setGraphic(null);
    }

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (textField != null) {
                    textField.setText(getString());
                }
                setText(null);
                setGraphic(textField);
            } else {
                
                setText(getString());
                setGraphic(null);
            }
        }
    }

    private void createTextField() {
        textField = new TextField(getString());
        if(addNewOtherItem){
            textField.setText("");
        }
        textField.setOnKeyReleased((KeyEvent t) -> {
            if (t.getCode() == KeyCode.ENTER) {
                commitEdit(textField.getText());
                if(addNewOtherItem && !textField.getText()
                    .equals(TreeViewController.TRIGGER_STRING)){
                    TreeViewController.addNewOtherItem();
                }
            } else if (t.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
            }
        });
        
    }

    private String getString() {
        return getItem() == null ? "" : getItem();
    }
}
