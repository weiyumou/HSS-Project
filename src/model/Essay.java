/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author weiyumou
 */
public class Essay implements Serializable{
    private String title;
    private String authorID;
    private String background = "";
    private List<Paragraph> paragraphs;
    private int numOfSentences;
    private List<Mark> marks;

    public Essay(List<String> lines) {
        int index = 0;
        while(index < lines.size() && lines.get(index).equals("\n"))
            ++index;
        
        this.authorID = lines.get(index).substring(lines.get(index).indexOf(':') + 1);

        ++index;
        while(index < lines.size() && lines.get(index).isEmpty())
            ++index;
        
        ++index;
        while(index < lines.size() && !lines.get(index).isEmpty()){
            this.background += lines.get(index) + "\n";
            ++index;
        }
        
        while(index < lines.size() && lines.get(index).isEmpty())
            ++index;
        
        this.title = lines.get(index).substring(lines.get(index).indexOf(':') + 1);
        index += 2;
        
        this.paragraphs = new ArrayList<>();
        numOfSentences = 0;
        while(index < lines.size()){
            if (!lines.get(index).isEmpty()) {
                Paragraph para = new Paragraph(lines.get(index), numOfSentences);
                this.paragraphs.add(para);
                numOfSentences += para.getSentences().size();
            }
            ++index;
        }
        
        this.marks = new ArrayList<>();
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

    public List<Mark> getMarks() {
        return marks;
    }

    public void setMarks(List<Mark> marks) {
        this.marks = marks;
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
        int count = 0, para_count = 0;
        String segment = "";
        
        for(Paragraph para : paragraphs){
            ++para_count;
            String temp_before = segment;
            segment += "\t[" + para_count + "]";
            String temp_after = segment;
            
            for(Sentence sentence : para.getSentences()){
                ++count;
                if(count >= from && count < to){
                    segment += sentence.getContent();
                }else if(count >= to){
                    return segment;
                }
            }
            if(segment.equals(temp_after)){
                segment = temp_before;
            }else{
                segment += "\n";
            }            
        }
        if(segment.equals("\t[1]")){
            segment = "";
        }
        return segment;
    }
    
    public Sentence getSingleSentence(int index){
        int count = 0;
        
        for(Paragraph para : paragraphs){
            for(Sentence sentence : para.getSentences()){
                ++count;
                if(count == index){
                    return sentence;
                }
            }
        }
        return null;
    }

    public int getNumOfSentences() {
        return numOfSentences;
    }

    public String getBackground() {
        return background;
    }
    
    
}
