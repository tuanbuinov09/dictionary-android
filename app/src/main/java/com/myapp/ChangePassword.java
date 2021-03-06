package com.myapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity {
    private static final int REQUEST_CODE_EXAMPLE = 2;
    //    private View mView;
    private EditText edtNewPassword;
    private EditText edtOldPassword;
    private Button btnChangePassword;
    private ProgressDialog progressDialog;
    private String m_Text = "";
    private String m_Text1 = "";
    private String strEmail="";
    private String strNewPassword="";
    private String strOldPassword="";
    FirebaseUser user=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initUi();
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strOldPassword  = edtOldPassword.getText().toString().trim();
                strNewPassword  = edtNewPassword.getText().toString().trim();
//                String strEmail="";FirebaseUser
                user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    // Name, email address, and profile photo Url
                    // String name = user.getDisplayName();
                    strEmail = user.getEmail();
                }
                reAuthentiate(strEmail,strOldPassword);
            }
        });
    }
    public void onClickChangePassword() {
 //       String strOldPassword  = edtOldPassword.getText().toString().trim();
 //       strNewPassword  = edtNewPassword.getText().toString().trim();

//        progressDialog.show();
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null) {
//            // Name, email address, and profile photo Url
//            // String name = user.getDisplayName();
//            strEmail = user.getEmail();
//        }

        String finalStrEmail = strEmail;
        //reAuthentiate(finalStrEmail,strOldPassword);+strEmail+"  "+strOldPassword+"  "+strNewPassword
        user.updatePassword(strNewPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ChangePassword.this,"Change password successfully",Toast.LENGTH_LONG).show();
//                            progressDialog.dismiss();

                        }else{
                            //reAuthentiate(finalStrEmail,strOldPassword);
                            //reAuthentiate1(finalStrEmail,strOldPassword);
                            Toast.makeText(ChangePassword.this,"Password change failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void reAuthentiate(String email,String password){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        AuthCredential credential = EmailAuthProvider
                .getCredential(email, password);
//                .getCredential("user@example.com", "password1234");

        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
//                            String text = "121212";
                            Intent intent = new Intent(ChangePassword.this,OtpSendActivity.class);
//                            intent.putExtra("strEmail",strEmail);
//                            intent.putExtra("strPassword",text);
                            startActivityForResult(intent, REQUEST_CODE_EXAMPLE);
//                            startActivityForResult(intent, 2);
                            //onClickChangePassword();
                        }else{
                            Toast.makeText(ChangePassword.this,"M???t kh???u kh??ng ch??nh x??c",Toast.LENGTH_SHORT).show();
                            return;
                        }

                    }
                });
    }
    private void reAuthentiate1(String email,String password){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        AuthCredential credential = EmailAuthProvider
                .getCredential(email, password);
//                .getCredential("user@example.com", "password1234");

        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
//                            String text = "121212";
                            Toast.makeText(ChangePassword.this,"Re auChange password successfully: "+strEmail+"  "+strOldPassword+"  "+strNewPassword,Toast.LENGTH_LONG).show();
                            onClickChangePassword();
                        }else{
                            Toast.makeText(ChangePassword.this,"C???n x??c th???c l???i:" +email +  "  "+password,Toast.LENGTH_SHORT).show();
                            return;
                        }

                    }
                });
    }
    private void signin(String email,String password){
        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(
                                    @NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(),
                                            "????ng nh???p th??nh c??ng!!",
                                            Toast.LENGTH_LONG)
                                            .show();
                                    onClickChangePassword();

                                    // hide the progress bar
                                    // if sign-in is successful
                                    // intent to home activity
//                                    Intent intent
//                                            = new Intent(SignInActivity.this,
//                                            MainActivity.class);
//                                    startActivity(intent);
                                } else {
                                    // sign-in failed
                                    Toast.makeText(getApplicationContext(),
                                            "Sai Email ho???c m???t kh???u!!",
                                            Toast.LENGTH_LONG)
                                            .show();
                                }
                            }
                        });
    }

    private void initUi(){
        progressDialog = new ProgressDialog(ChangePassword.this);
        edtNewPassword = findViewById(R.id.edt_new_pasword);
        edtOldPassword = findViewById(R.id.edt_old_pasword);
        btnChangePassword =findViewById(R.id.btn_change_password);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Ki???m tra requestCode c?? tr??ng v???i REQUEST_CODE v???a d??ng
        if(requestCode == REQUEST_CODE_EXAMPLE) {

            // resultCode ???????c set b???i DetailActivity
            // RESULT_OK ch??? ra r???ng k???t qu??? n??y ???? th??nh c??ng
            if(resultCode == ChangePassword.RESULT_OK) {
                // Nh???n d??? li???u t??? Intent tr??? v???
                final String result = data.getStringExtra(OtpSendActivity.EXTRA_DATA);
                signin(strEmail,strOldPassword);
                //reAuthentiate1(strEmail,strOldPassword);
                //onClickChangePassword();
                // S??? d???ng k???t qu??? result b???ng c??ch hi???n Toast
                //Toast.makeText(this, "Result:111111111 " + result, Toast.LENGTH_LONG).show();
                Toast.makeText(this, "?????i m???t kh???u th??nh c??ng " + result, Toast.LENGTH_LONG).show();
                finish();
            } else {

                Toast.makeText(this, "?????i m???t kh???u th???t b???i", Toast.LENGTH_LONG).show();
            }
        }
    }
}