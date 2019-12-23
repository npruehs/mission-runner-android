package de.npruehs.missionrunner.client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import de.npruehs.missionrunner.client.controller.AccountComponent;

public class MainActivity extends AppCompatActivity implements MainFragment.OnMainFragmentInteractionListener {
    AccountComponent accountComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        accountComponent = ((MissionRunnerApplication)getApplicationContext())
                .appComponent.accountComponent().create();

        setContentView(R.layout.activity_main);
    }

    @Override
    public void onShowMissions() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Check what fragment is currently shown, replace if needed.
            ShowMissionsFragment showMissionsFragment = (ShowMissionsFragment)
                    getSupportFragmentManager().findFragmentById(R.id.frameLayoutDetails);
            if (showMissionsFragment == null) {
                // Make new fragment to show this selection.
                showMissionsFragment = ShowMissionsFragment.newInstance();

                // Execute a transaction, replacing any existing fragment
                // with this one inside the frame.
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frameLayoutDetails, showMissionsFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
        } else {
            Intent intent = new Intent(this, ShowMissionsActivity.class);
            startActivity(intent);
        }
    }
}
