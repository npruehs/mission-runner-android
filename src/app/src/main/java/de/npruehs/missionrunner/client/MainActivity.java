package de.npruehs.missionrunner.client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import de.npruehs.missionrunner.client.controller.AccountComponent;

public class MainActivity extends AppCompatActivity implements MainFragment.OnMainFragmentInteractionListener {
    AccountComponent accountComponent;

    private NavController navController;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        accountComponent = ((MissionRunnerApplication)getApplicationContext())
                .appComponent.accountComponent().create();

        setContentView(R.layout.activity_main);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph()).build();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (NavigationUI.navigateUp(navController, appBarConfiguration)) {
            return true;
        }

        return super.onSupportNavigateUp();
    }

    @Override
    public void onShowMissions() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            navController.navigate(R.id.showMissionsFragmentLand);
        } else {
            NavDirections action = MainFragmentDirections.actionMainFragmentToShowMissionsFragment();
            navController.navigate(action);
        }
    }
}
