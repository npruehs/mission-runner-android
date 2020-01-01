package de.npruehs.missionrunner.client.controller.account;

import android.app.Application;

import androidx.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.npruehs.missionrunner.client.model.account.AccountDao;
import de.npruehs.missionrunner.client.model.account.AccountDatabase;
import retrofit2.Retrofit;

@Module(subcomponents = AccountComponent.class)
public class AccountModule {
    @Singleton
    @Provides
    public static AccountService provideAccountService(Retrofit retrofit) {
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
