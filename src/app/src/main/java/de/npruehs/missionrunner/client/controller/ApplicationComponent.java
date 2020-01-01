package de.npruehs.missionrunner.client.controller;

import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import de.npruehs.missionrunner.client.controller.account.AccountComponent;
import de.npruehs.missionrunner.client.controller.account.AccountModule;
import de.npruehs.missionrunner.client.controller.mission.MissionComponent;
import de.npruehs.missionrunner.client.controller.mission.MissionModule;
import retrofit2.Retrofit;

@Singleton
@Component(modules = { AccountModule.class, MissionModule.class })
public interface ApplicationComponent {
    AccountComponent.Factory accountComponent();
    MissionComponent.Factory missionComponent();

    @Component.Builder
    interface Builder {
        ApplicationComponent build();

        @BindsInstance
        Builder application(Application application);

        @BindsInstance
        Builder retrofit(Retrofit retrofit);
    }
}
