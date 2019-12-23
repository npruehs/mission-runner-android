package de.npruehs.missionrunner.client;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ApplicationExecutors {
    private Executor IO;

    private Executor main;

    @Inject
    public ApplicationExecutors() {
        IO = Executors.newSingleThreadExecutor();
        main = new MainThreadExecutor();
    }

    public Executor IO() {
        return IO;
    }

    public Executor main() {
        return main;
    }

    private class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
