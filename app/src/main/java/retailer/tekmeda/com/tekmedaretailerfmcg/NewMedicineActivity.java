package retailer.tekmeda.com.tekmedaretailerfmcg;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import retailer.tekmeda.com.tekmedaretailerfmcg.bean.Medicine;
import retailer.tekmeda.com.tekmedaretailerfmcg.bean.Stockists;
import retailer.tekmeda.com.tekmedaretailerfmcg.util.NewMedicineAdapter;
import retailer.tekmeda.com.tekmedaretailerfmcg.util.StockistListAdapter;

public class NewMedicineActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private ListView listView;
    private ArrayList<Medicine> medList;
    private NewMedicineAdapter newMedicineAdapter;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_medicine);
        listView = (ListView) findViewById(R.id.lvNewMedicinesList);
        progressDialog=new ProgressDialog(NewMedicineActivity.this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Fetching Details");
        progressDialog.show();
        medList=new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("NewMeds");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    Medicine med = ds.getValue(Medicine.class);
                    if(med.getVerified().equalsIgnoreCase("1"))
                    medList.add(med);
                }

                newMedicineAdapter = new NewMedicineAdapter(getApplicationContext(),medList);
                listView.setAdapter(newMedicineAdapter);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
