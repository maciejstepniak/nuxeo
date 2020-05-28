/*
 * (C) Copyright 2020 Nuxeo (http://nuxeo.com/) and others.
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
 *     Nour AL KOTOB
 */
package org.nuxeo.ecm.platform.task.core.listener;

import static org.nuxeo.ecm.core.query.sql.NXQL.ECM_UUID;
import static org.nuxeo.ecm.platform.routing.api.DocumentRoutingConstants.DOCUMENT_ROUTE_DOCUMENT_TYPE;
import static org.nuxeo.ecm.platform.task.TaskConstants.TASK_PROCESS_ID_PROPERTY_NAME;

import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.event.DocumentEventTypes;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventListener;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;

/**
 *
 * @since 11.1
 */
public class DeleteTaskForDeletedDocumentRouteListener implements EventListener {

    protected static final String QUERY_GET_TASKS_RELATED_TO_DOCUMENT_ROUTE = "SELECT * FROM Document WHERE %s = '%s'";

    @Override
    public void handleEvent(Event event) {
        if (DocumentEventTypes.ABOUT_TO_REMOVE.equals(event.getName())) {
            DocumentEventContext context = (DocumentEventContext) event.getContext();
            DocumentModel docModel = context.getSourceDocument();
            CoreSession session = context.getCoreSession();
            if (docModel.getType().equals(DOCUMENT_ROUTE_DOCUMENT_TYPE)) {
                deleteOrphanTasks(session, docModel.getId());
            }
        }
    }

    /**
     * Deletes all tasks which processId matches the given id.
     *
     * @param session the session used to query.
     * @param id the deleted DocumentRoute id
     * @since 11.1
     */
    private void deleteOrphanTasks(CoreSession session, String id) {
        String query = String.format(QUERY_GET_TASKS_RELATED_TO_DOCUMENT_ROUTE, TASK_PROCESS_ID_PROPERTY_NAME, id);
        session.queryProjection(query, 0, 0)
               .stream()
               .map(m -> m.get(ECM_UUID))
               .forEach(s -> session.removeDocument(new IdRef((String) s)));
    }

}
