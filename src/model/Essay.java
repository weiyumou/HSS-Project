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
    private int numOfSentences;

    public Essay(List<String> lines) {
        this.title = lines.get(0);
        this.authorID = lines.get(1);
        
        this.paragraphs = new ArrayList<>();
        numOfSentences = 0;
        for(int i = 2; i != lines.size(); ++i){
            Paragraph para = new Paragraph(lines.get(i), numOfSentences);
            this.paragraphs.add(para);
            numOfSentences += para.getSentences().size();
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
    
    public String getSegment(int from){
        return getSegment(from, numOfSentences + 1);
    }
    
    public String getSegment(int from, int to){
        int count = 0;
        String segment = "\t";
        
        for(Paragraph para : paragraphs){
            for(Sentence sentence : para.getSentences()){
                ++count;
                if(count >= from && count < to){
                    segment += sentence.getContent();
                }else if(count >= to){
                    return segment;
                }
            }
            if(!segment.equals("\t")){
                segment += "\n\t";
            }
        }
        return segment;
    }
    
    public String getAugmentedSegment(int from, int to){
        int count = 0;
        String segment = "\t";
        
        for(Paragraph para : paragraphs){
            for(Sentence sentence : para.getSentences()){
                ++count;
                if(count >= from && count < to){
                    segment += sentence.toString();
                }else if(count >= to){
                    return segment;
                }
            }
            if(!segment.equals("\t")){
                segment += "\n\t";
            }
        }
        return segment;
    }

    public int getNumOfSentences() {
        return numOfSentences;
    }
    
    
}
