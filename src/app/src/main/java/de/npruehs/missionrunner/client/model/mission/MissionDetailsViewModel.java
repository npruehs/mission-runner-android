package de.npruehs.missionrunner.client.model.mission;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import de.npruehs.missionrunner.client.ActivityScope;
import de.npruehs.missionrunner.client.controller.mission.MissionRepository;
import de.npruehs.missionrunner.client.model.Resource;
import de.npruehs.missionrunner.client.model.character.Character;

@ActivityScope
public class MissionDetailsViewModel extends ViewModel {
    private final MissionRepository missionRepository;

    private final LiveData<Resource<Mission[]>> missions;
    private final LiveData<Resource<Character[]>> characters;

    private final MediatorLiveData<MissionDetails> missionDetails;

    @Inject
    public MissionDetailsViewModel(MissionRepository missionRepository) {
        this.missionRepository = missionRepository;

        missions = missionRepository.getMissions();
        characters = missionRepository.getCharacters();

        missionDetails = new MediatorLiveData<>();

        missionDetails.addSource(missions, new Observer<Resource<Mission[]>>() {
            @Override
            public void onChanged(Resource<Mission[]> r) {
                missionDetails.setValue(new MissionDetails
                        (missions.getValue().getData(), characters.getValue().getData()));
            }
        });

        missionDetails.addSource(characters, new Observer<Resource<Character[]>>() {
            @Override
            public void onChanged(Resource<Character[]> r) {
                missionDetails.setValue(new MissionDetails
                        (missions.getValue().getData(), characters.getValue().getData()));
            }
        });
    }

    public LiveData<Resource<Mission[]>> getMissions() {
        return missions;
    }

    public LiveData<Resource<Character[]>> getCharacters() {
        return characters;
    }

    public MediatorLiveData<MissionDetails> getMissionDetails() {
        return missionDetails;
    }

    public void startMission(int missionId, int[] characterIds) {
        missionRepository.startMission(missionId, characterIds);
    }
}
