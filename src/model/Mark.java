/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Objects;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author weiyumou
 */
public class Mark implements Serializable {

    private Sentence sentence;
    private Error error;
    private String authorID;

    public Mark(String authorID, Sentence sentence, Error error) {
        this.authorID = authorID;
        this.sentence = sentence;
        this.error = error;
    }

    public SimpleStringProperty authoridProperty() {
        return new SimpleStringProperty(authorID);
    }
    
    public SimpleStringProperty idInEssayProperty() {
        
        return new SimpleStringProperty(String.valueOf(sentence.getIdInEssay()));
    }

    public SimpleStringProperty idInParagraphProperty() {
        return new SimpleStringProperty(String.valueOf(sentence.getIdInParagraph()));
    }

    public SimpleStringProperty sentenceContentProperty() {
        return new SimpleStringProperty(sentence.getContent());
    }

    public SimpleStringProperty typeIErrorProperty() {
        String err;
        try {
            err = error.getErrorTypes().get(0);
        } catch (IndexOutOfBoundsException e) {
            err = "";
        }
        return new SimpleStringProperty(err);
    }

    public SimpleStringProperty typeIIErrorProperty() {
        String err;
        try {
            err = error.getErrorTypes().get(1);
        } catch (IndexOutOfBoundsException e) {
            err = "";
        }
        return new SimpleStringProperty(err);
    }

    public SimpleStringProperty typeIIIErrorProperty() {
        String err;
        try {
            err = error.getErrorTypes().get(2);
        } catch (IndexOutOfBoundsException e) {
            err = "";
        }
        return new SimpleStringProperty(err);
    }

    public SimpleStringProperty errorSegmentProperty() {
        return new SimpleStringProperty(error.getSegment());
    }

    public SimpleStringProperty remarkProperty() {
        return new SimpleStringProperty(error.getRemark());
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
        return authorID;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!Mark.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final Mark other = (Mark) obj;

        if ((this.getAuthorID() == null) ? (other.getAuthorID() != null)
                : !this.getAuthorID().equals(other.getAuthorID())) {
            return false;
        }
        if ((this.getSentence() == null) ? (other.getSentence() != null)
                : !this.getSentence().equals(other.getSentence())) {
            return false;
        }
        return !((this.getError() == null) ? (other.getError() != null)
                : !this.getError().equals(other.getError()));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + Objects.hashCode(this.sentence);
        hash = 73 * hash + Objects.hashCode(this.error);
        hash = 73 * hash + Objects.hashCode(this.authorID);
        return hash;
    }

    @Override
    public String toString() {
        return authorID + "," + sentence.getRepString() + "," + error.toString();
    }
    
}
