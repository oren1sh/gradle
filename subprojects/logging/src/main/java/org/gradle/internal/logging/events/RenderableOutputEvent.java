/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.internal.logging.events;

import org.gradle.api.Nullable;
import org.gradle.api.logging.LogLevel;
import org.gradle.internal.logging.text.StyledTextOutput;

public abstract class RenderableOutputEvent extends CategorisedOutputEvent {
    private final OperationIdentifier operationId;

    protected RenderableOutputEvent(long timestamp, String category, LogLevel logLevel, OperationIdentifier operationId) {
        super(timestamp, category, logLevel);
        this.operationId = operationId;
    }

    /**
     * Renders this event to the given output. The output's style will be set to {@link
     * StyledTextOutput.Style#Normal}. The style will be reset after the rendering is complete, so
     * there is no need for this method to clean up the style.
     *
     * @param output The output to render to.
     */
    public abstract void render(StyledTextOutput output);

    @Nullable
    public OperationIdentifier getOperationId() {
        return operationId;
    }
}
