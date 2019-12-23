package de.npruehs.missionrunner.client.controller;

import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = AccountModule.class)
public interface ApplicationComponent {
    AccountComponent.Factory accountComponent();

    @Component.Builder
    interface Builder {
        ApplicationComponent build();

        @BindsInstance
        Builder application(Application application);
    }
}
