package de.npruehs.missionrunner.client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

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
        View navHostView = findViewById(R.id.nav_host_fragment);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Navigation.findNavController(navHostView).navigate(R.id.showMissionsFragmentLand);
        } else {
            NavDirections action = MainFragmentDirections.actionMainFragmentToShowMissionsFragment();
            Navigation.findNavController(navHostView).navigate(action);
        }
    }
}
