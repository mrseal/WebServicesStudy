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
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.namespace.QName;

import org.codehaus.jackson.map.ObjectMapper;

import com.cf.study.webservice.restful.jaxrs.Adage;

@Path("/")
public class Adages {

    private final String[] aphorisms = { "1. What can be shown cannot be said.", "2. If a lion could talk, we could not understand him.",
            "3. Philosophy is a battle against the bewitchment of our intelligence by means of language.", "4. Ambition is the death of thought.",
            "5. The limits of my language mean the limits of my world." };

    @GET
    @Path("/{id: \\d+}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getPlain(@PathParam("id") final int id) {
        return createAdage(id).toString();
    }

    @GET
    @Path("/{id: \\d+}")
    @Produces(MediaType.APPLICATION_XML)
    public JAXBElement<Adage> getXml(@PathParam("id") final int id) {
        return toXml(createAdage(id));
    }

    @GET
    @Path("/{id: \\d+}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson(@PathParam("id") final int id) {
        return toJson(createAdage(id));
    }

    @GET
    @Path("/response/{id: \\d+}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getResponsePlain(@PathParam("id") final int id) {
        return Response.ok(createAdage(id).toString(), "text/plain").build();
    }

    @GET
    @Path("/response/{id: \\d+}")
    @Produces(MediaType.APPLICATION_XML)
    public Response getResponseXml(@PathParam("id") final int id) {
        return Response.ok(createAdage(id), "application/xml").build();
    }

    @GET
    @Path("/response/{id: \\d+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getResponseJson(@PathParam("id") final int id) {
        return Response.ok(toJson(createAdage(id)), "application/json").build();
    }

    private Adage createAdage(final int index) {
        int key = index - 1;
        final Adage adage = new Adage();
        if (key == -1) {
            key = new Random().nextInt(aphorisms.length);
        }
        adage.setWords(aphorisms[key]);
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
