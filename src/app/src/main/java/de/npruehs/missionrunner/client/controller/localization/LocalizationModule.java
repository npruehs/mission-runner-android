package de.npruehs.missionrunner.client.controller.localization;

import android.app.Application;

import androidx.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.npruehs.missionrunner.client.model.localization.LocalizationDao;
import de.npruehs.missionrunner.client.model.localization.LocalizationDatabase;
import retrofit2.Retrofit;

@Module(subcomponents = LocalizationComponent.class)
public class LocalizationModule {
    @Singleton
    @Provides
    public static LocalizationService provideLocalizationService(Retrofit retrofit) {
        return retrofit.create(LocalizationService.class);
    }

    @Singleton
    @Provides
    public static LocalizationDatabase provideLocalizationDatabase(Application application) {
        return Room.databaseBuilder(application, LocalizationDatabase.class, "localization.db")
                .fallbackToDestructiveMigration().build();
    }

    @Singleton
    @Provides
    public static LocalizationDao provideLocalizationDao(LocalizationDatabase localizationDatabase) {
        return localizationDatabase.localizationDao();
    }
}
