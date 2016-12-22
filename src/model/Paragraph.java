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
public class Paragraph implements Serializable{
    private String content;
    private List<Sentence> sentences;
    private int numPrecedingSentences;

    public Paragraph(String content, int numPrecedingSentences) {
        this.content = content;
        this.numPrecedingSentences = numPrecedingSentences;
        generateSentences();
    }
    
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Sentence> getSentences() {
        return sentences;
    }

    public void setSentences(List<Sentence> sentences) {
        this.sentences = sentences;
    }

    private void generateSentences(){
        sentences = new ArrayList<>();
        int count = 0, prev = 0;
        for(int i = 0; i != content.length(); ++i){
            switch (content.charAt(i)) {
                case '。':
                case '！':
                case '？':
                    ++count;
                    sentences.add(new Sentence(numPrecedingSentences + count, 
                        count, content.substring(prev, i + 1).replace("\t", "")));
                    prev = i + 1;
                    break;
                default:
                    break;
            }
        }
    }
}
