/*
 * (C) Copyright 2006-2011 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     Gagnavarslan ehf
 */
package org.nuxeo.ecm.webdav.backend;

import javax.servlet.http.HttpServletRequest;

import org.nuxeo.ecm.webdav.service.WebDavService;

public class BackendHelper {

    /**
     * For tests. Otherwise the factory is configured through an extension point in the component.
     */
    public static void setBackendFactory(BackendFactory backendFactory) {
        WebDavService.instance().setBackendFactory(backendFactory);
    }

    public static Backend getBackend(String path, HttpServletRequest request) {
        return WebDavService.instance().getBackendFactory().getBackend(path, request);
    }

}
