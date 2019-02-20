package retailer.tekmeda.com.tekmedaretailerfmcg;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {



    private EditText etEmail, etPass;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextInputLayout tilPass;
    private final String TAG = "tekmeda";
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;
    private FirebaseUser user;

    private boolean isDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // tvNumberOfOrdersAccepted = (TextView)findViewById(R.id.tvNumberOfOrdersAccepted);
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null)
        {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Please Wait");
            progressDialog.setMessage("Signing In..");
            progressDialog.show();
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Retailers");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        if (child.getKey().equals(user.getUid())) {
                            progressDialog.dismiss();
//                                                SharedPreferences sharedPreferences = getSharedPreferences("logininfo", Context.MODE_PRIVATE);
//                                                SharedPreferences.Editor editor = sharedPreferences.edit();
//                                                editor.putBoolean("islogin", true);
//                                                editor.commit();
                            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
//                                                intent.putExtra("launchActivityC", true);
//                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //this will clear the entire task stack and create a new instance of A
                            startActivity(intent);
                            finish();
                            isDetails = true;
                        }
                    }
                    if (!isDetails) {

                        //Toast.makeText(getApplicationContext(), "New User", Toast.LENGTH_SHORT).show();
                        if (user.isEmailVerified()) {
                            progressDialog.dismiss();
                            startActivity(new Intent(getApplicationContext(), ProfileDetailsActivity.class));
                            finish();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "You need to verify your email to continue", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }

        etEmail = (EditText) findViewById(R.id.etEmailLogin);
        etPass = (EditText) findViewById(R.id.etPasswordLogin);
        tilPass = (TextInputLayout) findViewById(R.id.tilPassLog);
        progressDialog = new ProgressDialog(MainActivity.this);
        //Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d("ffgghhjj", "onAuthStateChanged:signed_in:" + user.getUid());
                    //saveToken(user);

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    public void loginuser(View v) {


        boolean error = false;
        String email = etEmail.getText().toString();
        String password = etPass.getText().toString();
        if (email.equals("")) {
            error = true;
            etEmail.setError("Enter an Email Id");
        }
        if (password.equals("")) {
            error = true;
            tilPass.setError("Enter a PassWord");
        }
        if (!error) {
            //Toast.makeText(getApplicationContext(),"Login successful",Toast.LENGTH_SHORT).show();
            tilPass.setError(null);
            etEmail.setError(null);
            progressDialog.setMessage("Logging In");
            progressDialog.setTitle("Please Wait");
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // progressDialog.dismiss();
                            Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                            // progressDialog.dismiss();
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                progressDialog.dismiss();
                                Log.w(TAG, "signInWithEmail:failed", task.getException());
                                Toast.makeText(MainActivity.this, "Invalid Credentials",
                                        Toast.LENGTH_SHORT).show();
                            } else {

                                user = mAuth.getCurrentUser();
                                isDetails = false;
                                databaseReference = FirebaseDatabase.getInstance().getReference().child("Retailers");
                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                                            if (child.getKey().equals(user.getUid())) {
                                                progressDialog.dismiss();
//                                                SharedPreferences sharedPreferences = getSharedPreferences("logininfo", Context.MODE_PRIVATE);
//                                                SharedPreferences.Editor editor = sharedPreferences.edit();
//                                                editor.putBoolean("islogin", true);
//                                                editor.commit();
                                                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
//                                                intent.putExtra("launchActivityC", true);
//                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //this will clear the entire task stack and create a new instance of A
                                                startActivity(intent);
                                                finish();
                                                isDetails = true;
                                            }
                                        }
                                        if (!isDetails) {

                                            //Toast.makeText(getApplicationContext(), "New User", Toast.LENGTH_SHORT).show();
                                            if (user.isEmailVerified()) {
                                                progressDialog.dismiss();
                                                startActivity(new Intent(getApplicationContext(), ProfileDetailsActivity.class));
                                                finish();
                                            } else {
                                                progressDialog.dismiss();
                                                Toast.makeText(getApplicationContext(), "You need to verify your email to continue", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                // Toast.makeText(getApplicationContext(),"Login Successful "+user.getUid(),Toast.LENGTH_SHORT).show();
                            }
                            // else {
                               /* SharedPreferences sharedPreferences = getSharedPreferences("logininfo", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("islogin", true);
                                editor.commit();
                              //  startActivity(new Intent(getApplicationContext(), UserActivity.class));
                              //  finish();
                            }*/

                            // ...
                        }
                    });
        }

    }


    public void reguser(View v)
    {
        startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
    }
}
