/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author weiyumou
 */
public class Sentence {
    private String idInEssay;
    private String idInParagraph;
    private String content;

    public Sentence(String idInEssay, String idInParagraph, String content) {
        this.idInEssay = idInEssay;
        this.idInParagraph = idInParagraph;
        this.content = content;
    }

    public String getIdInEssay() {
        return idInEssay;
    }

    public void setIdInEssay(String idInEssay) {
        this.idInEssay = idInEssay;
    }

    public String getIdInParagraph() {
        return idInParagraph;
    }

    public void setIdInParagraph(String idInParagraph) {
        this.idInParagraph = idInParagraph;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    
    
    
}
