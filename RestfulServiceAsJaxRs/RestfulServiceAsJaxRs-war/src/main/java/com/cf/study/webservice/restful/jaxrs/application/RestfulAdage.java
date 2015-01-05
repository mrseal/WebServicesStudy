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
package com.cf.study.webservice.restful.jaxrs.application;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/adage")
public class RestfulAdage extends Application {

    /* The class is left empty on purpose. Empty class means that whole classpath is scanned for all REST resources. */

    //    @Override
    //    public Set<Class<?>> getClasses() {
    //        final Set<Class<?>> set = new HashSet<Class<?>>();
    //        set.add(Adges.class);
    //        return set;
    //    }

}
