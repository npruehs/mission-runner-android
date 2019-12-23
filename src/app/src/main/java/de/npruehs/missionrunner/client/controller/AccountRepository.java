package de.npruehs.missionrunner.client.controller;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.npruehs.missionrunner.client.ApplicationExecutors;
import de.npruehs.missionrunner.client.model.Account;
import de.npruehs.missionrunner.client.model.AccountDao;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class AccountRepository {
    private final AccountService accountService;
    private final AccountDao accountDao;
    private final ApplicationExecutors executors;

    private final MediatorLiveData<Account> account;

    @Inject
    public AccountRepository(AccountService accountService, AccountDao accountDao, ApplicationExecutors executors) {
        this.accountService = accountService;
        this.accountDao = accountDao;
        this.executors = executors;

        this.account = new MediatorLiveData<>();
    }

    public LiveData<Account> getAccount(final String id) {
        final LiveData<Account> accountData = accountDao.load(id);

        // Fetch from local DB.
        account.addSource(accountData, new Observer<Account>() {
            @Override
            public void onChanged(Account a) {
                if (a != null) {
                    account.removeSource(account);
                    account.setValue(a);
                }
            }
        });

        // Fetch from server.
        Call<Account> accountCall = accountService.getAccount(id);
        accountCall.enqueue(new Callback<Account>() {

            @Override
            public void onResponse(Call<Account> call, final Response<Account> response) {
                executors.IO().execute(new Runnable() {
                    @Override
                    public void run() {
                        // Store in local DB.
                        accountDao.save(response.body());
                    }
                });
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                throw new RuntimeException(t.getMessage());
            }
        });

        return account;
    }
}
