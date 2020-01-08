package de.npruehs.missionrunner.client.model.mission;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import de.npruehs.missionrunner.client.ActivityScope;
import de.npruehs.missionrunner.client.controller.DataRepository;
import de.npruehs.missionrunner.client.model.Resource;

@ActivityScope
public class MissionViewModel extends ViewModel {
    private final DataRepository missionRepository;
    private final LiveData<Resource<Mission[]>> missions;

    @Inject
    public MissionViewModel(DataRepository missionRepository) {
        this.missionRepository = missionRepository;

        missions = missionRepository.getMissions();
    }

    public LiveData<Resource<Mission[]>> getMissions() {
        return missions;
    }
}
