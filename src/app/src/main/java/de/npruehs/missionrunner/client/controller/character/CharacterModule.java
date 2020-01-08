package de.npruehs.missionrunner.client.controller.character;

import android.app.Application;

import androidx.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.npruehs.missionrunner.client.model.character.CharacterDao;
import de.npruehs.missionrunner.client.model.character.CharacterDatabase;
import retrofit2.Retrofit;

@Module(subcomponents = CharacterComponent.class)
public class CharacterModule {
    @Singleton
    @Provides
    public static CharacterService provideCharacterService(Retrofit retrofit) {
        return retrofit.create(CharacterService.class);
    }

    @Singleton
    @Provides
    public static CharacterDatabase provideCharacterDatabase(Application application) {
        return Room.databaseBuilder(application, CharacterDatabase.class, "characters.db")
                .fallbackToDestructiveMigration().build();
    }

    @Singleton
    @Provides
    public static CharacterDao provideCharacterDao(CharacterDatabase characterDatabase) {
        return characterDatabase.characterDao();
    }
}
