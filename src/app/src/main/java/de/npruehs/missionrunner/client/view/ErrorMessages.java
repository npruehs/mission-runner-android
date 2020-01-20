package de.npruehs.missionrunner.client.view;

import android.content.Context;

import de.npruehs.missionrunner.client.R;
import de.npruehs.missionrunner.client.controller.net.NetworkResponse;

public class ErrorMessages {
    public static String get(Context context, NetworkResponse.Error error) {
        switch (error.getErrorCode()) {
            case 1:
                return context.getString(R.string.error_bad_request);

            default:
                return error.getErrorMessage();
        }
    }
}
