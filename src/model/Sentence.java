/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;

/**
 *
 * @author weiyumou
 */
public class Sentence implements Serializable{
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

    @Override
    public String toString() {
        return "[" + idInEssay + ", " + idInParagraph + "]" + content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
    
    @Override
    public boolean equals(Object obj){
        if (obj == null) {
            return false;
        }
        if (!Sentence.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final Sentence other = (Sentence) obj;
        
        if ((this.getIdInEssay() == null) ? (other.getIdInEssay() != null) 
            : !this.getIdInEssay().equals(other.getIdInEssay())) {
            return false;
        }
        if ((this.getIdInParagraph() == null) ? (other.getIdInParagraph() != null) 
            : !this.getIdInParagraph().equals(other.getIdInParagraph())) {
            return false;
        }
        if ((this.getContent() == null) ? (other.getContent() != null) 
            : !this.getContent().equals(other.getContent())) {
            return false;
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.getIdInEssay() != null ? this.getIdInEssay().hashCode() : 0);
        hash = 53 * hash + (this.getIdInEssay() != null ? this.getIdInEssay().hashCode() : 0);
        hash = 53 * hash + (this.getContent() != null ? this.getContent().hashCode() : 0);
        return hash;
    }
}
