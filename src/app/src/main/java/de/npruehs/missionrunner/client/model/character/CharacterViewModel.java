package de.npruehs.missionrunner.client.model.character;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import de.npruehs.missionrunner.client.ActivityScope;
import de.npruehs.missionrunner.client.controller.character.CharacterRepository;
import de.npruehs.missionrunner.client.model.Resource;

@ActivityScope
public class CharacterViewModel extends ViewModel {
    private final CharacterRepository characterRepository;
    private final LiveData<Resource<Character[]>> characters;

    @Inject
    public CharacterViewModel(CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;

        characters = characterRepository.getCharacters();
    }

    public LiveData<Resource<Character[]>> getCharacters() {
        return characters;
    }
}
