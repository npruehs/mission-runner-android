package de.npruehs.missionrunner.client.controller.mission;

import de.npruehs.missionrunner.client.model.mission.Mission;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MissionService {
    @GET("missions/get")
    Call<Mission[]> getMissions(@Query("accountId") String accountId);
}
