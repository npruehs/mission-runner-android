package de.npruehs.missionrunner.client.controller.character;

import de.npruehs.missionrunner.client.controller.net.NetworkResponse;
import de.npruehs.missionrunner.client.model.character.Character;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CharacterService {
    @GET("characters/get")
    Call<NetworkResponse<Character[]>> getCharacters(@Query("accountId") String accountId);
}
