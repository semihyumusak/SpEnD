/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit;

import java.io.IOException;

/**
 * An object which handles the actual communication portion of page retrieval/submission.
 *
 * @version $Revision: 8931 $
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Daniel Gredler
 * @author Marc Guillemot
 */
public interface WebConnection {

    /**
     * Submits a request and retrieves a response.
     * @param request the request
     * @return the response to the request defined by the specified request
     * @exception IOException if an IO error occurs
     */
    WebResponse getResponse(final WebRequest request) throws IOException;

}
