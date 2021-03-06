package com.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
public class SignUp extends AppCompatActivity {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    TextView labelErrorUsername = null;
    TextView labelErrorPassword = null;
    TextView labelErrorConfirmPassword = null;
    TextView labelErrorFullname = null;
    TextView labelErrorEmail = null;

    TextView editTextUsername = null;
    TextView editTextPassword = null;
    TextView editTextConfirmPassword = null;
    TextView editTextFullname = null;
    TextView editTextEmail = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_linearlayout);
        System.out.println("SignUp created");

        labelErrorUsername = findViewById(R.id.labelErrorUsername);
        labelErrorPassword = findViewById(R.id.labelErrorPassword);
        labelErrorConfirmPassword = findViewById(R.id.labelErrorConfirmPassword);
        labelErrorFullname = findViewById(R.id.labelErrorFullname);
        labelErrorEmail = findViewById(R.id.labelErrorEmail);

        labelErrorUsername.setText("");
        labelErrorPassword.setText("");
        labelErrorConfirmPassword.setText("");
        labelErrorFullname.setText("");
        labelErrorEmail.setText("");

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        editTextFullname = findViewById(R.id.editTextFullname);
        editTextEmail = findViewById(R.id.editTextEmail);
    }

    public void goToSignIn(View view) {
        Intent signInIntent = new Intent(this, SignIn.class);
        startActivity(signInIntent);
    }

    public boolean validateSignUp() {
        boolean check = true;
        if(editTextUsername.getText().toString().trim().isEmpty()){
            labelErrorUsername.setText("*H??y nh???p t??n ????ng nh???p");
            check = false;
        }else if(editTextUsername.getText().toString().trim().length()<6){
            labelErrorUsername.setText("*T??n ????ng nh???p t???i thi???u 6 k?? t???");
            check = false;
        }

        if(editTextPassword.getText().toString().trim().isEmpty()){
            labelErrorPassword.setText("*H??y nh???p m???t kh???u");
            check = false;
        }else if(editTextPassword.getText().toString().trim().length()<6){
            labelErrorPassword.setText("*M???t kh???u t???i thi???u 6 k?? t???");
            check = false;
        }

        if(!editTextConfirmPassword.getText().toString().trim().equals(editTextPassword.getText().toString().trim())){
            labelErrorConfirmPassword.setText("*X??c nh???n m???t kh???u kh??ng kh???p");
            check = false;
        }

        if(editTextFullname.getText().toString().trim().isEmpty()){
            labelErrorFullname.setText("*H??y nh???p h??? t??n");
            check = false;
        }

        if(editTextEmail.getText().toString().trim().isEmpty()){
            labelErrorEmail.setText("*H??y nh???p email");
            check = false;
        }else if(!validateEmail(editTextEmail.getText().toString().trim())){
            labelErrorEmail.setText("*Email kh??ng h???p l???");
            check = false;
        }

        return check;
    }

    public void handleSignUpClick(View view) {
        labelErrorUsername.setText("");
        labelErrorPassword.setText("");
        labelErrorConfirmPassword.setText("");
        labelErrorFullname.setText("");
        labelErrorEmail.setText("");

        if (validateSignUp()) {

//            GlobalVariables.username = editTextUsername.getText().toString().trim();

            Intent mainIntent = new Intent(this, Main.class);
            startActivity(mainIntent);
        }
    }
}