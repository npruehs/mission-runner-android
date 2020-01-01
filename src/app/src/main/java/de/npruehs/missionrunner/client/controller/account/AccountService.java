package de.npruehs.missionrunner.client.controller.account;

import de.npruehs.missionrunner.client.model.account.Account;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AccountService {
    @GET("account/get")
    Call<Account> getAccount(@Query("id") String id);
}
