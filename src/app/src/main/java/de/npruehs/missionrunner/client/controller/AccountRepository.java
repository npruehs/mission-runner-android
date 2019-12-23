package de.npruehs.missionrunner.client.controller;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.npruehs.missionrunner.client.ApplicationExecutors;
import de.npruehs.missionrunner.client.model.Account;
import de.npruehs.missionrunner.client.model.AccountDao;
import de.npruehs.missionrunner.client.model.Resource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class AccountRepository {
    private final AccountService accountService;
    private final AccountDao accountDao;
    private final ApplicationExecutors executors;

    private final MediatorLiveData<Resource<Account>> account;

    @Inject
    public AccountRepository(AccountService accountService, AccountDao accountDao, ApplicationExecutors executors) {
        this.accountService = accountService;
        this.accountDao = accountDao;
        this.executors = executors;

        this.account = new MediatorLiveData<>();
    }

    public LiveData<Resource<Account>> getAccount(final String id) {
        account.setValue(Resource.newPendingResource());

        // Fetch from local DB.
        final LiveData<Account> oldAccountData = accountDao.load(id);

        account.addSource(oldAccountData, new Observer<Account>() {
            @Override
            public void onChanged(Account a) {
                if (a != null) {
                    account.removeSource(oldAccountData);
                    account.setValue(Resource.newPendingResource(a));
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

                        // Fetch again from local DB.
                        executors.main().execute(new Runnable() {
                            @Override
                            public void run() {
                                final LiveData<Account> newAccountData = accountDao.load(id);

                                account.addSource(newAccountData, new Observer<Account>() {
                                    @Override
                                    public void onChanged(Account a) {
                                        if (a != null) {
                                            account.removeSource(newAccountData);
                                            account.setValue(Resource.newAvailableResource(a));
                                        }
                                    }
                                });
                            }
                        });
                    }
                });
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                account.setValue(Resource.newUnavailableResource(t.getMessage()));
            }
        });

        return account;
    }
}
