package de.npruehs.missionrunner.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AccountViewModel extends ViewModel {
    private LiveData<Account> account;

    public AccountViewModel() {
        // TODO(np): Remove mock data.
        Account testAccount = new Account();
        testAccount.setAccountId("A1B2C3");
        testAccount.setLevel(3);
        testAccount.setScore(1400);

        MutableLiveData<Account> testAccountLiveData = new MutableLiveData<Account>();
        testAccountLiveData.setValue(testAccount);
        account = testAccountLiveData;
    }

    public LiveData<Account> getAccount() {
        return account;
    }
}
