package ru.sp1.kidsapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import ru.sp1.kidsapp.AppData;
import ru.sp1.kidsapp.R;

public class NameInput extends DialogFragment
{

    ImageButton btReturn;
    Button btPlay;

    EditText inputUsername;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View frag =  inflater.inflate(R.layout.fragment_name_input, container, false);

        initComponents(frag);

        return frag;
    }


    private void initComponents(View view)
    {
        inputUsername = view.findViewById(R.id.inputUsername);

        btReturn = view.findViewById(R.id.btNameReturn);
        btReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dismiss();
            }
        });

        btPlay = view.findViewById(R.id.btNamePlay);
        btPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlayButton();
            }
        });
    }

    private void onPlayButton()
    {
        String nombre = String.valueOf(inputUsername.getText());
        if(nombre.isEmpty())
        {
            Toast.makeText(getContext(), R.string.inputNameError, Toast.LENGTH_SHORT).show();
            return;
        }

        AppData data = new ViewModelProvider(getActivity()).get(AppData.class);
        data.setUsername(nombre);
        data.saveUsername(getContext());

        dismiss();

        FragmentManager mng = getParentFragmentManager();
        mng.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentContainer, LevelSelector.class, null)
                .commit();

    }


}
