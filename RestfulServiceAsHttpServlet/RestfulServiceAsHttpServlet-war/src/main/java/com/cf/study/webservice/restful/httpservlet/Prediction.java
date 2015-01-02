/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2015
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.cf.study.webservice.restful.httpservlet;

import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;

public class Prediction implements Serializable, Comparable<Prediction> {

    private static final long serialVersionUID = 5323134997264820602L;

    private String who;
    private String what;
    private int id;

    public String getWho() {
        return who;
    }

    public void setWho(final String who) {
        this.who = who;
    }

    public String getWhat() {
        return what;
    }

    public void setWhat(final String what) {
        this.what = what;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int compareTo(final Prediction other) {
        return this.id - other.id;
    }

    public String toXML() {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final XMLEncoder encoder = new XMLEncoder(out);
        encoder.writeObject(this);
        encoder.close();
        return out.toString();
    }

}
