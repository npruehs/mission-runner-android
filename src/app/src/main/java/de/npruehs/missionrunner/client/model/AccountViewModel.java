package de.npruehs.missionrunner.client.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import de.npruehs.missionrunner.client.ActivityScope;
import de.npruehs.missionrunner.client.controller.AccountRepository;

@ActivityScope
public class AccountViewModel extends ViewModel {
    private final AccountRepository accountRepository;
    private LiveData<Resource<Account>> account;

    @Inject
    public AccountViewModel(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;

        account = accountRepository.getAccount("A1B2C3");
    }

    public LiveData<Resource<Account>> getAccount() {
        return account;
    }
}
