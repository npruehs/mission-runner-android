package de.npruehs.missionrunner.client.view.character;

import androidx.recyclerview.widget.RecyclerView;

public class CharacterRecyclerViewViewHolder extends RecyclerView.ViewHolder {
    private final CharacterCard characterCard;

    public CharacterRecyclerViewViewHolder(CharacterCard characterCard) {
        super(characterCard);

        this.characterCard = characterCard;
    }

    public CharacterCard getCharacterCard() {
        return characterCard;
    }
}
