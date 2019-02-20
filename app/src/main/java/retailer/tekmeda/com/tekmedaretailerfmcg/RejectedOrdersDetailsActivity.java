package retailer.tekmeda.com.tekmedaretailerfmcg;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import retailer.tekmeda.com.tekmedaretailerfmcg.bean.Orders;
import retailer.tekmeda.com.tekmedaretailerfmcg.util.RejectedOrderAdapter;

public class RejectedOrdersDetailsActivity extends AppCompatActivity {

    private FirebaseUser user;
    private ListView lvReject;
    private List<Orders> orderlist;
    private RejectedOrderAdapter rejectedOrderAdapter;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejected_orders_details);
        lvReject = (ListView)findViewById(R.id.lvRejectedMeds);
        orderlist=new ArrayList<>();
        progressDialog = new ProgressDialog(RejectedOrdersDetailsActivity.this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Fetching details");
        progressDialog.show();
        user= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("RejectedOrders").child("Retailers").child(user.getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    Orders orders=ds.getValue(Orders.class);
                    orderlist.add(orders);
                }
                rejectedOrderAdapter=new RejectedOrderAdapter(RejectedOrdersDetailsActivity.this,orderlist);
                lvReject.setAdapter(rejectedOrderAdapter);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void reorderMedicine(View v)
    {
        Intent intent = new Intent(getApplicationContext(),OrderMedicineActivity.class);
        intent.putExtra("isreorder",true);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1002 & data!=null)
        {
            Orders orders=(Orders)data.getSerializableExtra("orders");
            int pos=data.getIntExtra("position",0);
            orderlist.remove(pos);
            rejectedOrderAdapter.notifyDataSetChanged();
        }
    }
}
