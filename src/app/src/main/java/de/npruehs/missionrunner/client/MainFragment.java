package de.npruehs.missionrunner.client;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import de.npruehs.missionrunner.client.model.Account;
import de.npruehs.missionrunner.client.model.AccountViewModel;

public class MainFragment extends Fragment implements View.OnClickListener, Observer<Account> {
    private AccountViewModel viewModel;

    private TextView textViewAccountName;
    private TextView textViewAccountLevel;
    private TextView textViewAccountScore;

    private OnMainFragmentInteractionListener listener;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        // Register for button events.
        Button b = view.findViewById(R.id.buttonMissions);
        b.setOnClickListener(this);

        // Bind view to view model.
        textViewAccountName = view.findViewById(R.id.textViewAccountName);
        textViewAccountLevel = view.findViewById(R.id.textViewAccountLevelValue);
        textViewAccountScore = view.findViewById(R.id.textViewAccountScoreValue);

        viewModel = new AccountViewModel();
        viewModel.getAccount().observe(getViewLifecycleOwner(), this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonMissions:
                showMissions();
                break;
        }
    }

    @Override
    public void onChanged(Account account) {
        if (textViewAccountName != null) {
            textViewAccountName.setText(account.getAccountId());
        }

        if (textViewAccountLevel != null) {
            textViewAccountLevel.setText(Integer.toString(account.getLevel()));
        }

        if (textViewAccountScore != null) {
            textViewAccountScore.setText(Integer.toString(account.getScore()));
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
