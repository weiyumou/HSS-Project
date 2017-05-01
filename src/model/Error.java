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

    private static final int NO_ERROR = 3;
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
    
    public String getLowestError(){
        return errorTypes.get(errorTypes.size() - 1);
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
        this.remark = remark.replace("\n", "");
    }

    public String getModification() {
        return modification;
    }

    public void setModification(String modification) {
        this.modification = modification.replace("\n", "");
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.errorTypes);
        hash = 89 * hash + Objects.hashCode(this.segment);
        hash = 89 * hash + Objects.hashCode(this.modification);
        hash = 89 * hash + Objects.hashCode(this.remark);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Error other = (Error) obj;
        if (!Objects.equals(this.segment, other.segment)) {
            return false;
        }
        if (!Objects.equals(this.modification, other.modification)) {
            return false;
        }
        if (!Objects.equals(this.remark, other.remark)) {
            return false;
        }
        if (!Objects.equals(this.errorTypes, other.errorTypes)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String res = "";
        int count = 0;
        for(String error : errorTypes){
            res += error + ",";
            ++count;
        }
        while(count < NO_ERROR){
            res += ",";
            ++count;
        }
        String ret = segment + "," + res + modification + "," + remark;
        return ret;
    }
}
