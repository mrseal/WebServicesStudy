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
package com.cf.study.webservice.restful.httpservlet.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.http.HTTPException;

import org.json.JSONObject;
import org.json.XML;

import com.cf.study.webservice.restful.httpservlet.Prediction;
import com.cf.study.webservice.restful.httpservlet.PredictionRepository;
import com.cf.study.webservice.restful.httpservlet.util.SerializationUtility;

@WebServlet("/Prediction")
public class PredictionServlet extends HttpServlet {

    private static final long serialVersionUID = -3568946092246651984L;
    private static final String ID_PARAM = "id";
    private static final String WHO_PARAM = "who";
    private static final String WHAT_PARAM = "what";
    private static final String FORMAT_ACCEPT_PARAM = "accept";

    private PredictionRepository predictionRepository;

    @Override
    public void init() {
        predictionRepository = new PredictionRepository();
        predictionRepository.setServletContext(getServletContext());
    }

    @Override
    public void doPost(final HttpServletRequest request, final HttpServletResponse response) {
        final String who = request.getParameter(WHO_PARAM);
        final String what = request.getParameter(WHAT_PARAM);
        if (who == null || what == null) {
            throw new HTTPException(HttpServletResponse.SC_BAD_REQUEST);
        }
        final Prediction p = new Prediction();
        p.setWho(who);
        p.setWhat(what);
        final int id = predictionRepository.addPrediction(p);
        final String message = "Prediction " + id + " created.\n";
        sendResponse(response, SerializationUtility.toXML(message), false);
    }

    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response) {
        final String id = request.getParameter(ID_PARAM);
        final Integer key = id == null ? null : new Integer(id.trim());
        final boolean json = acceptJson(request);
        if (key == null) {
            final ConcurrentMap<Integer, Prediction> map = predictionRepository.getPredictions();
            final Object[] list = map.values().toArray();
            Arrays.sort(list);
            final String xml = SerializationUtility.toXML(list);
            sendResponse(response, xml, json);
        } else {
            final Prediction prediction = predictionRepository.getPredictions().get(key);
            if (prediction == null) {
                final String msg = "Prediction with id=" + key + " does not exist!\n";
                sendResponse(response, SerializationUtility.toXML(msg), false);
            } else {
                sendResponse(response, SerializationUtility.toXML(prediction), json);
            }
        }
    }

    @Override
    public void doPut(final HttpServletRequest request, final HttpServletResponse response) {
        String key = null;
        String rest = null;
        boolean who = false;
        try {
            final BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
            final String data = br.readLine();
            // The data should looks like: id=33#who=Homer Allision
            final String[] args = data.split("#");
            final String[] parts1 = args[0].split("=");
            key = parts1[1];
            final String[] parts2 = args[1].split("=");
            if (parts2[0].contains("who")) {
                who = true;
            }
            rest = parts2[1];
        } catch (final Exception e) {
            throw new HTTPException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        if (key == null) {
            throw new HTTPException(HttpServletResponse.SC_BAD_REQUEST);
        }
        final Prediction p = predictionRepository.getPredictions().get(new Integer(key.trim()));
        if (p == null) {
            final String msg = key + " does not map to a Prediction.\n";
            sendResponse(response, SerializationUtility.toXML(msg), false);
        } else {
            if (rest == null) {
                throw new HTTPException(HttpServletResponse.SC_BAD_REQUEST);
            } else {
                if (who) {
                    p.setWho(rest);
                } else {
                    p.setWhat(rest);
                }
                final String msg = "Prediction " + key + " has been edited.\n";
                sendResponse(response, SerializationUtility.toXML(msg), false);
            }
        }
    }

    @Override
    public void doDelete(final HttpServletRequest request, final HttpServletResponse response) {
        final String id = request.getParameter(ID_PARAM);
        final Integer key = id == null ? null : new Integer(id.trim());
        if (key == null) {
            throw new HTTPException(HttpServletResponse.SC_BAD_REQUEST);
        }
        try {
            predictionRepository.getPredictions().remove(key);
            final String msg = "Prediction " + key + " removed.\n";
            sendResponse(response, SerializationUtility.toXML(msg), false);
        } catch (final Exception e) {
            throw new HTTPException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void doTrace(final HttpServletRequest request, final HttpServletResponse response) {
        throw new HTTPException(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    @Override
    public void doHead(final HttpServletRequest request, final HttpServletResponse response) {
        throw new HTTPException(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    @Override
    public void doOptions(final HttpServletRequest request, final HttpServletResponse response) {
        throw new HTTPException(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    private boolean acceptJson(final HttpServletRequest request) {
        boolean json = false;
        final String accept = request.getHeader(FORMAT_ACCEPT_PARAM);
        if (accept != null && accept.contains("json")) {
            json = true;
        }
        return json;
    }

    private void sendResponse(final HttpServletResponse response, String payload, final boolean json) {
        if (json) {
            final JSONObject jsonObject = XML.toJSONObject(payload);
            payload = jsonObject.toString(3);
            // 3 is indentation level for nice look
        }
        try {
            final OutputStream out = response.getOutputStream();
            out.write(payload.getBytes());
            out.flush();
        } catch (final IOException e) {
            throw new HTTPException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
