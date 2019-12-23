package de.npruehs.missionrunner.client.controller;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.npruehs.missionrunner.client.model.Account;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class AccountRepository {
    private final AccountService accountService;

    private HashMap<String, MutableLiveData<Account>> accountCache = new HashMap<>();

    @Inject
    public AccountRepository(AccountService accountService) {
        this.accountService = accountService;
    }

    public LiveData<Account> getAccount(String id) {
        // Access cache.
        MutableLiveData<Account> cachedAccount = accountCache.get(id);

        if (cachedAccount != null) {
            return cachedAccount;
        }

        // Cache miss; fetch data from backend.
        final MutableLiveData<Account> account = new MutableLiveData<Account>();
        accountCache.put(id, account);

        Call<Account> accountCall = accountService.getAccount(id);
        accountCall.enqueue(new Callback<Account>() {

            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                account.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                throw new RuntimeException(t.getMessage());
            }
        });

        return account;
    }
}
