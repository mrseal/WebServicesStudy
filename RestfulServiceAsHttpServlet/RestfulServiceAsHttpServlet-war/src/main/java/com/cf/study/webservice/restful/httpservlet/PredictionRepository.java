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

import java.io.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PredictionRepository {

    private static final String PREDICTION_DB_SOURCE = "/WEB-INF/data/predictions.db";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ConcurrentMap<Integer, Prediction> predictions;
    private ServletContext servletContext;
    private AtomicInteger mapKey;

    public PredictionRepository() {
        predictions = new ConcurrentHashMap<Integer, Prediction>();
        mapKey = new AtomicInteger();
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public void setServletContext(final ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public int addPrediction(final Prediction p) {
        final int id = mapKey.incrementAndGet();
        p.setId(id);
        predictions.put(id, p);
        return id;
    }

    public ConcurrentMap<Integer, Prediction> getPredictions() {
        if (getServletContext() == null) {
            return null;
        }
        if (predictions.size() < 1) {
            populate();
        }
        return predictions;
    }

    private void populate() {
        final InputStream in = servletContext.getResourceAsStream(PREDICTION_DB_SOURCE);
        if (in != null) {
            try {
                final InputStreamReader isr = new InputStreamReader(in);
                final BufferedReader reader = new BufferedReader(isr);
                String record = null;
                while ((record = reader.readLine()) != null) {
                    final String[] parts = record.split("!");
                    final Prediction p = new Prediction();
                    p.setWho(parts[0]);
                    p.setWhat(parts[1]);
                    addPrediction(p);
                }
            } catch (final IOException e) {
                logger.error("Failed to populate Prediction repository from {}. Details: {}", PREDICTION_DB_SOURCE, e);
            }
        }
    }

}
