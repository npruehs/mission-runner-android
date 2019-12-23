package de.npruehs.missionrunner.client;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ApplicationExecutors {
    private Executor IO;

    @Inject
    public ApplicationExecutors() {
        IO = Executors.newSingleThreadExecutor();
    }

    public Executor IO() {
        return IO;
    }
}
