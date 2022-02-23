package client.application.ui.commands;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import client.application.R;
import client.application.ui.MySingleton;
import client.application.ui.SaveRecordingDialogFragment;
import client.application.ui.connect.ConnectFragment;

public class CommandsFragment extends Fragment {

    private CommandsViewModel commandsViewModel;
    private String responseMsg;
    Button startbtn;
    Button stopbtn;
    Button unpairbtn;
    TextView resultText;

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

        //Command button declaration
        startbtn = (Button) root.findViewById(R.id.startbtn);
        stopbtn = (Button) root.findViewById(R.id.stopbtn);
        unpairbtn = (Button) root.findViewById(R.id.unpairbtn);
        resultText = (TextView) root.findViewById(R.id.commandResult);


        //Start button event handler
        startbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                if((ConnectFragment.getUrl() == null  || ConnectFragment.getUrl().isEmpty())
                    && !ConnectFragment.checkIfPaired()){
                    resultText.setText("You need to connect to host first");
                }else{
                    PostRequest(ConnectFragment.getUrl() + "/start", "Start", ConnectFragment.getMap());
                }
            }
        });

        //Stop button event handler
        stopbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((ConnectFragment.getUrl() == null || ConnectFragment.getUrl().isEmpty())
                        && !ConnectFragment.checkIfPaired()){
                    resultText.setText("You need to connect to host first");
                }else{
                    PostRequest(ConnectFragment.getUrl() + "/stop", "Stop", ConnectFragment.getMap());
                }
            }
        });

        //Unpair event handlers
        unpairbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((ConnectFragment.getUrl() == null || ConnectFragment.getUrl().isEmpty())
                        && !ConnectFragment.checkIfPaired()){
                    resultText.setText("You need to connect to host first");
                }else{
                    PostRequest(ConnectFragment.getUrl() + "/unpair", "Unpair", ConnectFragment.getMap());
                }
            }
        });

        return root;
    }

    public void PostRequest(String localUrl, String btnLabel, HashMap<String, String> body){

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, localUrl, new JSONObject(body),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Pull response message
                            responseMsg = response.getString("msg");
                            //Check which event handler is accessing this request and act accordingly
                            switch(btnLabel){
                                case "Start":
                                    resultText.setText("Recording is in progress!");
                                    break;
                                case "Stop":
                                    resultText.setText("Recording has stopped!");
                                    SaveRecordingDialogFragment dialog = new SaveRecordingDialogFragment();
                                    dialog.onCreateDialog(getActivity().getApplicationContext());
                                    break;
                                case "Unpair":
                                    resultText.setText("Disconnected from host device.");
                                    ConnectFragment.unpair();
                                    break;
                            }
                            //Log the response message for debugging purposes
                            Log.e("Response: ", responseMsg);

                        }catch(JSONException e){
                            Log.e("JSON", e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                switch(btnLabel){
                    case "Start":
                        resultText.setText("Device is already recording!");
                        break;
                    case "Stop":
                        resultText.setText("Device is already stopped!");
                        break;
                    case "Unpair":
                        resultText.setText("Device is already disconnected");
                        break;
                }

                Log.e("Commands Error", error.toString());
            }
        });

        // Add the request to the RequestQueue.
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }
}