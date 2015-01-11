package com.cf.study.webservice.restful.restlet.resources;

import java.util.List;

import org.restlet.data.MediaType;
import org.restlet.ext.xml.DomRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.cf.study.webservice.restful.restlet.Adage;
import com.cf.study.webservice.restful.restlet.Adages;

public class XmlAllResource extends ServerResource {

    @Get
    public Representation toXml() {
        final List<Adage> list = Adages.getAdages();
        DomRepresentation dom = null;
        try {
            dom = new DomRepresentation(MediaType.TEXT_XML);
            dom.setIndenting(true);
            final Document doc = dom.getDocument();
            final Element root = doc.createElement("adages");
            for (final Adage adage : list) {
                final Element next = doc.createElement("adage");
                next.appendChild(doc.createTextNode(adage.toString()));
                root.appendChild(next);
            }
            doc.appendChild(root);
        } catch (final Exception e) {
        }
        return dom;
    }
}
