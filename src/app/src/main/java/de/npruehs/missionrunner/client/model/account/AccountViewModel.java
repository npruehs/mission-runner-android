package de.npruehs.missionrunner.client.model.account;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import de.npruehs.missionrunner.client.ActivityScope;
import de.npruehs.missionrunner.client.controller.account.AccountRepository;
import de.npruehs.missionrunner.client.model.Resource;

@ActivityScope
public class AccountViewModel extends ViewModel {
    private final AccountRepository accountRepository;
    private final LiveData<Resource<Account>> account;

    @Inject
    public AccountViewModel(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        account = accountRepository.getAccount();
    }

    public LiveData<Resource<Account>> getAccount() {
        return account;
    }
}
