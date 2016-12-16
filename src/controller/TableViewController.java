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
import java.util.Collections;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import javafx.util.Callback;
import model.Mark;
import model.Sentence;
import view.MainScreen;

/**
 *
 * @author weiyumou
 */
public class TableViewController {

    private static ObservableList<Mark> tableData;
    private static final ObservableList<Integer> highlightRows = FXCollections.observableArrayList();
    private static File dataFile;

    public static void loadData(Mark mark) {
        if (!tableData.contains(mark)) {
            tableData.add(mark);
            highlightRows.add(tableData.size() - 1);
//            MainScreen.getMarkTableView().setItems(tableData);
            MainScreen.getMarkTableView().scrollTo(Integer.MAX_VALUE);
        }
    }

    public static void load() {
//        tableData = FXCollections.observableArrayList();
        tableData.clear();
        try (ObjectInputStream ois
                = new ObjectInputStream(new FileInputStream(dataFile))) {
            while (true) {
                tableData.add((Mark) ois.readObject());
            }
        } catch (IOException | ClassNotFoundException ex) {
//            MainScreen.getMarkTableView().setItems(tableData);
            highlightMarks(TextAreaController.getCurrentSentence());
        }
    }

    public static void dump() {
        try (ObjectOutputStream oos
                = new ObjectOutputStream(new FileOutputStream(dataFile))) {
            for (int i = 0; i != tableData.size(); ++i) {
                oos.writeObject(tableData.get(i));
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void setDataFile(File dataFile) {
        TableViewController.dataFile = dataFile;
    }

    public static void clearSelections() {
        MainScreen.getMarkTableView().getSelectionModel().clearSelection();
    }

    public static void refreshTableView() {
        MainScreen.getMarkTableView().refresh();
    }

    public static void highlightMarks(Sentence currSentence) {
        highlightRows.clear();
        for (int i = 0; i != tableData.size(); ++i) {
            if (tableData.get(i).getSentence().equals(currSentence)) {
                highlightRows.add(i);
            }
        }
        if (!highlightRows.isEmpty()) {
            MainScreen.getMarkTableView().scrollTo(highlightRows.get(0));
        }
    }

    public static boolean updateSelectedItem(List<String> errorTypes) {
        int selectedIndex = MainScreen.getMarkTableView().getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            return false;
        }
        Mark currMark = tableData.get(selectedIndex);
        currMark.getError().setErrorTypes(errorTypes);
        refreshTableView();
        return true;
    }

    public static void attachTableView(TableView<Mark> tableView) {
        tableData = FXCollections.observableArrayList();
        tableView.setItems(tableData);
    }

    public static EventHandler<KeyEvent> getDeleteKeyEventHandler() {
        return (KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.F1) {
                int index = MainScreen.getMarkTableView()
                        .getSelectionModel().getSelectedIndex();
                tableData.remove(index);
                highlightRows.remove((Integer)index);
                refreshTableView();
            }
        };
    }

    public static Callback<TableView<Mark>, TableRow<Mark>> getRowFactory() {
        return (TableView<Mark> tableView) -> {
            final TableRow<Mark> row = new TableRow<Mark>() {
                @Override
                protected void updateItem(Mark person, boolean empty) {
                    super.updateItem(person, empty);
                    if (highlightRows.contains(getIndex())) {
                        if (!getStyleClass().contains("highlightedRow")) {
                            getStyleClass().add("highlightedRow");
                        }
                    } else {
                        getStyleClass().removeAll(Collections.singleton("highlightedRow"));
                    }
                }
            };
            row.setOnMouseClicked((MouseEvent mouseEvent) -> {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 1) {
                        Mark currMark = tableData.get(row.getIndex());
                        TextAreaController.scrollTo(currMark.getSentence().getIdInEssay());
                        TextAreaController.highlightText(tableData.get(row.getIndex())
                                .getError().getSegment());
                    }
                }
            });
            highlightRows.addListener(new ListChangeListener<Integer>() {
                @Override
                public void onChanged(Change<? extends Integer> change) {
                    if (highlightRows.contains(row.getIndex())) {
                        if (!row.getStyleClass().contains("highlightedRow")) {
                            row.getStyleClass().add("highlightedRow");
                        }
                    } else {
                        row.getStyleClass().removeAll(Collections.singleton("highlightedRow"));
                    }
                }
            });
            tableData.addListener(new ListChangeListener<Mark>() {
                @Override
                public void onChanged(ListChangeListener.Change<? extends Mark> change) {
                    refreshTableView();
                }
            });
            return row;
        };
    }
}
