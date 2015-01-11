package com.cf.study.webservice.restful.restlet.resources;

import java.util.List;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.cf.study.webservice.restful.restlet.Adage;
import com.cf.study.webservice.restful.restlet.Adages;

public class PlainResource extends ServerResource {

    @Get
    public Representation toXml() {
        final List<Adage> list = Adages.getAdages();
        final StringBuilder sb = new StringBuilder();
        for (final Adage adage : list) {
            sb.append(adage.toString());
        }
        final StringRepresentation result = new StringRepresentation(sb.toString(), MediaType.TEXT_PLAIN);
        return result;
    }

}
