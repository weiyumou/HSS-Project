/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author weiyumou
 */
public class Error implements Serializable{

    private List<String> errorTypes;
    private String segment;
    private String modification;
    private String remark;

    public Error(List<String> errorTypes, String segment, String modification, String remark) {
        this.errorTypes = errorTypes;
        this.segment = segment;
        this.remark = remark;
        this.modification = modification;
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

    public String getModification() {
        return modification;
    }

    public void setModification(String modification) {
        this.modification = modification;
    }
    
    
    
    @Override
    public boolean equals(Object obj){
        if (obj == null) {
            return false;
        }
        if (!Error.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final Error other = (Error) obj;
        
        if ((this.getSegment() == null) ? (other.getSegment() != null) 
            : !this.getSegment().equals(other.getSegment())) {
            return false;
        }
        return !((this.getErrorTypes() == null) ? (other.getErrorTypes() != null) 
                : !this.getErrorTypes().equals(other.getErrorTypes()));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.errorTypes);
        hash = 89 * hash + Objects.hashCode(this.segment);
        return hash;
    }

    @Override
    public String toString() {
        String res = "";
        for(String error : errorTypes){
            res += error + ",";
        }
        return res + segment + "," + modification + "," + remark;
    }
    
   
}
