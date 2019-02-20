package retailer.tekmeda.com.tekmedaretailerfmcg;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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
import retailer.tekmeda.com.tekmedaretailerfmcg.util.PlacedOrderAdapter;

public class PlacedOrderActivity extends AppCompatActivity {
    private String stockistId = "";
    private ListView listView;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    public static List<Orders> ordersList;
    private boolean isOrderPresent;
    private PlacedOrderAdapter ordersAdapter;
    private String orderNumber="";
    private TextView textView;
    private boolean showOrderNumber=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placed_order);
        listView = (ListView) findViewById(R.id.lvPlacedMedicines);
        stockistId=getIntent().getStringExtra("stockistId");
        user = FirebaseAuth.getInstance().getCurrentUser();
        ordersList =new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("PlacedOrders").child("Retailers").child(user.getUid()).child(stockistId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    showOrderNumber=true;
                    orderNumber=ds.getKey();

                    for(DataSnapshot dss:ds.getChildren()) {

                        Orders orders = dss.getValue(Orders.class);
                        if(showOrderNumber==true)
                        {
                            orders.setShowOrderNumber(true);
                            showOrderNumber=false;
                        }
                        else
                            orders.setShowOrderNumber(false);
                            ordersList.add(orders);
                    }
                }
                if(ordersList.size() == 0)
                    isOrderPresent=false;
                ordersAdapter= new PlacedOrderAdapter(PlacedOrderActivity.this,ordersList);
                listView.setAdapter(ordersAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
