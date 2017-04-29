/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.ArrayList;
import java.util.Collection;
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
import org.fxmisc.richtext.StyleSpan;
import org.fxmisc.richtext.StyleSpans;
import org.fxmisc.richtext.StyleSpansBuilder;
import view.MainScreen;

/**
 *
 * @author weiyumou
 */
public class TableViewController {

    private static ObservableList<Mark> tableData;
    private static final ObservableList<Integer> highlightRows = FXCollections.observableArrayList();
    

    public static void loadData(Mark mark) {
        if (!tableData.contains(mark)) {
            tableData.add(mark);
            highlightRows.add(tableData.size() - 1);
            MainScreen.getMarkTableView().scrollTo(Integer.MAX_VALUE);
            highlightSeg(mark.getError().getSegment(), mark.getError().getErrorTypes().get(0));
        }
    }
    
    public static void load(List<Mark> marks) {
        tableData.clear();
        marks.forEach((mark) -> {
            tableData.add(mark);
        });
    }
    
    public static List<Mark> getMarkList(){
        List<Mark> markList = new ArrayList<>();
        tableData.forEach((mark) -> {
            markList.add(mark);
        });
        return markList;
    }

//    public static void load() {
//        tableData.clear();
//        try (ObjectInputStream ois
//                = new ObjectInputStream(new FileInputStream(dataFile))) {
//            while (true) {
//                tableData.add((Mark) ois.readObject());
//            }
//        } catch (IOException | ClassNotFoundException ex) {
//            highlightMarks(TextAreaController.getCurrentSentence());
//            highlightErrors(TextAreaController.getCurrentSentence());
//        }
//    }

//    public static void dump() {
//        try (ObjectOutputStream oos
//                = new ObjectOutputStream(new FileOutputStream(dataFile))) {
//            for (int i = 0; i != tableData.size(); ++i) {
//                oos.writeObject(tableData.get(i));
//            }
//        } catch (IOException ex) {
//            System.out.println(ex.getMessage());
//        }
//    }


//    public static void setExcelFile(File excelFile) {
//        TableViewController.excelFile = excelFile;
//    }

    public static void clearSelections() {
        MainScreen.getMarkTableView().getSelectionModel().clearSelection();
    }

    public static void refreshTableView() {
        MainScreen.getMarkTableView().refresh();
    }

    public static void highlightMarks(Sentence currSentence) {
        highlightRows.clear();
        for (int i = 0; i < tableData.size(); ++i) {
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
        highlightSeg(currMark.getError().getSegment(), currMark.getError().getErrorTypes().get(0));
        refreshTableView();
        return true;
    }

    public static void attachTableView(TableView<Mark> tableView) {
        tableData = FXCollections.observableArrayList();
        tableView.setItems(tableData);
    }

    public static EventHandler<KeyEvent> getDeleteKeyEventHandler() {
        return (KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.DELETE) {
                int index = MainScreen.getMarkTableView()
                        .getSelectionModel().getSelectedIndex();
                Mark currMark = tableData.get(index);
                tableData.remove(index);
                highlightRows.remove((Integer) index);
                unhighlightSeg(currMark.getError().getSegment());
                TextAreaController.scrollTo(currMark.getSentence().getIdInEssay());
                refreshTableView();
            }
        };
    }

    public static Callback<TableView<Mark>, TableRow<Mark>> getRowFactory() {
        return (TableView<Mark> tableView) -> {
            final TableRow<Mark> row = new TableRow<Mark>() {
                @Override
                protected void updateItem(Mark mark, boolean empty) {
                    super.updateItem(mark, empty);

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
                        if (row.getIndex() < tableData.size()) {
                            Mark currMark = tableData.get(row.getIndex());
                            TextAreaController.scrollTo(currMark.getSentence().getIdInEssay());
                            highlightText(tableData.get(row.getIndex()).getError().getSegment());
                        }

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
            tableData.addListener((ListChangeListener.Change<? extends Mark> change) -> {
                refreshTableView();
            });
            return row;
        };
    }

//    public static void dumpToExcel() {
//        final String[] colNames = MainScreen.getTableColNames();
//        try (PrintWriter w = new PrintWriter(new OutputStreamWriter(new FileOutputStream(excelFile.getPath()), "UTF-8"));) {
//            w.print("\uFEFF");
//            int i;
//            for (i = 0; i != colNames.length - 1; ++i) {
//                w.print(colNames[i] + ",");
//            }
//            w.println(colNames[i]);
//            for (Mark item : tableData) {
//                w.println(item.toString());
//            }
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setTitle("提示");
//            alert.setHeaderText(null);
//            alert.setContentText("已保存");
//            alert.showAndWait();
//        } catch (IOException e) {
////            e.printStackTrace();
//        }
//    }

    public static void highlightErrors(Sentence currSentence) {
        for (int i = 0; i != tableData.size(); ++i) {
            if (tableData.get(i).getSentence().equals(currSentence)) {
                highlightSeg(tableData.get(i).getError().getSegment(),
                        tableData.get(i).getError().getErrorTypes().get(0));
            }
        }
    }

    private static void highlightText(String segment) {
        MainScreen.getCurrEssay().selectRange(0, 0);
        int start = MainScreen.getCurrEssay().getText().indexOf(segment);
        if (start != -1) {
            MainScreen.getCurrEssay().selectRange(start, start + segment.length());
        }
    }

    private static void unhighlightSeg(String segment) {
        MainScreen.getCurrEssay().selectRange(0, 0);
        int start = MainScreen.getCurrEssay().getText().indexOf(segment);
        if (start != -1) {
            MainScreen.getCurrEssay().clearStyle(start, start + segment.length());
        }
    }

    private static void highlightSeg(String segment, String typeIError) {
        int start = MainScreen.getCurrEssay().getText().indexOf(segment);
        if (start == -1) {
            return;
        }
        String curr_style;
        switch (typeIError) {
            case "标点":
                curr_style = "punc";
                break;
            case "字":
                curr_style = "lett";
                break;
            case "词":
                curr_style = "word";
                break;
            case "句":
                curr_style = "sent";
                break;
            case "篇章及语用":
                curr_style = "para";
                break;
            default:
                curr_style = "";
                break;
        }

        StyleSpans<Collection<String>> curr_stylespan
                = MainScreen.getCurrEssay().getStyleSpans(start, start + segment.length());
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();

        for (int i = 0; i < curr_stylespan.getSpanCount(); ++i) {
            StyleSpan<Collection<String>> stylespan = curr_stylespan.getStyleSpan(i);
            Collection<String> new_style = new ArrayList<>(stylespan.getStyle());
            if (!new_style.contains(curr_style)) {
                new_style.add(curr_style);
            }
            spansBuilder.add(new_style, stylespan.getLength());
        }
        MainScreen.getCurrEssay().setStyleSpans(start, spansBuilder.create());
    }
}
