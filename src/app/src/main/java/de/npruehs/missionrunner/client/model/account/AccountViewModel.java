package de.npruehs.missionrunner.client.model.account;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import de.npruehs.missionrunner.client.ActivityScope;
import de.npruehs.missionrunner.client.controller.DataRepository;
import de.npruehs.missionrunner.client.model.Resource;

@ActivityScope
public class AccountViewModel extends ViewModel {
    private final DataRepository dataRepository;
    private final LiveData<Resource<Account>> account;

    @Inject
    public AccountViewModel(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
        account = dataRepository.getAccount();
    }

    public LiveData<Resource<Account>> getAccount() {
        return account;
    }
}
