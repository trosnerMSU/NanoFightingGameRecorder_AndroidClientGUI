package client.application.ui.commands;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import client.application.R;

public class CommandsFragment extends Fragment {

    private CommandsViewModel commandsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        commandsViewModel =
                new ViewModelProvider(this).get(CommandsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_commands, container, false);
        final TextView textView = root.findViewById(R.id.text_commands);
        commandsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }

        });

        //Command Buttons
        Button startbtn = (Button) root.findViewById(R.id.startbtn);
        //Button stopbtn = (Button) findViewById(R.id.stopbtn);
        //Button unpairbtn = (Button) findViewById(R.id.unpairbtn);

        startbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                commandsViewModel.setText("Start Button Pressed");
            }
        });

        return root;
    }
}