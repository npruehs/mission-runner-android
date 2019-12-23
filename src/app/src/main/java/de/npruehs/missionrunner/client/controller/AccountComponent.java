package de.npruehs.missionrunner.client.controller;

import dagger.Subcomponent;
import de.npruehs.missionrunner.client.ActivityScope;
import de.npruehs.missionrunner.client.MainFragment;

@ActivityScope
@Subcomponent
public interface AccountComponent {

    @Subcomponent.Factory
    interface Factory {
        AccountComponent create();
    }

    void inject(MainFragment mainFragment);
}
