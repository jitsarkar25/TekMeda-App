package retailer.tekmeda.com.tekmedaretailerfmcg;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import retailer.tekmeda.com.tekmedaretailerfmcg.util.OrdersAdapter;

public class AlreadyOrderedMedicineActivity extends AppCompatActivity {

    private String stockistId = "";
    private ListView listView;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    public static List<Orders> ordersList;
    private boolean isOrderPresent;
    private OrdersAdapter ordersAdapter;
    private String orderNumber="";
    private TextView textView;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_already_ordered_medicine);
        isOrderPresent=true;
        textView = (TextView)findViewById(R.id.tvOrderNumber);
        listView = (ListView) findViewById(R.id.lvAlreadyOrderedMeds);
        stockistId=getIntent().getStringExtra("stockistId");
        user = FirebaseAuth.getInstance().getCurrentUser();
        ordersList =new ArrayList<>();
        progressDialog=new ProgressDialog(AlreadyOrderedMedicineActivity.this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Fetching Details");
        progressDialog.show();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("DraftOrders").child("Retailers").child(user.getUid()).child(stockistId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    orderNumber=ds.getKey();
                    textView.setText("Order # : "+orderNumber);
                    for(DataSnapshot dss:ds.getChildren()) {
                        Orders orders = dss.getValue(Orders.class);
                        ordersList.add(orders);
                    }
                }
                if(ordersList.size() == 0)
                    isOrderPresent=false;
                ordersAdapter= new OrdersAdapter(AlreadyOrderedMedicineActivity.this,ordersList);
                listView.setAdapter(ordersAdapter);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Orders orders = ordersList.get(i);
                Intent intent=new Intent(getApplicationContext(),OrderMedicineActivity.class);
                intent.putExtra("isEdit",true);
                intent.putExtra("orders",orders);
                intent.putExtra("orderNumber",orders.getOrderNumber());
                intent.putExtra("stockistId",stockistId);
                intent.putExtra("position",i);
                startActivityForResult(intent,1003);
               // finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.neworder, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_neworder) {
            Intent intent = new Intent(getApplicationContext(),OrderMedicineActivity.class);
            intent.putExtra("stockistId",stockistId);
            intent.putExtra("orderPresent",isOrderPresent);
            intent.putExtra("orderNumber",orderNumber);
            startActivityForResult(intent,1002);
           // finish();
        }
        if(id == R.id.nav_placeorder)
        {
            final ProgressDialog progressDialog=new ProgressDialog(AlreadyOrderedMedicineActivity.this);
            progressDialog.setTitle("Please Wait");
            progressDialog.setMessage("Fetching Details");
            progressDialog.show();
            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("DraftOrders").child("Retailers").child(user.getUid()).child(stockistId).child(orderNumber);
            for(Orders orders:ordersList)
            {
                databaseReference1.child(orders.getOrderId()).removeValue();
            }
            DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child("DraftOrders").child("Stockists").child(stockistId).child(user.getUid()).child(orderNumber);
            for(Orders orders:ordersList)
            {
                databaseReference2.child(orders.getOrderId()).removeValue();
            }

            for( int i = 0; i < ordersList.size();i++)
            {
                final int j=i;
               final Orders orders = ordersList.get(i);
               orders.setTime(System.currentTimeMillis()+"");
                DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference().child("PlacedOrders").child("Retailers").child(user.getUid()).child(stockistId).child(orderNumber).child(orders.getOrderId());
                databaseReference3.setValue(orders).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        DatabaseReference databaseReference4= FirebaseDatabase.getInstance().getReference().child("PlacedOrders").child("Stockists").child(stockistId).child(user.getUid()).child(orderNumber).child(orders.getOrderId());
                        databaseReference4.setValue(orders).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(j == ordersList.size()-1)
                                {
                                    Toast.makeText(getApplicationContext(),"Order Placed",Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    finish();
                                }

                            }
                        });
                    }
                });
            }

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1002 && data!=null)
        {
            Orders orders =(Orders)data.getSerializableExtra("orders");
            if(orders!=null)
            {
                ordersList.add(orders);
                isOrderPresent=true;
                orderNumber=orders.getOrderNumber();
                textView.setText("Order # : "+orders.getOrderNumber());
                ordersAdapter.notifyDataSetChanged();
            }


        }
        else if(requestCode==1003 && data!=null)
        {
            Orders orders =(Orders)data.getSerializableExtra("orders");
            int position =data.getIntExtra("position",0);
            if(orders!=null)
            {


                ordersList.remove(position);
                ordersList.add(position,orders);

              //  textView.setText("Order # : "+orders.getOrderNumber());
                ordersAdapter.notifyDataSetChanged();
            }


        }
    }

    public void editMed(View v)
    {

    }
}
