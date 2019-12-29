package de.npruehs.missionrunner.client.controller.mission;

import dagger.Subcomponent;
import de.npruehs.missionrunner.client.ActivityScope;
import de.npruehs.missionrunner.client.ShowMissionsFragment;

@ActivityScope
@Subcomponent
public interface MissionComponent {
    @Subcomponent.Factory
    interface Factory {
        MissionComponent create();
    }

    void inject(ShowMissionsFragment showMissionsFragment);
}
