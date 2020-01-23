package de.npruehs.missionrunner.client.view.mission;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import javax.inject.Inject;

import de.npruehs.missionrunner.client.R;
import de.npruehs.missionrunner.client.controller.mission.MissionComponent;
import de.npruehs.missionrunner.client.controller.mission.MissionComponentProvider;
import de.npruehs.missionrunner.client.model.Resource;
import de.npruehs.missionrunner.client.model.mission.Mission;
import de.npruehs.missionrunner.client.model.mission.MissionViewModel;

public class ShowMissionsFragment extends Fragment implements Observer<Resource<Mission[]>>, MissionRecyclerViewAdapter.OnMissionSelectListener {
    @Inject
    MissionViewModel viewModel;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private OnShowMissionsFragmentInteractionListener listener;

    public ShowMissionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
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

        progressBar = view.findViewById(R.id.progressBar);

        // Bind view to view model.
        viewModel.getMissions().observe(getViewLifecycleOwner(), this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (getActivity() instanceof  MissionComponentProvider) {
            MissionComponent missionComponent = ((MissionComponentProvider)getActivity()).getMissionComponent();

            if (missionComponent != null) {
                missionComponent.inject(this);
            }
        }

        if (context instanceof OnShowMissionsFragmentInteractionListener) {
            listener = (OnShowMissionsFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnShowMissionsFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        listener = null;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_missions, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_home:
                showHome();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onChanged(Resource<Mission[]> missions) {
        if (missions.getData() != null) {
            MissionRecyclerViewAdapter adapter = new MissionRecyclerViewAdapter(missions.getData());
            adapter.setMissionSelectionListener(this);
            recyclerView.setAdapter(adapter);
        }

        // Update loading indicator.
        switch (missions.getStatus()) {
            case AVAILABLE:
                if (progressBar != null) {
                    progressBar.setVisibility(View.INVISIBLE);
                }
                break;

            case PENDING:
                if (progressBar != null) {
                    progressBar.setVisibility(View.VISIBLE);
                }
                break;

            case UNAVAILABLE:
                if (progressBar != null) {
                    progressBar.setVisibility(View.INVISIBLE);
                }

                Toast.makeText(getContext(), missions.getError(), Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onMissionSelected(Mission mission) {
        if (mission != null) {
            showMission(mission.getId());
        }
    }

    private void showHome() {
        if (listener != null) {
            listener.onShowHome();
        }
    }

    private void showMission(int missionId) {
        if (listener != null) {
            listener.onShowMission(missionId);
        }
    }

    public interface OnShowMissionsFragmentInteractionListener {
        void onShowHome();
        void onShowMission(int missionId);
    }
}
