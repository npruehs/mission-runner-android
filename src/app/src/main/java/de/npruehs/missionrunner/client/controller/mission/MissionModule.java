package de.npruehs.missionrunner.client.controller.mission;

import android.app.Application;

import androidx.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.npruehs.missionrunner.client.model.mission.MissionDao;
import de.npruehs.missionrunner.client.model.mission.MissionDatabase;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(subcomponents = MissionComponent.class)
public class MissionModule {
    @Singleton
    @Provides
    public static MissionService provideMissionService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.178.27:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(MissionService.class);
    }

    @Singleton
    @Provides
    public static MissionDatabase provideMissionDatabase(Application application) {
        return Room.databaseBuilder(application, MissionDatabase.class, "missions.db")
                .fallbackToDestructiveMigration().build();
    }

    @Singleton
    @Provides
    public static MissionDao provideAccountDao(MissionDatabase missionDatabase) {
        return missionDatabase.missionDao();
    }
}
