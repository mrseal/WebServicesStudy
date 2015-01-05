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
package com.cf.study.webservice.restful.jaxrs.resources;

import java.io.IOException;
import java.util.Random;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.namespace.QName;

import org.codehaus.jackson.map.ObjectMapper;

import com.cf.study.webservice.restful.jaxrs.Adage;

@Path("/")
public class Adages {

    private final String[] aphorisms = { "What can be shown cannot be said.", "If a lion could talk, we could not understand him.",
            "Philosophy is a battle against the bewitchment of our intelligence by means of language.", "Ambition is the death of thought.",
    "The limits of my language mean the limits of my world." };

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getPlain() {
        return createAdage().toString();
    }

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public JAXBElement<Adage> getXml() {
        return toXml(createAdage());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        return toJson(createAdage());
    }

    private Adage createAdage() {
        final Adage adage = new Adage();
        adage.setWords(aphorisms[new Random().nextInt(aphorisms.length)]);
        return adage;
    }

    @XmlElementDecl(namespace = "http://aphorism.adage", name = "adage")
    private JAXBElement<Adage> toXml(final Adage adage) {
        return new JAXBElement<Adage>(new QName("adage"), Adage.class, adage);
    }

    private String toJson(final Adage adage) {
        String json = "ERROR: Failed to get the required resource.";
        try {
            json = new ObjectMapper().writeValueAsString(adage);
        } catch (final IOException e) {
        }
        return json;
    }

}
