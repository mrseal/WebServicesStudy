package com.cf.study.webservice.restful.restlet.application;

import org.restlet.Application;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.routing.Router;

import com.cf.study.webservice.restful.restlet.Adage;
import com.cf.study.webservice.restful.restlet.Adages;
import com.cf.study.webservice.restful.restlet.resources.CreateResource;
import com.cf.study.webservice.restful.restlet.resources.PlainResource;
import com.cf.study.webservice.restful.restlet.resources.XmlAllResource;

public class AdagesApplication extends Application {

    @Override
    public synchronized Restlet createInboundRoot() {

        /*
         * Each resource could be written as an anonymous class encapsulated
         * inside this Application class; however, under Restlet best practices,
         * these resources should be implemented as relatively small, individual
         * classes such as PlainResource, XmlAllResource, etc as below.
         */

        // janitor is a resource as an anonymous class
        final Restlet janitor = new Restlet(getContext()) {
            @Override
            public void handle(final Request request, final Response response) {
                String msg = null;
                final String sid = (String) request.getAttributes().get("id");
                if (sid == null) {
                    msg = badRequest("No ID given.\n");
                }
                Integer id = null;
                try {
                    id = Integer.parseInt(sid.trim());
                } catch (final Exception e) {
                    msg = badRequest("Ill-formed ID.\n");
                }
                final Adage adage = Adages.find(id);
                if (adage == null) {
                    msg = badRequest("No adage with ID " + id + "\n");

                } else {
                    Adages.getAdages().remove(adage);
                    msg = "Adage " + id + " removed.\n";
                }
                response.setEntity(msg, MediaType.TEXT_PLAIN);
            }
        };

        final Router router = new Router(getContext());

        // Resource as an individual class
        router.attach("/", PlainResource.class);
        router.attach("/xml", XmlAllResource.class);
        // router.attach("/xml/{id}", XmlOneResource.class);
        // router.attach("/json", JsonAllResource.class);
        router.attach("/create", CreateResource.class);
        // router.attach("/update", UpdateResource.class);

        // Resource as an anonymous resource
        router.attach("/delete/{id}", janitor);

        return router;
    }

    private String badRequest(final String msg) {
        final Status error = new Status(Status.CLIENT_ERROR_BAD_REQUEST, msg);
        return error.toString();
    }

}
