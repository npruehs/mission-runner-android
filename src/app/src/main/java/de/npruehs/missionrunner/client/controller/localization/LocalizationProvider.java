package de.npruehs.missionrunner.client.controller.localization;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.npruehs.missionrunner.client.R;

@Singleton
public class LocalizationProvider {
    private final Application application;

    private String localizationHash;

    @Inject
    public LocalizationProvider(Application application) {
        this.application = application;

        String localizationPreferences = application.getString(R.string.preferences_localization);
        SharedPreferences sharedPref = application.getSharedPreferences(localizationPreferences, Context.MODE_PRIVATE);

        String preferencesKeyHash = application.getString(R.string.preferences_localization_hash);
        localizationHash = sharedPref.getString(preferencesKeyHash, null);
    }

    public String getLocalizationHash() {
        return localizationHash;
    }

    public void setLocalizationHash(String localizationHash) {
        this.localizationHash = localizationHash;

        // Save to preferences.
        String localizationPreferences = application.getString(R.string.preferences_localization);
        SharedPreferences sharedPref = application.getSharedPreferences(localizationPreferences, Context.MODE_PRIVATE);

        String preferencesKeyHash = application.getString(R.string.preferences_localization_hash);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(preferencesKeyHash, localizationHash);
        editor.commit();
    }
}
