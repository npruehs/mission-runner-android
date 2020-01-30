package de.npruehs.missionrunner.client.view.character;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.npruehs.missionrunner.client.model.character.Character;
import de.npruehs.missionrunner.client.model.localization.LocalizationData;

public class CharacterRecyclerViewAdapter extends RecyclerView.Adapter<CharacterRecyclerViewViewHolder> implements View.OnClickListener {
    private final Character[] characters;
    private final LocalizationData localization;

    private OnCharacterSelectListener listener;

    public CharacterRecyclerViewAdapter(Character[] characters, LocalizationData localization) {
        this.characters = characters;
        this.localization = localization;
    }

    @NonNull
    @Override
    public CharacterRecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CharacterCard characterCard = new CharacterCard(parent.getContext());
        characterCard.setOnClickListener(this);
        return new CharacterRecyclerViewViewHolder(characterCard);
    }

    @Override
    public void onBindViewHolder(@NonNull CharacterRecyclerViewViewHolder holder, int position) {
        Character character = characters[position];
        holder.getCharacterCard().setCharacter(character, localization);
    }

    @Override
    public int getItemCount() {
        return characters.length;
    }

    @Override
    public void onClick(View v) {
        if (listener == null) {
            return;
        }

        CharacterCard characterCard = (CharacterCard)v;
        listener.onCharacterSelected(characterCard.getCharacter());
    }

    public void setCharacterSelectionListener(OnCharacterSelectListener listener) {
        this.listener = listener;
    }

    public interface OnCharacterSelectListener {
        void onCharacterSelected(Character character);
    }
}
