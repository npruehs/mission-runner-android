package de.npruehs.missionrunner.client.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import de.npruehs.missionrunner.client.controller.AccountRepository;

public class AccountViewModel extends ViewModel {
    private AccountRepository accountRepository;
    private LiveData<Account> account;

    public AccountViewModel() {
        accountRepository = new AccountRepository();
        account = accountRepository.getAccount("A1B2C3");
    }

    public LiveData<Account> getAccount() {
        return account;
    }
}
