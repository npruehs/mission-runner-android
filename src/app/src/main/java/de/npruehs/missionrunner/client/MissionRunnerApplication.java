package de.npruehs.missionrunner.client;

import android.app.Application;

import de.npruehs.missionrunner.client.controller.ApplicationComponent;
import de.npruehs.missionrunner.client.controller.DaggerApplicationComponent;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MissionRunnerApplication extends Application {
    ApplicationComponent appComponent = DaggerApplicationComponent
            .builder()
            .application(this)
            .retrofit(new Retrofit.Builder().baseUrl("http://192.168.178.27:8080/").addConverterFactory(GsonConverterFactory.create()).build())
            .build();
}
