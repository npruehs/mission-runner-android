package de.npruehs.missionrunner.client.model.mission;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import de.npruehs.missionrunner.client.ActivityScope;
import de.npruehs.missionrunner.client.controller.DataRepository;
import de.npruehs.missionrunner.client.model.Resource;
import de.npruehs.missionrunner.client.model.character.Character;
import de.npruehs.missionrunner.client.model.localization.LocalizationData;

@ActivityScope
public class MissionDetailsViewModel extends ViewModel {
    private final DataRepository missionRepository;

    private final LiveData<Resource<Mission[]>> missions;
    private final LiveData<Resource<Character[]>> characters;
    private final LiveData<Resource<LocalizationData>> localization;

    private final MediatorLiveData<MissionDetails> missionDetails;

    @Inject
    public MissionDetailsViewModel(DataRepository missionRepository) {
        this.missionRepository = missionRepository;

        missions = missionRepository.getMissions();
        characters = missionRepository.getCharacters();
        localization = missionRepository.getLocalization();

        missionDetails = new MediatorLiveData<>();

        missionDetails.addSource(missions, new Observer<Resource<Mission[]>>() {
            @Override
            public void onChanged(Resource<Mission[]> r) {
                missionDetails.setValue(new MissionDetails
                        (missions.getValue().getData(), characters.getValue().getData(), localization.getValue().getData()));
            }
        });

        missionDetails.addSource(characters, new Observer<Resource<Character[]>>() {
            @Override
            public void onChanged(Resource<Character[]> c) {
                missionDetails.setValue(new MissionDetails
                        (missions.getValue().getData(), characters.getValue().getData(), localization.getValue().getData()));
            }
        });

        missionDetails.addSource(localization, new Observer<Resource<LocalizationData>>() {
            @Override
            public void onChanged(Resource<LocalizationData> l) {
                missionDetails.setValue(new MissionDetails
                        (missions.getValue().getData(), characters.getValue().getData(), localization.getValue().getData()));
            }
        });
    }

    public LiveData<Resource<Mission[]>> getMissions() {
        return missions;
    }

    public LiveData<Resource<Character[]>> getCharacters() {
        return characters;
    }

    public LiveData<Resource<LocalizationData>> getLocalization() { return localization; }

    public MediatorLiveData<MissionDetails> getMissionDetails() {
        return missionDetails;
    }

    public void startMission(int missionId, int[] characterIds) {
        missionRepository.startMission(missionId, characterIds);
    }

    public void finishMission(int missionId) {
        missionRepository.finishMission(missionId);
    }
}
