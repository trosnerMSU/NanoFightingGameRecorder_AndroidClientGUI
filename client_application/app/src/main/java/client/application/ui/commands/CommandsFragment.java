package client.application.ui.commands;

import android.os.Bundle;
import android.util.Log;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import client.application.R;
import client.application.ui.MySingleton;
import client.application.ui.connect.ConnectFragment;

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

        //For Https self-signed CA connections
        ConnectFragment.handleSSLHandshake();

        //Command Buttons
        Button startbtn = (Button) root.findViewById(R.id.startbtn);
        Button stopbtn = (Button) root.findViewById(R.id.stopbtn);
        Button unpairbtn = (Button) root.findViewById(R.id.unpairbtn);
        TextView resultText = (TextView) root.findViewById(R.id.commandResult);


        startbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                if(ConnectFragment.getUrl().equals(null) || ConnectFragment.getUrl().isEmpty()){
                    resultText.setText("URL is inactive");
                }else{
                    PostRequest(ConnectFragment.getUrl(), "Start");
                }
            }
        });

        stopbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ConnectFragment.getUrl().equals(null) || ConnectFragment.getUrl().isEmpty()){
                    resultText.setText("URL is inactive");
                }else{
                    PostRequest(ConnectFragment.getUrl(), "Stop");
                }
            }
        });

        unpairbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ConnectFragment.getUrl().equals(null) || ConnectFragment.getUrl().isEmpty()){
                    resultText.setText("URL is inactive");
                }else{
                    PostRequest(ConnectFragment.getUrl(), "Unpair");
                }
            }
        });

        return root;
    }

    public void PostRequest(String localUrl, String btnLabel){

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, localUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        // Add the request to the RequestQueue.
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }
}