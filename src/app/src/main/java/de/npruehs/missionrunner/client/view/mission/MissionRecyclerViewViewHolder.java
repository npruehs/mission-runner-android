package de.npruehs.missionrunner.client.view.mission;

import androidx.recyclerview.widget.RecyclerView;

public class MissionRecyclerViewViewHolder extends RecyclerView.ViewHolder {
    private final MissionCard missionCard;

    public MissionRecyclerViewViewHolder(MissionCard missionCard) {
        super(missionCard);

        this.missionCard = missionCard;
    }

    public MissionCard getMissionCard() {
        return missionCard;
    }
}
