package de.npruehs.missionrunner.client.view.mission;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.npruehs.missionrunner.client.model.mission.Mission;

public class MissionRecyclerViewAdapter extends RecyclerView.Adapter<MissionRecyclerViewViewHolder> implements View.OnClickListener {
    private Mission[] missions;
    private OnMissionSelectListener listener;

    public MissionRecyclerViewAdapter(Mission[] missions) {
        this.missions = missions;
    }

    @NonNull
    @Override
    public MissionRecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MissionCard missionCard = new MissionCard(parent.getContext());
        missionCard.setOnClickListener(this);
        return new MissionRecyclerViewViewHolder(missionCard);
    }

    @Override
    public void onBindViewHolder(@NonNull MissionRecyclerViewViewHolder holder, int position) {
        Mission mission = missions[position];
        holder.missionCard.setMission(mission);
    }

    @Override
    public int getItemCount() {
        return missions.length;
    }

    @Override
    public void onClick(View v) {
        if (listener == null) {
            return;
        }

        MissionCard missionCard = (MissionCard)v;
        listener.onMissionSelected(missionCard.getMission());
    }

    public void setMissionSelectionListener(OnMissionSelectListener listener) {
        this.listener = listener;
    }

    public interface OnMissionSelectListener {
        void onMissionSelected(Mission mission);
    }
}
