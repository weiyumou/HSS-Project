/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author weiyumou
 */
public class Essay {
    private String title;
    private String authorID;
    private List<Paragraph> paragraphs;

    public Essay(List<String> lines) {
        this.title = lines.get(0);
        this.authorID = lines.get(1);
        
        this.paragraphs = new ArrayList<>();
        int numPrecSentences = 0;
        for(int i = 2; i != lines.size(); ++i){
            Paragraph para = new Paragraph(lines.get(i), numPrecSentences);
            this.paragraphs.add(para);
            numPrecSentences += para.getSentences().size();
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorID() {
        return authorID;
    }

    public void setAuthorID(String authorID) {
        this.authorID = authorID;
    }

    public List<Paragraph> getParagraphs() {
        return paragraphs;
    }

    public void setParagraphs(List<Paragraph> paragraphs) {
        this.paragraphs = paragraphs;
    }
    
    @Override
    public String toString(){
        String res = "";
        
        for(Paragraph para : paragraphs){
            for(Sentence sentence : para.getSentences()){
                res += sentence.toString() + "\n";
            }
        }
        
        return res;
    }
}
