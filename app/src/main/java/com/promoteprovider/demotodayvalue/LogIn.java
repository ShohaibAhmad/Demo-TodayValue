package com.promoteprovider.demotodayvalue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.promoteprovider.demotodayvalue.Storages.MySharedPreferences;
import com.promoteprovider.demotodayvalue.utils.Util;

public class LogIn extends AppCompatActivity {
    FirebaseAuth auth;
    EditText email,pass;
    TextView FP;
    //alert
    private AlertDialog dialog;
    private MySharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        //alert
        dialog = Util.getAlertDialog(this,"LogIn Loading...");
        sp = MySharedPreferences.getInstance(this);
        //Auth
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null){
            Intent intent = new Intent(LogIn.this,MainActivity.class);
            startActivity(intent);
            finish();
        }

        TextView login=findViewById(R.id.login);
        email=findViewById(R.id.email);
        pass=findViewById(R.id.pass);
        TextView NewAccount=findViewById(R.id.NewAccount);
        FP = findViewById(R.id.resetBtn);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //int
                String emailF = email.getText().toString();
                String passF = pass.getText().toString();

                //Auth
                if (TextUtils.isEmpty(emailF)){
                    email.setError("Email is required!");
                    return;
                }

                if (TextUtils.isEmpty(passF)){
                    pass.setError("Password is required!");
                    return;
                }

                if (passF.length() < 6){
                    pass.setError("Password must be >= 6 Character!");
                    return;
                }
                dialog.show();
                    //auth the user

                auth.signInWithEmailAndPassword(emailF,passF).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        dialog.dismiss();
                        if (task.isSuccessful()){
                            sp.setLogin("2");
                            sp.setUserId(auth.getUid());
                            Toast.makeText(LogIn.this, "Login Successfully!", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(LogIn.this,MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(LogIn.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        
                    }
                });
            }
        });

        FP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText resetPass = new EditText(view.getContext());
                AlertDialog.Builder passReset = new AlertDialog.Builder(view.getContext());
                passReset.setTitle("Reset Password ?");
                passReset.setMessage("Enter Your Email to Reset the Password Link.");
                passReset.setView(resetPass);
                passReset.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String mail = resetPass.getText().toString();
                        auth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(LogIn.this, "Reset Link Sent to Your Email.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LogIn.this, "Error ! Reset Link is Not Sent"+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                passReset.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                passReset.create().show();
            }
        });
//     Password Hide Show
//        button.setOnTouchListener(new View.OnTouchListener() {
//            public boolean onTouch(View v, MotionEvent event) {
//
//                switch ( event.getAction() ) {
//
//                    case MotionEvent.ACTION_UP:
//                        pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//                        break;
//
//                    case MotionEvent.ACTION_DOWN:
//                        pass.setInputType(InputType.TYPE_CLASS_TEXT);
//                        break;
//
//                }
//                return true;
//            }
//        });



        NewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LogIn.this,Welcome.class);
                startActivity(intent);
            }
        });




    }
}