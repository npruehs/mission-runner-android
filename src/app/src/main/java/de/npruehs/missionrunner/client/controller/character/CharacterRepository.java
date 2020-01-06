package de.npruehs.missionrunner.client.controller.character;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.npruehs.missionrunner.client.ApplicationExecutors;
import de.npruehs.missionrunner.client.controller.account.AccountIdProvider;
import de.npruehs.missionrunner.client.model.Resource;
import de.npruehs.missionrunner.client.model.character.Character;
import de.npruehs.missionrunner.client.model.character.CharacterDao;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class CharacterRepository {
    private final CharacterService characterService;
    private final CharacterDao characterDao;
    private final ApplicationExecutors executors;
    private final AccountIdProvider accountIdProvider;

    private final MediatorLiveData<Resource<Character[]>> characters;

    @Inject
    public CharacterRepository(CharacterService characterService, CharacterDao characterDao, ApplicationExecutors executors, AccountIdProvider accountIdProvider) {
        this.characterService = characterService;
        this.characterDao = characterDao;
        this.executors = executors;
        this.accountIdProvider = accountIdProvider;

        this.characters = new MediatorLiveData<>();
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
                executors.IO().execute(new Runnable() {
                    @Override
                    public void run() {
                        // Store in local DB.
                        characterDao.save(response.body());

                        // Fetch again from local DB.
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

            @Override
            public void onFailure(Call<Character[]> call, Throwable t) {
                characters.setValue(Resource.newUnavailableResource(t.getMessage()));
            }
        });

        return characters;
    }
}
