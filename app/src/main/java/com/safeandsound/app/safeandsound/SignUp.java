package com.safeandsound.app.safeandsound;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.util.concurrent.ExecutionException;

/**
 * Created by louisapabst on 22.04.17.
 */

public class SignUp extends FragmentActivity {

    private String username;
    private String password;
    private String email;
    private String ipAddressRP;



    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

    }

    public Boolean addUserToDB(String username, String password, String email, String ipAddressRP) {
        HttpURLConnection connection;
        OutputStreamWriter request = null;


        Boolean result = null;
        InputStream is = null;
        StringBuilder sb=null;

        //http post
        try {
            String urlString = "http://192.168.10.53/db_register.php";
            PostParams params = new PostParams(username, password, email, ipAddressRP, urlString);
            result = new ConnectionPOST().execute(params).get();
        }catch (InterruptedException e){
            e.printStackTrace();
        }catch (ExecutionException e){
            e.printStackTrace();
        }
        return result;
    }

    public void signup(View view) {
        final EditText username_ET = (EditText) findViewById(R.id.username_input);
        final EditText password_ET = (EditText) findViewById(R.id.password_input);
        final EditText email_ET = (EditText) findViewById(R.id.email_input);
        final EditText ipAddressRP_ET = (EditText) findViewById(R.id.ipAddress_input);

        username = username_ET.getText().toString();
        password = password_ET.getText().toString();
        email = email_ET.getText().toString();
        ipAddressRP = ipAddressRP_ET.getText().toString();
        Boolean result = addUserToDB(username, password, email, ipAddressRP);

        showMessage(result);
        }

    public void showMessage(Boolean b){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thank you for Signing Up!");
        TextView text = new TextView(this);
        if(b){
            text.setText("You're now signed up");

        } else{
            text.setText("Unfortunately something went wrong");
        }
        builder.show();
    }
}
