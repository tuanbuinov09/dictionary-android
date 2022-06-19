package com.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignIn extends AppCompatActivity {
    EditText editUsername = null;
    EditText editPassword = null;
    TextView labelError = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_linearlayout);
        editUsername = findViewById(R.id.editTextUsername);
        editPassword = findViewById(R.id.editTextPassword);
        labelError = findViewById(R.id.labelError);
        labelError.setText("");
        setEvent();
    }

    private void setEvent() {
//        TextView btnSignUp = (TextView) findViewById(R.id.textViewSignUp);
//        btnSignUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Homescreen.this, SignUp.class);
//            startActivity(intent);
//            }
//        });
//
//        Button btnSignIn = (Button) findViewById(R.id.btnSignIn);
//        btnSignIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Homescreen.this, SignUp.class);
//                startActivity(intent);
//            }
//        });
    }

    public boolean checkLogin() {
        labelError.setText("");
        if (editUsername.getText().toString().trim().equalsIgnoreCase("tuanbui")
                && editPassword.getText().toString().trim().equalsIgnoreCase("123456")) {
            return true;
        }
        labelError.setText("*Thông tin đăng nhập không đúng");
        return false;
    }

    public void handleSignInClick(View view) {
        labelError.setText("");
        this.postLoginRequest();

//        if (checkLogin()) {
////            GlobalVariables.username = editUsername.getText().toString().trim();
//            Intent mainIntent = new Intent(this, Main.class);
//            startActivity(mainIntent);
//        } else {
//
//        }
    }

    public void postLoginRequest() {
        String url = "http://10.0.2.2:7000/users/login";
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            getResponseData(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                            Toast.makeText(SignIn.this, error.toString(), Toast.LENGTH_LONG).show();
                             labelError.setText("*Thông tin đăng nhập không đúng");
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("username", editUsername.getText().toString().trim());
                    params.put("password", editPassword.getText().toString().trim());
                    return params;
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(SignIn.this);
            requestQueue.add(stringRequest);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    void getResponseData(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);

            String access_token = jsonObject.getString("access_token");
            String refresh_token = jsonObject.getString("refresh_token");

            GlobalVariables.access_token = "";
            GlobalVariables.refresh_token = "";

            GlobalVariables.access_token = access_token;
            GlobalVariables.refresh_token = refresh_token;

            String s = this.getDecodedJwt(access_token);

            String string[] = s.split("\\}\\{");

            JSONObject jsonObject2 = new JSONObject("{"+string[1]);
            GlobalVariables.userId = jsonObject2.getString("sub");
//            GlobalVariables.userId = substring.substring();

            if (GlobalVariables.access_token.isEmpty()) {
                labelError.setText("*Thông tin đăng nhập không đúng");
                return;
            }

            Intent mainIntent = new Intent(this, Main.class);
            startActivity(mainIntent);
            Toast.makeText(SignIn.this,"Đăng nhập thành công", Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static String getDecodedJwt(String jwt) {
        StringBuilder result = new StringBuilder();

        String[] parts = jwt.split("[.]");
        try {
            int index = 0;
            for (String part : parts) {
                if (index >= 2)
                    break;
                index++;
                byte[] decodedBytes = Base64.decode(part.getBytes("UTF-8"), Base64.URL_SAFE);
                result.append(new String(decodedBytes, "UTF-8"));
            }
        } catch (Exception e) {
            throw new RuntimeException("Couldnt decode jwt", e);
        }

        return result.toString();
    }

    public void goToSignUp(View view) {
        Intent signUpIntent = new Intent(this, SignUp.class);
        startActivity(signUpIntent);
    }
}