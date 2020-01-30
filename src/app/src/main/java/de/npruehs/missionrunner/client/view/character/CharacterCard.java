package de.npruehs.missionrunner.client.view.character;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import de.npruehs.missionrunner.client.R;
import de.npruehs.missionrunner.client.model.character.Character;
import de.npruehs.missionrunner.client.model.character.CharacterSkill;
import de.npruehs.missionrunner.client.model.localization.LocalizationData;

public class CharacterCard extends CardView {
    private TextView textViewCharacterName;
    private TextView textViewCharacterSkills;

    private Character character;
    private LocalizationData localization;

    public CharacterCard(@NonNull Context context) {
        super(context);

        inflateCharacterCard(context);
    }

    public CharacterCard(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        inflateCharacterCard(context);
    }

    public CharacterCard(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflateCharacterCard(context);
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character, LocalizationData localization) {
        this.character = character;
        this.localization = localization;

        if (textViewCharacterName != null) {
            textViewCharacterName.setText(localization.get(character.getName()));
        }

        if (textViewCharacterSkills != null) {
            CharacterSkill[] characterSkills = character.getSkills();
            StringBuilder characterSkillsString = new StringBuilder();

            for (int i = 0; i < characterSkills.length; ++i) {
                if (i > 0) {
                    characterSkillsString.append(", ");
                }

                characterSkillsString.append(localization.get(characterSkills[i].getSkill()));

                if (characterSkills[i].getCount() > 1) {
                    characterSkillsString.append(" x");
                    characterSkillsString.append(characterSkills[i].getCount());
                }
            }

            textViewCharacterSkills.setText(characterSkillsString.toString());
        }
    }

    private void inflateCharacterCard(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.card_character, this);

        textViewCharacterName = findViewById(R.id.textViewCharacterName);
        textViewCharacterSkills = findViewById(R.id.textViewCharacterSkills);
    }
}
