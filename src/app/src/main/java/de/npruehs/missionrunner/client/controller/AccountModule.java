package de.npruehs.missionrunner.client.controller;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(subcomponents = AccountComponent.class)
class AccountModule {
    @Singleton
    @Provides
    public static AccountService provideAccountService() {
        // Create account service.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.178.27:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(AccountService.class);
    }
}
