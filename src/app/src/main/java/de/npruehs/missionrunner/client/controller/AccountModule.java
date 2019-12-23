package de.npruehs.missionrunner.client.controller;

import android.app.Application;

import androidx.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.npruehs.missionrunner.client.model.AccountDao;
import de.npruehs.missionrunner.client.model.AccountDatabase;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(subcomponents = AccountComponent.class)
class AccountModule {
    @Singleton
    @Provides
    public static AccountService provideAccountService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.178.27:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(AccountService.class);
    }

    @Singleton
    @Provides
    public static AccountDatabase provideAccountDatabase(Application application) {
        return Room.databaseBuilder(application, AccountDatabase.class, "account.db")
                .fallbackToDestructiveMigration().build();
    }

    @Singleton
    @Provides
    public static AccountDao provideAccountDao(AccountDatabase accountDatabase) {
        return accountDatabase.accountDao();
    }
}
