package retailer.tekmeda.com.tekmedaretailerfmcg;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retailer.tekmeda.com.tekmedaretailerfmcg.bean.Retailers;

public class RegisterActivity extends AppCompatActivity {



    private EditText etEmailReg,etPassReg,etConfPassReg,etFirstName,etLastName,etSecretCode;
    private TextInputLayout tilPass,tilPassConf;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference databaseReference;
    private final String TAG="tekmeda";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etEmailReg=(EditText) findViewById(R.id.etEmailRegister);

        etPassReg =(EditText) findViewById(R.id.etPasswordRegister);
        etConfPassReg=(EditText) findViewById(R.id.etConfPasswordRegister);
        tilPass=(TextInputLayout) findViewById(R.id.tilPassReg);
        tilPassConf=(TextInputLayout) findViewById(R.id.tilPassConfReg);
        progressDialog=new ProgressDialog(RegisterActivity.this);
        //Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }


    public void registeruser(View v){
        final String email=etEmailReg.getText().toString();
        String pass=etPassReg.getText().toString();
        String passconf=etConfPassReg.getText().toString();
        boolean error=false;
        if(email.equals(""))
        {
            error=true;
            etEmailReg.setError("Please Enter Email");
        }
        else if(!validate(email))
        {
            error=true;
            etEmailReg.setError("Enter a valid Email id");
        }
        if(pass.equals(""))
        {
            error=true;
            tilPass.setError("Please Enter Password");
        }
        if(passconf.equals(""))
        {
            error=true;
            tilPassConf.setError("Please Re-Enter Password");
        }
        if(passconf.length()<6)
        {
            error=true;
            tilPass.setError("Password must be atleast 6 characters long");
        }
        if(!error && !pass.equals(passconf))
        {
            error=true;
            tilPassConf.setError("Passwords doesnt match");
        }
        if(!error) //Firebase Register User

        {
            progressDialog.setMessage("Registering User");
            progressDialog.setTitle("Please Wait");
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());


                            if (task.isSuccessful()) {

                                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                user.sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {

                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(getApplicationContext(), "Registration Successfull..Please Verify Your Email", Toast.LENGTH_LONG).show();
                                                    // progressDialog.dismiss();
                                                    //startActivity(new Intent(getApplicationContext(),UserDetailsActivity.class));
                                                    finish();
                                                    /*
                                                    Retailers retailers = new Retailers();
                                                    retailers.setFirstName(etFirstName.getText().toString());
                                                    retailers.setLastName(etLastName.getText().toString());
                                                    retailers.setEmailid(email);
                                                    retailers.setId(user.getUid());
                                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Retailer").child(user.getUid());
                                                    databaseReference.setValue(retailers).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                        }
                                                    });*/

                                                }
                                            }
                                        });


                            }
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                progressDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });//Toast.makeText(getApplicationContext(),"Regisration successfull",Toast.LENGTH_SHORT).show();
        }

        // startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
    }
    public static boolean validate(String emailStr) {
        Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }


    public void loginuser(View v)
    {
        finish();
    }
}
