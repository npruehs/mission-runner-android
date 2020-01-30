package de.npruehs.missionrunner.client.controller.localization;

import de.npruehs.missionrunner.client.controller.net.NetworkResponse;
import de.npruehs.missionrunner.client.model.localization.LocalizationData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LocalizationService {
    @GET("localization/get")
    Call<NetworkResponse<LocalizationData>> getLocalization(@Query("hash") String hash);
}
