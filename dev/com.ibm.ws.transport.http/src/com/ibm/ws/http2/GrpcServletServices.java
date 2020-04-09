/*******************************************************************************
 * Copyright (c) 2020 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ibm.ws.http2;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Keep track of gRPC service to application mappings
 */
public class GrpcServletServices {

    private static final Map<String, ServiceInformation> servletGrpcServices = new ConcurrentHashMap<String, ServiceInformation>();

    /**
     * Register an a gRPC service with its application
     *
     * @param String gRPC service name
     * @param String contextRoot for the app
     */
    public static void addServletGrpcService(String service, String contextRoot, Class<?> clazz) {
        if (servletGrpcServices.containsKey(service)) {
            throw new RuntimeException("duplicate gRPC service added: " + service);
        } else {
            servletGrpcServices.put(service, new ServiceInformation(contextRoot, clazz));
        }
    }

    /**
     * Remove a service mapping
     *
     * @param String service
     */
    public static void removeServletGrpcService(String service) {
        if (servletGrpcServices != null) {
            servletGrpcServices.remove(service);
        }
        if (servletGrpcServices.isEmpty()) {
            servletGrpcServices.clear();
        }
    }

    /**
     * Return the set of servlet gRPC Services that have been registered.
     *
     * @return HashMap<String service, String contextRoot> or null if the set is empty
     */
    public static Map<String, ServiceInformation> getServletGrpcServices() {
        if (servletGrpcServices == null) {
            return null;
        }
        return servletGrpcServices;
    }

    public static void destroy() {
        servletGrpcServices.clear();
    }

    public static class ServiceInformation {
        String contextRoot;
        Class<?> clazz;

        ServiceInformation(String root, Class<?> c) {
            contextRoot = root;
            clazz = c;
        }

        public String getContextRoot() {
            return contextRoot;
        }

        public Class<?> getServiceClass() {
            return clazz;
        }
    }
}
