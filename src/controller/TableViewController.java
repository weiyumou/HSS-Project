/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.FileOutputStream;
import java.io.IOException;
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

    private static final ObservableList<Mark> tableData = FXCollections.observableArrayList();

    public static void load(Mark mark) {
        if (!tableData.contains(mark)) {
            tableData.add(mark);
            MainScreen.getMarkTableView().setItems(tableData);
        }
    }

    public static ObservableList<Mark> getTableData() {
        return tableData;
    }

    public static void dump(String path) {
        FileOutputStream fout = null;
        ObjectOutputStream oos = null;
        try {
            fout = new FileOutputStream(path);
            oos = new ObjectOutputStream(fout);
            ObservableList<Mark> tableData = TableViewController.getTableData();
            oos.writeObject(tableData.get(0));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } finally {
            if (fout != null) {
                try {
                    fout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

