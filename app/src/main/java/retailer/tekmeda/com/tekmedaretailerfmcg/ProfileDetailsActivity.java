package retailer.tekmeda.com.tekmedaretailerfmcg;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import retailer.tekmeda.com.tekmedaretailerfmcg.bean.Retailers;

public class ProfileDetailsActivity extends AppCompatActivity {

    EditText etFirstName,etLastName,etEmail,etPhone,etState,etPin,etAddress,etCity,etStoreName,etStoreNo,etStreet;
    TextView tvid;
    private boolean isEdit=false;
    private ProgressDialog progressDialog;
    private Retailers retailers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);
        etFirstName = (EditText)findViewById(R.id.etFirstName);
        etLastName = (EditText)findViewById(R.id.etLastName);
        etEmail = (EditText)findViewById(R.id.etEmail);
        etPhone = (EditText)findViewById(R.id.etphone);
        etState = (EditText)findViewById(R.id.etState);
        etPin = (EditText)findViewById(R.id.etPIN);
        etAddress = (EditText)findViewById(R.id.etStoreAddress);
        etCity = (EditText)findViewById(R.id.etCity);
        etStoreName = (EditText)findViewById(R.id.etStoreName);
        tvid=(TextView)findViewById(R.id.tvId);
        etStoreNo=(EditText) findViewById(R.id.etShopNo);
        etStreet=(EditText) findViewById(R.id.etStreet);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        etEmail.setText(user.getEmail());

        isEdit = getIntent().getBooleanExtra("edit",false);
        if(isEdit)
        {
            progressDialog = new ProgressDialog(ProfileDetailsActivity.this);
            progressDialog.setMessage("Fetching Details");
            progressDialog.setTitle("Please Wait");
            progressDialog.show();

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Retailers");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        if (child.getKey().equals(user.getUid())) {
                            progressDialog.dismiss();
                            retailers = child.getValue(Retailers.class);
                            etFirstName.setText(retailers.getFirstName());
                            etLastName.setText(retailers.getLastName());
                            etEmail.setText(retailers.getEmailid());
                            etStoreName.setText(retailers.getRetailName());
                            etPhone.setText(retailers.getPhonenumber());
                            etState.setText(retailers.getState());
                            etCity.setText(retailers.getCity());
                            etPin.setText(retailers.getPin());
                            etAddress.setText(retailers.getAddress());
                            etStreet.setText(retailers.getStreet());
                            etStoreNo.setText(retailers.getShopNo());
                            tvid.setText("ID : "+retailers.getId());
                        }
                    }
        }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }


            });
        }
    }
    public void saveuserdetails(View v)
    {
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

        Retailers retailers = new Retailers();
        retailers.setFirstName(etFirstName.getText().toString());
        retailers.setLastName(etLastName.getText().toString());
        retailers.setEmailid(etEmail.getText().toString());
        retailers.setPhonenumber(etPhone.getText().toString());
        retailers.setState(etState.getText().toString());
        retailers.setPin(etPin.getText().toString());
        retailers.setAddress(etAddress.getText().toString());
        retailers.setCity(etCity.getText().toString());
        retailers.setRetailName(etStoreName.getText().toString());
        retailers.setStreet(etStreet.getText().toString());
        retailers.setShopNo(etStoreNo.getText().toString());
        retailers.setId(user.getUid());

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Retailers").child(user.getUid());
        databaseReference.setValue(retailers);

        Toast.makeText(getApplicationContext(),"Profile updated",Toast.LENGTH_SHORT).show();
        if(!isEdit)
        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
        finish();

    }
}
