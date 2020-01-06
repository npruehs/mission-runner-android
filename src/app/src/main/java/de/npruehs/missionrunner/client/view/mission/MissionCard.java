package de.npruehs.missionrunner.client.view.mission;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import de.npruehs.missionrunner.client.R;
import de.npruehs.missionrunner.client.model.mission.Mission;
import de.npruehs.missionrunner.client.model.mission.MissionRequirement;

public class MissionCard extends CardView {
    private TextView textViewMissionName;
    private TextView textViewMissionStatus;
    private TextView textViewMissionRequirements;
    private TextView textViewMissionTime;
    private TextView textViewMissionRewards;

    private Mission mission;

    public MissionCard(@NonNull Context context) {
        super(context);

        inflateMissionCard(context);
    }

    public MissionCard(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        inflateMissionCard(context);
    }

    public MissionCard(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflateMissionCard(context);
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;

        if (textViewMissionName != null) {
            textViewMissionName.setText(mission.getName());
        }

        if (textViewMissionStatus != null) {
            textViewMissionStatus.setText(mission.getStatus().toString());
        }

        if (textViewMissionRequirements != null) {
            MissionRequirement[] missionRequirements = mission.getRequirements();
            StringBuilder missionRequirementsString = new StringBuilder();

            for (int i = 0; i < missionRequirements.length; ++i) {
                if (i > 0) {
                    missionRequirementsString.append(", ");
                }

                missionRequirementsString.append(missionRequirements[i].getRequirement());

                if (missionRequirements[i].getCount() > 1) {
                    missionRequirementsString.append(" x");
                    missionRequirementsString.append(missionRequirements[i].getCount());
                }
            }

            textViewMissionRequirements.setText(missionRequirementsString.toString());
        }

        if (textViewMissionTime != null) {
            textViewMissionTime.setText(Integer.toString(mission.getRequiredTime()));
        }

        if (textViewMissionRewards != null) {
            textViewMissionRewards.setText(Integer.toString(mission.getReward()));
        }
    }

    private void inflateMissionCard(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.card_mission, this);

        textViewMissionName = findViewById(R.id.textViewMissionName);
        textViewMissionStatus = findViewById(R.id.textViewMissionStatus);
        textViewMissionRequirements = findViewById(R.id.textViewMissionRequirements);
        textViewMissionTime = findViewById(R.id.textViewMissionTimeValue);
        textViewMissionRewards = findViewById(R.id.textViewMissionRewardsValue);
    }


}
