package de.npruehs.missionrunner.client.view.mission;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Period;

import de.npruehs.missionrunner.client.R;
import de.npruehs.missionrunner.client.model.mission.Mission;
import de.npruehs.missionrunner.client.model.mission.MissionRequirement;
import de.npruehs.missionrunner.client.model.mission.MissionStatus;

public class MissionCard extends CardView {
    private TextView textViewMissionName;
    private TextView textViewMissionStatus;
    private TextView textViewMissionRequirements;
    private TextView textViewMissionTime;
    private TextView textViewMissionRewards;

    private Mission mission;

    private CountDownTimer missionCountdown;

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

    public void setMission(final Mission mission) {
        this.mission = mission;

        if (textViewMissionName != null) {
            textViewMissionName.setText(mission.getName());
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
            switch (mission.getStatus()) {
                case OPEN:
                    textViewMissionTime.setText(Integer.toString(mission.getRequiredTime()));
                    break;

                case RUNNING:
                    textViewMissionTime.setText(Integer.toString(mission.getRequiredTime()));

                    final Mission currentMission = mission;

                    if (missionCountdown != null) {
                        missionCountdown.cancel();
                    }

                    missionCountdown = new CountDownTimer(mission.getRequiredTime() * 1000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            textViewMissionTime.setText(Integer.toString(mission.getRemainingSeconds()));
                        }

                        @Override
                        public void onFinish() {
                            textViewMissionTime.setText("0");

                            updateStatus();
                        }
                    };
                    missionCountdown.start();
                    break;

                case FINISHED:
                    textViewMissionTime.setText("0");
                    break;
            }
        }

        if (textViewMissionRewards != null) {
            textViewMissionRewards.setText(Integer.toString(mission.getReward()));
        }

        updateStatus();
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

    private void updateStatus() {
        if (mission == null || textViewMissionStatus == null) {
            return;
        }

        if (mission.isRunning()) {
            textViewMissionStatus.setText(getContext().getString(R.string.mission_text_status_running));
        } else if (mission.isFinished()) {
            textViewMissionStatus.setText(getContext().getString(R.string.mission_text_status_finished));
        } else {
            textViewMissionStatus.setText(getContext().getString(R.string.mission_text_status_open));
        }
    }
}
