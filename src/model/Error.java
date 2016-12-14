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
public class Error {
    private String typeI;
    private String typeII;
    private String typeIII;
    private String segment;
    private String remark;

    public Error(String typeI, String typeII, String typeIII, String segment, String remark) {
        this.typeI = typeI;
        this.typeII = typeII;
        this.typeIII = typeIII;
        this.segment = segment;
        this.remark = remark;
    }

    public String getTypeI() {
        return typeI;
    }

    public void setTypeI(String typeI) {
        this.typeI = typeI;
    }

    public String getTypeII() {
        return typeII;
    }

    public void setTypeII(String typeII) {
        this.typeII = typeII;
    }

    public String getTypeIII() {
        return typeIII;
    }

    public void setTypeIII(String typeIII) {
        this.typeIII = typeIII;
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
}
