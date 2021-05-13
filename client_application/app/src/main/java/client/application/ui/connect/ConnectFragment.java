package client.application.ui.connect;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import client.application.R;
import client.application.ui.MySingleton;

public class ConnectFragment extends Fragment {

    private ConnectViewModel connectViewModel;
    private RequestQueue myQueue;
    String url;
    TextView textConnectionResult;
    Button ConnectBtn;
    EditText textDomainName;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        connectViewModel =
                new ViewModelProvider(this).get(ConnectViewModel.class);
        View root = inflater.inflate(R.layout.fragment_connect, container, false);
        final TextView textView = root.findViewById(R.id.text_connect);
        connectViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        //Connect Widgets
        textConnectionResult = (TextView) root.findViewById(R.id.textConnectionResult);
        ConnectBtn = (Button) root.findViewById(R.id.ConnectBtn);
        textDomainName = (EditText) root.findViewById(R.id.editTextDomainEndpoint);

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        ConnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sync("https://10.0.2.2:3000/");
            }
        });

        return root;
    }

    public void Sync(String url){

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Display the first 500 characters of the response string.
                        textConnectionResult.setText("Response is: "+ response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textConnectionResult.setText("That didn't work! Domain might be incorrect or unavailable!" + url);
            }
        });

        // Add the request to the RequestQueue.
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    private String getUrl(){
        return url;
    }
}