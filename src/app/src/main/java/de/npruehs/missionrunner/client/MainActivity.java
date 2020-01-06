package de.npruehs.missionrunner.client;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import de.npruehs.missionrunner.client.controller.ApplicationComponent;
import de.npruehs.missionrunner.client.controller.account.AccountComponent;
import de.npruehs.missionrunner.client.controller.account.AccountComponentProvider;
import de.npruehs.missionrunner.client.controller.character.CharacterComponent;
import de.npruehs.missionrunner.client.controller.mission.MissionComponent;
import de.npruehs.missionrunner.client.controller.mission.MissionComponentProvider;

public class MainActivity
        extends AppCompatActivity
        implements MainFragment.OnMainFragmentInteractionListener, AccountComponentProvider, MissionComponentProvider {
    private AccountComponent accountComponent;
    private MissionComponent missionComponent;
    private CharacterComponent characterComponent;

    private NavController navController;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ApplicationComponent applicationComponent = ((MissionRunnerApplication)getApplicationContext())
                .appComponent;

        accountComponent = applicationComponent.accountComponent().create();
        missionComponent = applicationComponent.missionComponent().create();
        characterComponent = applicationComponent.characterComponent().create();

        setContentView(R.layout.activity_main);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph()).build();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
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

    public AccountComponent getAccountComponent() {
        return accountComponent;
    }

    public MissionComponent getMissionComponent() {
        return missionComponent;
    }
}
