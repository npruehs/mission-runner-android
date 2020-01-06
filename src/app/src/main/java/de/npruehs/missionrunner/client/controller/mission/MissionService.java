package de.npruehs.missionrunner.client.controller.mission;

import de.npruehs.missionrunner.client.controller.mission.net.StartMissionRequest;
import de.npruehs.missionrunner.client.controller.mission.net.StartMissionResponse;
import de.npruehs.missionrunner.client.controller.net.NetworkResponse;
import de.npruehs.missionrunner.client.model.mission.Mission;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MissionService {
    @GET("missions/get")
    Call<Mission[]> getMissions(@Query("accountId") String accountId);

    @POST("missions/start")
    Call<NetworkResponse<StartMissionResponse>> startMission(@Body StartMissionRequest request);
}
