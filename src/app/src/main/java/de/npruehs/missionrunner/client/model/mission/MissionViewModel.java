package de.npruehs.missionrunner.client.model.mission;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import de.npruehs.missionrunner.client.ActivityScope;
import de.npruehs.missionrunner.client.controller.DataRepository;
import de.npruehs.missionrunner.client.model.Resource;
import de.npruehs.missionrunner.client.model.ResourceStatus;
import de.npruehs.missionrunner.client.model.localization.LocalizationData;

@ActivityScope
public class MissionViewModel extends ViewModel {
    private final DataRepository repository;

    private final LiveData<Resource<Mission[]>> missions;
    private final LiveData<Resource<LocalizationData>> localization;

    private final MediatorLiveData<Resource<LocalizedMissions>> localizedMissions;

    @Inject
    public MissionViewModel(DataRepository repository) {
        this.repository = repository;

        missions = repository.getMissions();
        localization = repository.getLocalization();

        localizedMissions = new MediatorLiveData<>();

        localizedMissions.addSource(missions, new Observer<Resource<Mission[]>>() {
            @Override
            public void onChanged(Resource<Mission[]> r) {
                updateLocalizedMissions();
            }
        });

        localizedMissions.addSource(localization, new Observer<Resource<LocalizationData>>() {
            @Override
            public void onChanged(Resource<LocalizationData> l) {
                updateLocalizedMissions();
            }
        });
    }

    public LiveData<Resource<Mission[]>> getMissions() {
        return missions;
    }
    public LiveData<Resource<LocalizationData>> getLocalization() { return localization; }

    public MediatorLiveData<Resource<LocalizedMissions>> getLocalizedMissions() {
        return localizedMissions;
    }

    private void updateLocalizedMissions() {
        LocalizedMissions data = new LocalizedMissions
                (missions.getValue().getData(), localization.getValue().getData());

        if (missions.getValue().getStatus() == ResourceStatus.UNAVAILABLE)
        {
            localizedMissions.setValue(Resource.newUnavailableResource(missions.getValue().getError(), data));
        }
        else if (localization.getValue().getStatus() == ResourceStatus.UNAVAILABLE)
        {
            localizedMissions.setValue(Resource.newUnavailableResource(localization.getValue().getError(), data));
        }
        else if (missions.getValue().getStatus() == ResourceStatus.PENDING ||
                localization.getValue().getStatus() == ResourceStatus.PENDING)
        {
            localizedMissions.setValue(Resource.newPendingResource(data));
        }
        else
        {
            localizedMissions.setValue(Resource.newAvailableResource(data));
        }
    }
}
