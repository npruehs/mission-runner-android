package de.npruehs.missionrunner.client.controller.account;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.npruehs.missionrunner.client.R;

@Singleton
public class AccountIdProvider {
    private String accountId;

    @Inject
    public AccountIdProvider(Application application) {
        String accountPreferences = application.getString(R.string.preferences_account);
        SharedPreferences sharedPref = application.getSharedPreferences(accountPreferences, Context.MODE_PRIVATE);

        String preferencesKeyId = application.getString(R.string.preferences_account_id);
        accountId = sharedPref.getString(preferencesKeyId, null);

        if (accountId == null) {
            accountId = UUID.randomUUID().toString().replace("-", "");

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(preferencesKeyId, accountId);
            editor.commit();
        }
    }

    public String getAccountId() {
        return accountId;
    }
}
