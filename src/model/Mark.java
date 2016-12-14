/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author weiyumou
 */
public class Mark {
    private final SimpleStringProperty authorID;
    private final SimpleStringProperty idInEssay;
    private final SimpleStringProperty idInParagraph;
    private final SimpleStringProperty sentenceContent;
    private final SimpleStringProperty typeIError;
    private final SimpleStringProperty typeIIError;
    private final SimpleStringProperty typeIIIError;
    private final SimpleStringProperty errorSegment;
    private final SimpleStringProperty remark;
    
    private Sentence sentence;
    private Error error;

    public Mark(String authorID, Sentence sentence, Error error) {
        this.authorID = new SimpleStringProperty(authorID);
        this.idInEssay = new SimpleStringProperty(sentence.getIdInEssay());
        this.idInParagraph = new SimpleStringProperty(sentence.getIdInParagraph());
        this.sentenceContent = new SimpleStringProperty(sentence.getContent());
        this.typeIError = new SimpleStringProperty(error.getTypeI());
        this.typeIIError = new SimpleStringProperty(error.getTypeII());
        this.typeIIIError = new SimpleStringProperty(error.getTypeIII());
        this.errorSegment = new SimpleStringProperty(error.getSegment());
        this.remark = new SimpleStringProperty(error.getRemark());
    }

    public Sentence getSentence() {
        return sentence;
    }

    public void setSentence(Sentence sentence) {
        this.sentence = sentence;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public String getAuthorID() {
        return authorID.get();
    }

    public String getIdInEssay() {
        return idInEssay.get();
    }

    public String getIdInParagraph() {
        return idInParagraph.get();
    }

    public String getSentenceContent() {
        return sentenceContent.get();
    }

    public String getTypeIError() {
        return typeIError.get();
    }

    public String getTypeIIError() {
        return typeIIError.get();
    }

    public String getTypeIIIError() {
        return typeIIIError.get();
    }

    public String getErrorSegment() {
        return errorSegment.get();
    }

    public String getRemark() {
        return remark.get();
    }

    

//    @Override
//    public String toString() {
//        return "Mark{" + "authorID=" + authorID + ", idInEssay=" + idInEssay + ", idInParagraph=" + idInParagraph + ", sentenceContent=" + sentenceContent + ", typeIError=" + typeIError + ", typeIIError=" + typeIIError + ", typeIIIError=" + typeIIIError + ", errorSegment=" + errorSegment + ", remark=" + remark + '}';
//    }
    
    
}

