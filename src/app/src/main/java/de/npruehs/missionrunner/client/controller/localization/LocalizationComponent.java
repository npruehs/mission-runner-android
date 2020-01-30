package de.npruehs.missionrunner.client.controller.localization;

import dagger.Subcomponent;
import de.npruehs.missionrunner.client.ActivityScope;

@ActivityScope
@Subcomponent
public interface LocalizationComponent {
    @Subcomponent.Factory
    interface Factory {
        LocalizationComponent create();
    }
}
