package de.npruehs.missionrunner.client;

import android.app.Application;

import de.npruehs.missionrunner.client.controller.ApplicationComponent;
import de.npruehs.missionrunner.client.controller.DaggerApplicationComponent;

public class MissionRunnerApplication extends Application {
    ApplicationComponent appComponent = DaggerApplicationComponent.builder().application(this).build();
}
