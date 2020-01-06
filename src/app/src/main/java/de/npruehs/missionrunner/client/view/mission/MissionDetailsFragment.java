package de.npruehs.missionrunner.client.view.mission;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Vector;

import javax.inject.Inject;

import de.npruehs.missionrunner.client.R;
import de.npruehs.missionrunner.client.controller.mission.MissionComponent;
import de.npruehs.missionrunner.client.controller.mission.MissionComponentProvider;
import de.npruehs.missionrunner.client.model.character.Character;
import de.npruehs.missionrunner.client.model.mission.Mission;
import de.npruehs.missionrunner.client.model.mission.MissionDetails;
import de.npruehs.missionrunner.client.model.mission.MissionDetailsViewModel;
import de.npruehs.missionrunner.client.view.character.CharacterRecyclerViewAdapter;

public class MissionDetailsFragment extends Fragment implements Observer<MissionDetails>, CharacterRecyclerViewAdapter.OnCharacterSelectListener {
    @Inject
    MissionDetailsViewModel viewModel;

    private MissionCard missionCard;
    private RecyclerView recyclerViewAssignedCharacters;
    private RecyclerView recyclerViewUnassignedCharacters;

    private int missionId;

    private final Vector<Character> assignedCharacters = new Vector<>();
    private final Vector<Character> unassignedCharacters = new Vector<>();

    public MissionDetailsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mission_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Cache view references.
        missionCard = view.findViewById(R.id.missionCard);

        // Get id of mission to show.
        missionId = MissionDetailsFragmentArgs.fromBundle(getArguments()).getMissionId();

        // Set up list of assigned characters.
        recyclerViewAssignedCharacters = view.findViewById(R.id.recyclerViewAssignedCharacters);
        recyclerViewAssignedCharacters.setHasFixedSize(true);

        RecyclerView.LayoutManager recyclerViewLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewAssignedCharacters.setLayoutManager(recyclerViewLayoutManager);

        // Set up list of unassigned characters.
        recyclerViewUnassignedCharacters = view.findViewById(R.id.recyclerViewUnassignedCharacters);
        recyclerViewUnassignedCharacters.setHasFixedSize(true);

        recyclerViewLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewUnassignedCharacters.setLayoutManager(recyclerViewLayoutManager);

        // Bind view to view model.
        viewModel.getMissionDetails().observe(getViewLifecycleOwner(), this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (getActivity() instanceof MissionComponentProvider) {
            MissionComponent missionComponent = ((MissionComponentProvider)getActivity()).getMissionComponent();

            if (missionComponent != null) {
                missionComponent.inject(this);
            }
        }
    }

    @Override
    public void onChanged(MissionDetails missionDetails) {
        if (missionDetails == null) {
            return;
        }

        if (missionDetails.getMissions() != null) {
            for (Mission mission : missionDetails.getMissions()) {
                if (mission != null && mission.getId() == missionId) {
                    showMission(mission);
                    break;
                }
            }
        }

        if (missionDetails.getCharacters() != null) {
            showCharacters(missionDetails.getCharacters());
        }
    }

    private void showMission(Mission mission) {
        if (missionCard != null) {
            missionCard.setMission(mission);
        }
    }

    private void showCharacters(Character[] characters) {
        assignedCharacters.clear();
        unassignedCharacters.clear();

        for (Character character : characters) {
            switch (character.getStatus()) {
                case IDLE:
                    unassignedCharacters.add(character);
                    break;

                case MISSION:
                    assignedCharacters.add(character);
                    break;
            }
        }

        updateCharacters();
    }

    @Override
    public void onCharacterSelected(Character character) {
        if (assignedCharacters.remove(character)) {
            unassignedCharacters.add(character);
        } else if (unassignedCharacters.remove(character)) {
            assignedCharacters.add(character);
        }

        updateCharacters();
    }

    private void updateCharacters() {
        Character[] charactersToShow = new Character[assignedCharacters.size()];
        assignedCharacters.toArray(charactersToShow);
        CharacterRecyclerViewAdapter adapter = new CharacterRecyclerViewAdapter(charactersToShow);
        adapter.setCharacterSelectionListener(this);
        recyclerViewAssignedCharacters.setAdapter(adapter);

        charactersToShow = new Character[unassignedCharacters.size()];
        unassignedCharacters.toArray(charactersToShow);
        adapter = new CharacterRecyclerViewAdapter(charactersToShow);
        adapter.setCharacterSelectionListener(this);
        recyclerViewUnassignedCharacters.setAdapter(adapter);
    }
}
