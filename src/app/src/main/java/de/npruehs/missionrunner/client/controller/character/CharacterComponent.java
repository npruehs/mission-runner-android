package de.npruehs.missionrunner.client.controller.character;

import dagger.Subcomponent;
import de.npruehs.missionrunner.client.ActivityScope;

@ActivityScope
@Subcomponent
public interface CharacterComponent {
    @Subcomponent.Factory
    interface Factory {
        CharacterComponent create();
    }
}
