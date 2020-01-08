package de.npruehs.missionrunner.client.view.mission;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import de.npruehs.missionrunner.client.R;
import de.npruehs.missionrunner.client.controller.mission.MissionComponent;
import de.npruehs.missionrunner.client.controller.mission.MissionComponentProvider;
import de.npruehs.missionrunner.client.model.character.Character;
import de.npruehs.missionrunner.client.model.character.CharacterSkill;
import de.npruehs.missionrunner.client.model.character.CharacterStatus;
import de.npruehs.missionrunner.client.model.mission.Mission;
import de.npruehs.missionrunner.client.model.mission.MissionDetails;
import de.npruehs.missionrunner.client.model.mission.MissionDetailsViewModel;
import de.npruehs.missionrunner.client.model.mission.MissionRequirement;
import de.npruehs.missionrunner.client.view.character.CharacterRecyclerViewAdapter;

public class MissionDetailsFragment
        extends Fragment
        implements Observer<MissionDetails>,
        CharacterRecyclerViewAdapter.OnCharacterSelectListener,
        View.OnClickListener,
        MissionCard.OnMissionFinishListener {
    @Inject
    MissionDetailsViewModel viewModel;

    private MissionCard missionCard;
    private RecyclerView recyclerViewAssignedCharacters;
    private TextView textViewUnassignedCharacters;
    private RecyclerView recyclerViewUnassignedCharacters;
    private FloatingActionButton buttonStart;
    private FloatingActionButton buttonFinish;

    private int missionId;

    private final ArrayList<Character> assignedCharacters = new ArrayList<>();
    private final ArrayList<Character> unassignedCharacters = new ArrayList<>();

    private OnMissionDetailsFragmentInteractionListener listener;

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
        textViewUnassignedCharacters = view.findViewById(R.id.textViewUnassignedCharacters);

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

        // Set up button listeners.
        buttonStart = view.findViewById(R.id.floatingActionButtonStart);
        buttonFinish = view.findViewById(R.id.floatingActionButtonFinish);

        if (buttonStart != null) {
            buttonStart.setOnClickListener(this);
        }

        if (buttonFinish != null) {
            buttonFinish.setOnClickListener(this);
        }

        // Set up mission listener.
        if (missionCard != null) {
            missionCard.setMissionFinishedListener(this);
        }
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

        if (context instanceof OnMissionDetailsFragmentInteractionListener) {
            listener = (OnMissionDetailsFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMissionDetailsFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        listener = null;
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

        boolean canStartMission = !mission.isRunning() && !mission.isFinished();

        textViewUnassignedCharacters.setVisibility(canStartMission ? View.VISIBLE : View.GONE);
        recyclerViewUnassignedCharacters.setVisibility(canStartMission ? View.VISIBLE : View.GONE);

        updateButtons();
    }

    private void showCharacters(Character[] characters) {
        assignedCharacters.clear();
        unassignedCharacters.clear();

        for (Character character : characters) {
            if (character.getStatus() == CharacterStatus.IDLE) {
                unassignedCharacters.add(character);
            } else if (character.getMissionId() == missionId) {
                assignedCharacters.add(character);
            }
        }

        updateCharacters();
    }

    @Override
    public void onCharacterSelected(Character character) {
        if (missionCard == null) {
            return;
        }

        Mission mission = missionCard.getMission();

        if (mission == null) {
            return;
        }

        if (mission.isRunning() || mission.isFinished()) {
            return;
        }

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.floatingActionButtonStart:
                startMission();
                break;

            case R.id.floatingActionButtonFinish:
                finishMission();
                break;
        }
    }

    @Override
    public void onMissionFinished(Mission mission) {
        updateButtons();
    }

    private void startMission() {
        if (!canStartMission()) {
            Snackbar.make(buttonStart, R.string.error_mission_requirements_not_met, Snackbar.LENGTH_SHORT)
                    .show();
            return;
        }

        // Start mission.
        int[] characterIds = new int[assignedCharacters.size()];

        for (int i = 0; i < assignedCharacters.size(); ++i) {
            characterIds[i] = assignedCharacters.get(i).getId();
        }

        viewModel.startMission(missionId, characterIds);

        // Return to mission list.
        returnToMissions();
    }

    private void finishMission() {
        // Finish mission.
        viewModel.finishMission(missionId);

        // Return to mission list.
        returnToMissions();
    }

    private void returnToMissions() {
        if (listener != null) {
            listener.onReturnToMissions();
        }
    }

    private void updateButtons() {
        if (missionCard == null) {
            return;
        }

        Mission mission = missionCard.getMission();

        if (mission == null) {
            return;
        }

        if (mission.isRunning()) {
            buttonStart.setVisibility(View.GONE);
            buttonFinish.setVisibility(View.GONE);
        } else if (mission.isFinished()) {
            buttonStart.setVisibility(View.GONE);
            buttonFinish.setVisibility(View.VISIBLE);
        } else {
            buttonStart.setVisibility(View.VISIBLE);
            buttonFinish.setVisibility(View.GONE);
        }
    }

    private boolean canStartMission() {
        // Get mission requirements.
        Mission mission = missionCard.getMission();

        if (mission == null) {
            return false;
        }

        HashMap<String, Integer> requirements = new HashMap<>();

        for (MissionRequirement requirement : mission.getRequirements()) {
            requirements.put(requirement.getRequirement(), requirement.getCount());
        }

        // Apply character skills.
        for (Character character : assignedCharacters) {
            for (CharacterSkill skill : character.getSkills()) {
                if (requirements.containsKey(skill.getSkill())) {
                    Integer requiredSkills = requirements.get(skill.getSkill());

                    if (requiredSkills > 0) {
                        requiredSkills -= skill.getCount();
                        requirements.put(skill.getSkill(), requiredSkills);
                    }
                }
            }
        }

        // Check if all requirements are met.
        for (Integer requiredSkills : requirements.values()) {
            if (requiredSkills > 0){
                return false;
            }
        }

        return true;
    }

    public interface OnMissionDetailsFragmentInteractionListener {
        void onReturnToMissions();
    }
}
