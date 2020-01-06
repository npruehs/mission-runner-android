package de.npruehs.missionrunner.client.controller.mission;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.npruehs.missionrunner.client.ApplicationExecutors;
import de.npruehs.missionrunner.client.controller.account.AccountIdProvider;
import de.npruehs.missionrunner.client.model.Resource;
import de.npruehs.missionrunner.client.model.mission.Mission;
import de.npruehs.missionrunner.client.model.mission.MissionDao;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class MissionRepository {
    private final MissionService missionService;
    private final MissionDao missionDao;
    private final ApplicationExecutors executors;
    private final AccountIdProvider accountIdProvider;

    private final MediatorLiveData<Resource<Mission[]>> missions;

    @Inject
    public MissionRepository(MissionService missionService, MissionDao missionDao, ApplicationExecutors executors, AccountIdProvider accountIdProvider) {
        this.missionService = missionService;
        this.missionDao = missionDao;
        this.executors = executors;
        this.accountIdProvider = accountIdProvider;

        this.missions = new MediatorLiveData<>();
    }

    public LiveData<Resource<Mission[]>> getMissions() {
        final String accountId = accountIdProvider.getAccountId();

        missions.setValue(Resource.newPendingResource());

        // Fetch from local DB.
        final LiveData<Mission[]> oldMissionsData = missionDao.load(accountId);

        missions.addSource(oldMissionsData, new Observer<Mission[]>() {
            @Override
            public void onChanged(Mission[] m) {
                if (m != null) {
                    missions.removeSource(oldMissionsData);
                    missions.setValue(Resource.newPendingResource(m));
                }
            }
        });

        // Fetch from server.
        Call<Mission[]> call = missionService.getMissions(accountId);
        call.enqueue(new Callback<Mission[]>() {

            @Override
            public void onResponse(Call<Mission[]> call, final Response<Mission[]> response) {
                executors.IO().execute(new Runnable() {
                    @Override
                    public void run() {
                        // Store in local DB.
                        missionDao.save(response.body());

                        // Fetch again from local DB.
                        executors.main().execute(new Runnable() {
                            @Override
                            public void run() {
                                final LiveData<Mission[]> newMissionData = missionDao.load(accountId);

                                missions.addSource(newMissionData, new Observer<Mission[]>() {
                                    @Override
                                    public void onChanged(Mission[] m) {
                                        if (m != null) {
                                            missions.removeSource(newMissionData);
                                            missions.setValue(Resource.newAvailableResource(m));
                                        }
                                    }
                                });
                            }
                        });
                    }
                });
            }

            @Override
            public void onFailure(Call<Mission[]> call, Throwable t) {
                missions.setValue(Resource.newUnavailableResource(t.getMessage()));
            }
        });

        return missions;
    }
}
