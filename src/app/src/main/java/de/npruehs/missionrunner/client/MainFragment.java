package de.npruehs.missionrunner.client;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import javax.inject.Inject;

import de.npruehs.missionrunner.client.controller.account.AccountComponent;
import de.npruehs.missionrunner.client.controller.account.AccountComponentProvider;
import de.npruehs.missionrunner.client.model.Resource;
import de.npruehs.missionrunner.client.model.account.Account;
import de.npruehs.missionrunner.client.model.account.AccountViewModel;

public class MainFragment extends Fragment implements Observer<Resource<Account>> {
    @Inject
    AccountViewModel viewModel;

    private TextView textViewAccountName;
    private TextView textViewAccountLevel;
    private TextView textViewAccountScore;
    private ProgressBar progressBar;

    private OnMainFragmentInteractionListener listener;

    public MainFragment() {
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
        // Inflate the layout for this fragment.
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Bind view to view model.
        textViewAccountName = view.findViewById(R.id.textViewAccountName);
        textViewAccountLevel = view.findViewById(R.id.textViewAccountLevelValue);
        textViewAccountScore = view.findViewById(R.id.textViewAccountScoreValue);
        progressBar = view.findViewById(R.id.progressBar);

        viewModel.getAccount().observe(getViewLifecycleOwner(), this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (getActivity() instanceof  AccountComponentProvider) {
            AccountComponent accountComponent = ((AccountComponentProvider)getActivity()).getAccountComponent();

            if (accountComponent != null) {
                accountComponent.inject(this);
            }
        }

        if (context instanceof OnMainFragmentInteractionListener) {
            listener = (OnMainFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMainFragmentInteractionListener");
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

        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_missions:
                showMissions();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onChanged(Resource<Account> account) {
        // Update data.
        if (account.getData() != null) {
            if (textViewAccountName != null) {
                textViewAccountName.setText(account.getData().getName());
            }

            if (textViewAccountLevel != null) {
                textViewAccountLevel.setText(Integer.toString(account.getData().getLevel()));
            }

            if (textViewAccountScore != null) {
                textViewAccountScore.setText(Integer.toString(account.getData().getScore()));
            }
        }

        // Update loading indicator.
        switch (account.getStatus()) {
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

                Toast.makeText(getContext(), account.getError(), Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void showMissions() {
        if (listener != null) {
            listener.onShowMissions();
        }
    }

    public interface OnMainFragmentInteractionListener {
        void onShowMissions();
    }
}
