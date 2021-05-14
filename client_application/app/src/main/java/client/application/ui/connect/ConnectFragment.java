package client.application.ui.connect;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import client.application.R;
import client.application.ui.MySingleton;

public class ConnectFragment extends Fragment {

    private static String url;
    private static String pairKey;
    private static boolean isPaired = false;
    private ConnectViewModel connectViewModel;
    private RequestQueue myQueue;
    TextView textConnectionResult;
    Button ConnectBtn;
    EditText textDomainName;

    //HashMap with the post request body which will hold the pair key
    //This is what will be sent to the host server and will allow
    //The user to actually gain access to the start, stop, unpair buttons
    private static HashMap<String, String> map = new HashMap<String, String>();

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

        //For Https self-signed CA connections
        handleSSLHandshake();

        //Connect Widgets
        textConnectionResult = (TextView) root.findViewById(R.id.textConnectionResult);
        ConnectBtn = (Button) root.findViewById(R.id.ConnectBtn);
        textDomainName = (EditText) root.findViewById(R.id.editTextDomainEndpoint);

        MySingleton.getInstance(getActivity().getApplicationContext()).getRequestQueue();
        ConnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String localUrl = textDomainName.getText().toString();

                if(localUrl.isEmpty()){
                    textConnectionResult.setText("No URL is given!");
                }else if(localUrl.contains("https://") || localUrl.contains("http://")){
                    localUrl += "/nano/pair";
                    JsonSync(localUrl);
                }else{
                    textConnectionResult.setText("Network protocol is missing! (Ex. https:// )");
                }
            }
        });
        return root;
    }

    public void StringSync(String localUrl){

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, localUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        textConnectionResult.setText("Response is: "+ response.substring(0,100));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textConnectionResult.setText("That didn't work! Domain might be incorrect or unavailable!" + localUrl);
            }
        });

        // Add the request to the RequestQueue.
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void JsonSync(String localUrl){

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, localUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            //Now we have a valid api that we are paired to
                            //We set the url equal to the local url so that the Commands Fragment
                            //Can access the url that we are paired to.
                            pairKey = response.getString("msg");
                            map.clear();
                            map.put("key", pairKey);
                            url = localUrl.replace("/pair","");
                            isPaired = true;
                            textConnectionResult.setText("Successfully paired!");
                        }catch(JSONException e){
                            Log.e("JSON Error",e.toString());
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error.networkResponse == null){
                    textConnectionResult.setText("Invalid host");
                    Log.e("UnknownHostException",error.toString());
                }else if(error.networkResponse.statusCode == 401){
                    textConnectionResult.setText("Device is already in use. Unauthorized " + error.networkResponse.statusCode);
                    Log.e("Unauthorized",error.toString());
                }
            }
        });

        // Add the request to the RequestQueue.
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    @SuppressLint("TrulyRandom")
    public static void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }
    }

    public static String getUrl(){
        return url;
    }

    public static String getPairKey(){
        return pairKey;
    }

    public static boolean checkIfPaired(){
        return isPaired;
    }

    public static void unpair(){
        isPaired = false;
        url = "";
        map.clear();
    }

    public static HashMap<String, String> getMap(){
        return map;
    }

}