/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Mark;
import view.MainScreen;

/**
 *
 * @author weiyumou
 */
public class TableViewController {

    private static ObservableList<Mark> tableData = FXCollections.observableArrayList();

    public static void loadData(Mark mark) {
        if (!tableData.contains(mark)) {
            tableData.add(mark);
            MainScreen.getMarkTableView().setItems(tableData);
            
        }
    }

    public static void load(File path) {
        tableData = FXCollections.observableArrayList();
        try (ObjectInputStream ois
                = new ObjectInputStream(new FileInputStream(path))) {
            while (true) {
                tableData.add((Mark) ois.readObject());
            }
        } catch (IOException | ClassNotFoundException ex) {
            MainScreen.getMarkTableView().setItems(tableData);
        }
    }

    public static void dump(String path) {
        try (ObjectOutputStream oos
                = new ObjectOutputStream(new FileOutputStream(path))) {
            for(int i = 0; i != tableData.size(); ++i){
                 oos.writeObject(tableData.get(i));
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
