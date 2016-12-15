/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author weiyumou
 */
public class Error implements Serializable{
//    private String typeI;
//    private String typeII;
//    private String typeIII;
    
    private List<String> errorTypes;
    private String segment;
    private String remark;

    public Error(List<String> errorTypes, String segment, String remark) {
        this.errorTypes = errorTypes;
        this.segment = segment;
        this.remark = remark;
    }

    public List<String> getErrorTypes() {
        return errorTypes;
    }

    public void setErrorTypes(List<String> errorTypes) {
        this.errorTypes = errorTypes;
    }

    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    @Override
    public boolean equals(Object obj){
        if (obj == null) {
            return false;
        }
        if (!Sentence.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final Error other = (Error) obj;
        
        if ((this.getSegment() == null) ? (other.getSegment() != null) 
            : !this.getSegment().equals(other.getSegment())) {
            return false;
        }
        if ((this.getErrorTypes() == null) ? (other.getErrorTypes() != null) 
            : !this.getErrorTypes().equals(other.getErrorTypes())) {
            return false;
        }
        
        return true;
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.getSegment() != null ? this.getSegment().hashCode() : 0);
        hash = 53 * hash + (this.getErrorTypes() != null ? this.getErrorTypes().hashCode() : 0);
        return hash;
    }
}
