package ru.sp1.kidsapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import ru.sp1.kidsapp.AppData;
import ru.sp1.kidsapp.R;


public class MainMenu extends Fragment
{
    String username;

    TextView textWelcome;
    Button btPlay;

    ImageButton btScoreboard, btSettings;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View frag =  inflater.inflate(R.layout.fragment_main_menu, container, false);

        initComponents(frag);


        return frag;
    }

    private void initComponents(View view)
    {
        AppData data = new ViewModelProvider(getActivity()).get(AppData.class);
        data.loadScoresFromDB();
        data.loadUsername(getContext());
        username = data.getUsername();

        textWelcome = view.findViewById(R.id.textInfo);
        if(username == null || username.isEmpty())
        {
            textWelcome.setVisibility(View.INVISIBLE);
        } else
        {
            String msg = getResources().getString(R.string.menuWelcomeText, username);
            textWelcome.setText(msg);
        }


        btPlay = view.findViewById(R.id.btPlay);
        btPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playButton();
            }
        });

        btScoreboard = view.findViewById(R.id.btStats);
        btScoreboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager mng = getParentFragmentManager();
                mng.beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragmentContainer, Scoreboard.class, null)
                        .commit();
            }
        });

        btSettings = view.findViewById(R.id.btSettings);
        btSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager mng = getParentFragmentManager();
                mng.beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragmentContainer, AppSettings.class, null)
                        .commit();
            }
        });
    }



    private void playButton()
    {
        username = new ViewModelProvider(getActivity()).get(AppData.class).getUsername();

        if(username == null || username.isEmpty())
        {
            NameInput nameDialog = new NameInput();
            nameDialog.show(getParentFragmentManager(), "nameDialog");

            return;
        }

        FragmentManager mng = getParentFragmentManager();
        mng.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentContainer, LevelSelector.class, null)
                .commit();
    }
}