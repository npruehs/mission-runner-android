package de.npruehs.missionrunner.client.controller;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.ArrayList;
import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.npruehs.missionrunner.client.ApplicationExecutors;
import de.npruehs.missionrunner.client.controller.account.AccountIdProvider;
import de.npruehs.missionrunner.client.controller.account.AccountService;
import de.npruehs.missionrunner.client.controller.character.CharacterService;
import de.npruehs.missionrunner.client.controller.mission.MissionService;
import de.npruehs.missionrunner.client.controller.mission.net.FinishMissionRequest;
import de.npruehs.missionrunner.client.controller.mission.net.FinishMissionResponse;
import de.npruehs.missionrunner.client.controller.mission.net.StartMissionRequest;
import de.npruehs.missionrunner.client.controller.mission.net.StartMissionResponse;
import de.npruehs.missionrunner.client.controller.net.NetworkResponse;
import de.npruehs.missionrunner.client.model.Resource;
import de.npruehs.missionrunner.client.model.account.Account;
import de.npruehs.missionrunner.client.model.account.AccountDao;
import de.npruehs.missionrunner.client.model.character.Character;
import de.npruehs.missionrunner.client.model.character.CharacterDao;
import de.npruehs.missionrunner.client.model.mission.Mission;
import de.npruehs.missionrunner.client.model.mission.MissionDao;
import de.npruehs.missionrunner.client.model.mission.MissionStatus;
import de.npruehs.missionrunner.client.view.ErrorMessages;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class DataRepository {
    private final Application application;

    private final AccountService accountService;
    private final AccountDao accountDao;

    private final MissionService missionService;
    private final MissionDao missionDao;

    private final CharacterService characterService;
    private final CharacterDao characterDao;

    private final ApplicationExecutors executors;
    private final AccountIdProvider accountIdProvider;

    private final MediatorLiveData<Resource<Account>> account;
    private final MediatorLiveData<Resource<Mission[]>> missions;
    private final MediatorLiveData<Resource<Character[]>> characters;

    @Inject
    public DataRepository(Application application, AccountService accountService, AccountDao accountDao,
                          MissionService missionService, MissionDao missionDao,
                          CharacterService characterService, CharacterDao characterDao,
                          ApplicationExecutors executors, AccountIdProvider accountIdProvider) {
        this.application = application;

        this.accountService = accountService;
        this.accountDao = accountDao;

        this.missionService = missionService;
        this.missionDao = missionDao;

        this.characterService = characterService;
        this.characterDao = characterDao;

        this.executors = executors;
        this.accountIdProvider = accountIdProvider;

        this.account = new MediatorLiveData<>();
        this.missions = new MediatorLiveData<>();
        this.characters = new MediatorLiveData<>();
    }


    public LiveData<Resource<Account>> getAccount() {
        final String accountId = accountIdProvider.getAccountId();

        account.setValue(Resource.newPendingResource());

        // Fetch from local DB.
        final LiveData<Account> oldAccountData = accountDao.get(accountId);

        account.addSource(oldAccountData, new Observer<Account>() {
            @Override
            public void onChanged(Account a) {
                if (a != null) {
                    account.removeSource(oldAccountData);
                    account.setValue(Resource.newPendingResource(a));
                }
            }
        });

        // Fetch from server.
        Call<Account> accountCall = accountService.getAccount(accountId);
        accountCall.enqueue(new Callback<Account>() {

            @Override
            public void onResponse(Call<Account> call, final Response<Account> response) {
                saveAccount(response.body());
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                account.setValue(Resource.newUnavailableResource(t.getMessage()));
            }
        });

        return account;
    }

    public LiveData<Resource<Mission[]>> getMissions() {
        final String accountId = accountIdProvider.getAccountId();

        missions.setValue(Resource.newPendingResource());

        // Fetch from local DB.
        final LiveData<Mission[]> oldMissionsData = missionDao.get();

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
        Call<NetworkResponse<Mission[]>> call = missionService.getMissions(accountId);
        call.enqueue(new Callback<NetworkResponse<Mission[]>>() {

            @Override
            public void onResponse(Call<NetworkResponse<Mission[]>> call, final Response<NetworkResponse<Mission[]>> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        saveMissions(response.body().getData());
                    } else {
                        String errorMessage = ErrorMessages.get(application, response.body().getError());
                        characters.setValue(Resource.newUnavailableResource(errorMessage, oldMissionsData));
                    }
                } else {
                    characters.setValue(Resource.newUnavailableResource(response.message(), oldMissionsData));
                }
            }

            @Override
            public void onFailure(Call<NetworkResponse<Mission[]>> call, Throwable t) {
                missions.setValue(Resource.newUnavailableResource(t.getMessage()));
            }
        });

        return missions;
    }

    public LiveData<Resource<Character[]>> getCharacters() {
        final String accountId = accountIdProvider.getAccountId();

        characters.setValue(Resource.newPendingResource());

        // Fetch from local DB.
        final LiveData<Character[]> oldData = characterDao.get();

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
        Call<NetworkResponse<Character[]>> call = characterService.getCharacters(accountId);
        call.enqueue(new Callback<NetworkResponse<Character[]>>() {

            @Override
            public void onResponse(Call<NetworkResponse<Character[]>> call, final Response<NetworkResponse<Character[]>> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        saveCharacters(response.body().getData());
                    } else {
                        String errorMessage = ErrorMessages.get(application, response.body().getError());
                        characters.setValue(Resource.newUnavailableResource(errorMessage, oldData));
                    }
                } else {
                    characters.setValue(Resource.newUnavailableResource(response.message(), oldData));
                }
            }

            @Override
            public void onFailure(Call<NetworkResponse<Character[]>> call, Throwable t) {
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

                            saveMissions(oldMissionData);
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

                            saveCharacters(oldCharacterData);
                        }
                    } else {
                        String errorMessage = ErrorMessages.get(application, response.body().getError());
                        missions.setValue(Resource.newUnavailableResource(errorMessage, oldMissionData));
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

    public void finishMission(int missionId) {
        final String accountId = accountIdProvider.getAccountId();

        if (account.getValue() == null) {
            throw new IllegalStateException("Call getAccount first.");
        }

        if (missions.getValue() == null) {
            throw new IllegalStateException("Call getMissions first.");
        }

        if (characters.getValue() == null) {
            throw new IllegalStateException("Call getMissions first.");
        }

        final Account oldAccountData = account.getValue().getData();
        final Mission[] oldMissionData = missions.getValue().getData();
        final Character[] oldCharacterData = characters.getValue().getData();

        if (oldAccountData == null) {
            throw new IllegalStateException("Call getAccount first.");
        }

        if (oldMissionData == null) {
            throw new IllegalStateException("Call getMissions first.");
        }

        if (oldCharacterData == null) {
            throw new IllegalStateException("Call getMissions first.");
        }

        account.setValue(Resource.newPendingResource(oldAccountData));
        missions.setValue(Resource.newPendingResource(oldMissionData));
        characters.setValue(Resource.newPendingResource(oldCharacterData));

        final ArrayList<Mission> missionList = new ArrayList<>(Arrays.asList(oldMissionData));
        final ArrayList<Character> characterList = new ArrayList<>(Arrays.asList(oldCharacterData));

        // Send request.
        final FinishMissionRequest request = new FinishMissionRequest();
        request.setAccountId(accountId);
        request.setMissionId(missionId);

        Call<NetworkResponse<FinishMissionResponse>> call = missionService.finishMission(request);
        call.enqueue(new Callback<NetworkResponse<FinishMissionResponse>>() {

            @Override
            public void onResponse(Call<NetworkResponse<FinishMissionResponse>> call, final Response<NetworkResponse<FinishMissionResponse>> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        final FinishMissionResponse data = response.body().getData();

                        // Update account data.
                        FinishMissionResponse.AccountUpdate accountUpdate = data.getAccount();

                        if (accountUpdate != null) {
                            oldAccountData.setLevel(accountUpdate.getLevel());
                            oldAccountData.setScore(accountUpdate.getScore());

                            saveAccount(oldAccountData);
                        }

                        // Update mission data.
                        FinishMissionResponse.MissionUpdate missionUpdate = data.getMissions();

                        if (missionUpdate != null) {
                            // Remove missions.
                            for (int i = missionList.size() - 1; i >= 0; --i) {
                                Mission oldMission = missionList.get(i);

                                for (int j = 0; j < missionUpdate.getRemovedMissions().length; ++j) {
                                    if (oldMission.getId() == missionUpdate.getRemovedMissions()[j]) {
                                        missionList.remove(i);
                                    }
                                }
                            }

                            // Add missions.
                            for (Mission newMission : missionUpdate.getAddedMissions()) {
                                missionList.add(newMission);
                            }

                            // Save to DB.
                            Mission[] newMissions = new Mission[missionList.size()];
                            missionList.toArray(newMissions);
                            saveMissions(newMissions);
                        }

                        // Update character data.
                        FinishMissionResponse.CharacterUpdate[] characterUpdates = data.getCharacters();

                        if (characterUpdates != null) {
                            for (FinishMissionResponse.CharacterUpdate characterUpdate : characterUpdates) {
                                boolean newCharacter = true;

                                for (Character character : characterList) {
                                    if (character.getId() == characterUpdate.getId()) {
                                        newCharacter = false;
                                        character.setStatus(characterUpdate.getStatus());
                                        break;
                                    }
                                }

                                // Add new characters.
                                if (newCharacter) {
                                    Character character = new Character();
                                    character.setId(characterUpdate.getId());
                                    character.setName(characterUpdate.getName());
                                    character.setStatus(characterUpdate.getStatus());
                                    character.setMissionId(characterUpdate.getMissionId());
                                    character.setSkills(characterUpdate.getSkills());

                                    characterList.add(character);
                                }
                            }

                            // Save to DB.
                            Character[] newCharacters = new Character[characterList.size()];
                            characterList.toArray(newCharacters);
                            saveCharacters(newCharacters);
                        }
                    } else {
                        String errorMessage = ErrorMessages.get(application, response.body().getError());
                        missions.setValue(Resource.newUnavailableResource(errorMessage, oldMissionData));
                    }
                } else {
                    missions.setValue(Resource.newUnavailableResource(response.message(), oldMissionData));
                }
            }

            @Override
            public void onFailure(Call<NetworkResponse<FinishMissionResponse>> call, Throwable t) {
                missions.setValue(Resource.newUnavailableResource(t.getMessage(), oldMissionData));
            }
        });
    }

    private void saveAccount(final Account newAccount) {
        executors.IO().execute(new Runnable() {
            @Override
            public void run() {
                // Store in local DB.
                accountDao.insert(newAccount);

                // Fetch again from local DB.
                executors.main().execute(new Runnable() {
                    @Override
                    public void run() {
                        final LiveData<Account> newAccountData = accountDao.get(newAccount.getId());

                        account.addSource(newAccountData, new Observer<Account>() {
                            @Override
                            public void onChanged(Account a) {
                                if (a != null) {
                                    account.removeSource(newAccountData);
                                    account.setValue(Resource.newAvailableResource(a));
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    private void saveMissions(final Mission[] newMissions) {
        executors.IO().execute(new Runnable() {
            @Override
            public void run() {
                // Store in local DB.
                missionDao.clear();
                missionDao.insert(newMissions);

                // Fetch again from local DB (single source of truth).
                executors.main().execute(new Runnable() {
                    @Override
                    public void run() {
                        final LiveData<Mission[]> newMissionData = missionDao.get();

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

    private void saveCharacters(final Character[] newCharacters) {
        executors.IO().execute(new Runnable() {
            @Override
            public void run() {
                // Store in local DB.
                characterDao.insert(newCharacters);

                // Fetch again from local DB (single source of truth).
                executors.main().execute(new Runnable() {
                    @Override
                    public void run() {
                        final LiveData<Character[]> newData = characterDao.get();

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
