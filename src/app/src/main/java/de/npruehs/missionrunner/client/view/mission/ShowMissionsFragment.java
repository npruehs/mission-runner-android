package de.npruehs.missionrunner.client.view.mission;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import javax.inject.Inject;

import de.npruehs.missionrunner.client.R;
import de.npruehs.missionrunner.client.controller.mission.MissionComponentProvider;
import de.npruehs.missionrunner.client.model.Resource;
import de.npruehs.missionrunner.client.model.mission.Mission;
import de.npruehs.missionrunner.client.model.mission.MissionViewModel;

public class ShowMissionsFragment extends Fragment implements Observer<Resource<Mission[]>>, MissionRecyclerViewAdapter.OnMissionSelectListener {
    @Inject
    MissionViewModel viewModel;

    private RecyclerView recyclerView;

    public ShowMissionsFragment() {
        // Required empty public constructor
    }

    public static ShowMissionsFragment newInstance() {
        ShowMissionsFragment f = new ShowMissionsFragment();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_missions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Register for list item events.
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager recyclerViewLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(recyclerViewLayoutManager);

        // Bind view to view model.
        viewModel.getMissions().observe(getViewLifecycleOwner(), this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        ((MissionComponentProvider)getActivity()).getMissionComponent().inject(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onChanged(Resource<Mission[]> missions) {
        if (missions.getData() != null) {
            MissionRecyclerViewAdapter adapter = new MissionRecyclerViewAdapter(missions.getData());
            adapter.setMissionSelectionListener(this);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onMissionSelected(Mission mission) {
        Toast.makeText(getContext(), mission.getName(), Toast.LENGTH_SHORT).show();
    }

    public interface OnShowMissionsFragmentInteractionListener {
    }
}
