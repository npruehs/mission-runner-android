package de.npruehs.missionrunner.client.controller.mission;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.npruehs.missionrunner.client.ApplicationExecutors;
import de.npruehs.missionrunner.client.controller.account.AccountIdProvider;
import de.npruehs.missionrunner.client.controller.character.CharacterService;
import de.npruehs.missionrunner.client.controller.mission.net.StartMissionRequest;
import de.npruehs.missionrunner.client.controller.mission.net.StartMissionResponse;
import de.npruehs.missionrunner.client.controller.net.NetworkResponse;
import de.npruehs.missionrunner.client.model.Resource;
import de.npruehs.missionrunner.client.model.character.Character;
import de.npruehs.missionrunner.client.model.character.CharacterDao;
import de.npruehs.missionrunner.client.model.mission.Mission;
import de.npruehs.missionrunner.client.model.mission.MissionDao;
import de.npruehs.missionrunner.client.model.mission.MissionStatus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class MissionRepository {
    private final MissionService missionService;
    private final MissionDao missionDao;

    private final CharacterService characterService;
    private final CharacterDao characterDao;

    private final ApplicationExecutors executors;
    private final AccountIdProvider accountIdProvider;

    private final MediatorLiveData<Resource<Mission[]>> missions;
    private final MediatorLiveData<Resource<Character[]>> characters;

    @Inject
    public MissionRepository(MissionService missionService, MissionDao missionDao, CharacterService characterService, CharacterDao characterDao, ApplicationExecutors executors, AccountIdProvider accountIdProvider) {
        this.missionService = missionService;
        this.missionDao = missionDao;

        this.characterService = characterService;
        this.characterDao = characterDao;

        this.executors = executors;
        this.accountIdProvider = accountIdProvider;

        this.missions = new MediatorLiveData<>();
        this.characters = new MediatorLiveData<>();
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
                saveMissions(accountId, response.body());
            }

            @Override
            public void onFailure(Call<Mission[]> call, Throwable t) {
                missions.setValue(Resource.newUnavailableResource(t.getMessage()));
            }
        });

        return missions;
    }

    public LiveData<Resource<Character[]>> getCharacters() {
        final String accountId = accountIdProvider.getAccountId();

        characters.setValue(Resource.newPendingResource());

        // Fetch from local DB.
        final LiveData<Character[]> oldData = characterDao.load(accountId);

        characters.addSource(oldData, new Observer<Character[]>() {
            @Override
            public void onChanged(Character[] c) {
                if (c != null) {
                    characters.removeSource(oldData);
                    characters.setValue(Resource.newPendingResource(c));
                }
            }
        });

        // Fetch from server.
        Call<Character[]> call = characterService.getCharacters(accountId);
        call.enqueue(new Callback<Character[]>() {

            @Override
            public void onResponse(Call<Character[]> call, final Response<Character[]> response) {
                saveCharacters(accountId, response.body());
            }

            @Override
            public void onFailure(Call<Character[]> call, Throwable t) {
                characters.setValue(Resource.newUnavailableResource(t.getMessage()));
            }
        });

        return characters;
    }

    public void startMission(int missionId, int[] characterIds) {
        final String accountId = accountIdProvider.getAccountId();

        if (missions.getValue() == null) {
            throw new IllegalStateException("Call getMissions first.");
        }

        if (characters.getValue() == null) {
            throw new IllegalStateException("Call getCharacters first.");
        }

        final Mission[] oldMissionData = missions.getValue().getData();
        final Character[] oldCharacterData = characters.getValue().getData();

        if (oldMissionData == null) {
            throw new IllegalStateException("Call getMissions first.");
        }

        if (oldCharacterData == null) {
            throw new IllegalStateException("Call getCharacters first.");
        }

        missions.setValue(Resource.newPendingResource(oldMissionData));
        characters.setValue(Resource.newPendingResource(oldCharacterData));

        // Send request.
        final StartMissionRequest request = new StartMissionRequest();
        request.setAccountId(accountId);
        request.setMissionId(missionId);
        request.setCharacterIds(characterIds);

        Call<NetworkResponse<StartMissionResponse>> call = missionService.startMission(request);
        call.enqueue(new Callback<NetworkResponse<StartMissionResponse>>() {

            @Override
            public void onResponse(Call<NetworkResponse<StartMissionResponse>> call, final Response<NetworkResponse<StartMissionResponse>> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        final StartMissionResponse data = response.body().getData();

                        // Update mission data.
                        StartMissionResponse.MissionUpdate missionUpdate = data.getMission();

                        if (missionUpdate != null) {
                            for (Mission mission : oldMissionData) {
                                if (mission.getId() == missionUpdate.getId()) {
                                    mission.setStatus(missionUpdate.getStatus());

                                    if (mission.getStatus() == MissionStatus.RUNNING) {
                                        mission.setFinishTime(DateTime.now(DateTimeZone.UTC)
                                                .plusSeconds(mission.getRequiredTime()));
                                    }
                                }
                            }

                            saveMissions(accountId, oldMissionData);
                        }

                        // Update character data.
                        StartMissionResponse.CharacterUpdate[] characterUpdates = data.getCharacters();

                        if (characterUpdates != null) {
                            for (Character character : oldCharacterData) {
                                for (StartMissionResponse.CharacterUpdate characterUpdate : characterUpdates) {
                                    if (character.getId() == characterUpdate.getId()) {
                                        character.setStatus(characterUpdate.getStatus());
                                    }
                                }
                            }

                            saveCharacters(accountId, oldCharacterData);
                        }
                    } else {
                        // TODO(np): Localize error code.
                        missions.setValue(Resource.newUnavailableResource
                                (response.body().getError().getErrorMessage(), oldMissionData));
                    }
                } else {
                    missions.setValue(Resource.newUnavailableResource(response.message(), oldMissionData));
                }
            }

            @Override
            public void onFailure(Call<NetworkResponse<StartMissionResponse>> call, Throwable t) {
                missions.setValue(Resource.newUnavailableResource(t.getMessage(), oldMissionData));
            }
        });
    }

    private void saveMissions(final String accountId, final Mission[] newMissions) {
        executors.IO().execute(new Runnable() {
            @Override
            public void run() {
                // Store in local DB.
                missionDao.save(newMissions);

                // Fetch again from local DB (single source of truth).
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

    private void saveCharacters(final String accountId, final Character[] newCharacters) {
        executors.IO().execute(new Runnable() {
            @Override
            public void run() {
                // Store in local DB.
                characterDao.save(newCharacters);

                // Fetch again from local DB (single source of truth).
                executors.main().execute(new Runnable() {
                    @Override
                    public void run() {
                        final LiveData<Character[]> newData = characterDao.load(accountId);

                        characters.addSource(newData, new Observer<Character[]>() {
                            @Override
                            public void onChanged(Character[] c) {
                                if (c != null) {
                                    characters.removeSource(newData);
                                    characters.setValue(Resource.newAvailableResource(c));
                                }
                            }
                        });
                    }
                });
            }
        });
    }
}
