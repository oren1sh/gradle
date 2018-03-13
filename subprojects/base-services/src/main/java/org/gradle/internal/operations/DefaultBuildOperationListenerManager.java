/*
 * Copyright 2018 the original author or authors.
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

package org.gradle.internal.operations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@SuppressWarnings("WhileLoopReplaceableByForEach") // necessary for thread safety
public class DefaultBuildOperationListenerManager implements BuildOperationListenerManager {

    private final Lock listenersLock = new ReentrantLock();

    // This cannot be CopyOnWriteArrayList because we need to iterate it in reverse,
    // which requires atomically getting an iterator and the size.
    // Moreover, we iterate this list far more often that we mutate,
    // making a (albeit home grown) copy-on-write strategy more appealing.
    private List<BuildOperationListener> listeners = Collections.emptyList();

    private final BuildOperationListener broadcaster = new BuildOperationListener() {
        @Override
        public void started(BuildOperationDescriptor buildOperation, OperationStartEvent startEvent) {
            Iterator<BuildOperationListener> iterator = listeners.iterator();
            while (iterator.hasNext()) {
                iterator.next().started(buildOperation, startEvent);
            }
        }

        @Override
        public void progress(OperationIdentifier operationIdentifier, OperationProgressEvent progressEvent) {
            Iterator<BuildOperationListener> iterator = listeners.iterator();
            while (iterator.hasNext()) {
                iterator.next().progress(operationIdentifier, progressEvent);
            }
        }

        @Override
        public void finished(BuildOperationDescriptor buildOperation, OperationFinishEvent finishEvent) {
            List<BuildOperationListener> listeners = DefaultBuildOperationListenerManager.this.listeners;
            ListIterator<BuildOperationListener> iterator = listeners.listIterator(listeners.size());
            while (iterator.hasPrevious()) {
                iterator.previous().finished(buildOperation, finishEvent);
            }
        }
    };

    @Override
    public void addListener(BuildOperationListener listener) {
        listenersLock.lock();
        try {
            List<BuildOperationListener> listeners = new ArrayList<BuildOperationListener>(this.listeners);
            listeners.add(listener);
            this.listeners = listeners;
        } finally {
            listenersLock.unlock();
        }
    }

    @Override
    public void removeListener(BuildOperationListener listener) {
        listenersLock.lock();
        try {
            List<BuildOperationListener> listeners = new ArrayList<BuildOperationListener>(this.listeners);
            listeners.remove(listener);
            this.listeners = listeners;
        } finally {
            listenersLock.unlock();
        }
    }

    @Override
    public BuildOperationListener getBroadcaster() {
        return broadcaster;
    }

}
