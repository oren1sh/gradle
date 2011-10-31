/*
 * Copyright 2011 the original author or authors.
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

package org.gradle.api.internal.tasks.testing.verbosity;

import org.gradle.api.tasks.testing.TestVerbosity;

/**
 * by Szczepan Faber, created at: 10/31/11
 */
public class TestVerbosityImpl implements TestVerbosity {

    boolean showStandardStream;

    public boolean getShowStandardStream() {
        return showStandardStream;
    }

    public TestVerbosity setShowStandardStream(boolean showStandardStream) {
        this.showStandardStream = showStandardStream;
        return this;
    }
}
