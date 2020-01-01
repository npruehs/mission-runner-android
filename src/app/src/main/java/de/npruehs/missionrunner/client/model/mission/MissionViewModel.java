package de.npruehs.missionrunner.client.model.mission;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import de.npruehs.missionrunner.client.ActivityScope;
import de.npruehs.missionrunner.client.controller.mission.MissionRepository;
import de.npruehs.missionrunner.client.model.Resource;

@ActivityScope
public class MissionViewModel extends ViewModel {
    private final MissionRepository missionRepository;
    private final LiveData<Resource<Mission[]>> missions;

    @Inject
    public MissionViewModel(MissionRepository missionRepository) {
        this.missionRepository = missionRepository;

        missions = missionRepository.getMissions("A1B2C3");
    }

    public LiveData<Resource<Mission[]>> getMissions() {
        return missions;
    }
}
