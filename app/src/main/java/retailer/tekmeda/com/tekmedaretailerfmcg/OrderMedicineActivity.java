package retailer.tekmeda.com.tekmedaretailerfmcg;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retailer.tekmeda.com.tekmedaretailerfmcg.bean.Connections;
import retailer.tekmeda.com.tekmedaretailerfmcg.bean.Medicine;
import retailer.tekmeda.com.tekmedaretailerfmcg.bean.Orders;
import retailer.tekmeda.com.tekmedaretailerfmcg.bean.Stockists;
import retailer.tekmeda.com.tekmedaretailerfmcg.util.AcceptedStockistListAdapter;
import retailer.tekmeda.com.tekmedaretailerfmcg.util.OrdersAdapter;

public class OrderMedicineActivity extends AppCompatActivity {

    private Spinner spinnerUnit, spinnerStockist;
    private AutoCompleteTextView medicineName;
    private EditText quantity;
    private LinearLayout ll;

    private boolean isEdit = false;
    private boolean isReorder = false;
    private boolean isNew = false;
    private String stockistId = "";
    private String orderId = "";
    private Set<Medicine> MedicineSet;
    private ArrayList<String> medicineList;
    private boolean isOrderPresent;
    public String orderNumber;
    private List<String> stockistIdList;
    private List<Stockists> stockistsList;
    private TextView textView;
    private ProgressDialog progressDialog;
    private ArrayList<String> connectedStockistsIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_medicine);
        progressDialog = new ProgressDialog(OrderMedicineActivity.this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Fetching Details");
        connectedStockistsIds = new ArrayList<>();
        MedicineSet = new HashSet<>();
        medicineList = new ArrayList<>();
        stockistIdList = new ArrayList<>();
        stockistsList = new ArrayList<>();

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("medicineList.csv")));

            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                String s[] = mLine.split(",");
                Medicine med = new Medicine();
                med.setName(s[0]);
                //   med.setMedComp(s[1]);
                MedicineSet.add(med);
                medicineList.add(s[0]);
                //process line

            }
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
        stockistId = getIntent().getStringExtra("stockistId");
        isOrderPresent = getIntent().getBooleanExtra("orderPresent", false);


        isEdit = getIntent().getBooleanExtra("isEdit", false);
        isReorder = getIntent().getBooleanExtra("isreorder", false);
        isNew = getIntent().getBooleanExtra("isNew", false);
        orderNumber = getIntent().getStringExtra("orderNumber");

        spinnerUnit = (Spinner) findViewById(R.id.spUnit);
        spinnerStockist = (Spinner) findViewById(R.id.spStockist);
        quantity = (EditText) findViewById(R.id.tvQuanity);
        medicineName = (AutoCompleteTextView) findViewById(R.id.tvMedicineName);

        ArrayList<String> unitList = new ArrayList<String>();
        unitList.add("Bottle");
        unitList.add("Box");
        unitList.add("Carton");
        unitList.add("Cld");
        unitList.add("File");
        unitList.add("Strips");
        unitList.add("Tubes");
        unitList.add("Lot");
        unitList.add("Piece");


        ArrayAdapter<String> unitadapter = new ArrayAdapter<String>(
                this, R.layout.spinner_xml, unitList);

        ArrayAdapter<String> medadapter = new ArrayAdapter<String>(
                this, R.layout.item, R.id.autotext1, medicineList);


        spinnerUnit.setAdapter(unitadapter);

        medicineName.setAdapter(medadapter);

        if (isEdit) {


           // unitadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            Orders orders = (Orders) getIntent().getSerializableExtra("orders");
            orderId = orders.getOrderId();
            medicineName.setText(orders.getMedicineName());
            spinnerUnit.setSelection(unitList.indexOf(orders.getUnit()));
            quantity.setText(orders.getMedicineQty());
        }
        if (isReorder) {
            progressDialog = new ProgressDialog(OrderMedicineActivity.this);
            progressDialog.setTitle("Please Wait");
            progressDialog.setMessage("Fetching Details");
            progressDialog.show();
            loadStockists();
            Orders orders = (Orders) getIntent().getSerializableExtra("orders");
            medicineName.setText(orders.getMedicineName());
            spinnerUnit.setSelection(unitList.indexOf(orders.getUnit()));
            quantity.setText(orders.getMedicineQty());
            ll = (LinearLayout) findViewById(R.id.llStockistSelect);
            ll.setVisibility(View.VISIBLE);
            spinnerStockist.setSelection(0);

        }
        if (isNew) {
            medicineName.setText("Palcol 650 mg");
            ll = (LinearLayout) findViewById(R.id.llStockistSelect);
            ll.setVisibility(View.VISIBLE);

           /* stockistadapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item, stockistList);*/
            unitadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         /*   stockistadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerStockist.setAdapter(stockistadapter);*/
        }

    }

    public void placeOrder(View v) {

        progressDialog.show();
        if (!medicineList.contains(medicineName.getText().toString())) {
            {
                Toast.makeText(getApplicationContext(), "Medicine not in list", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        } else if(quantity.getText().toString().equalsIgnoreCase("")){
            {
                Toast.makeText(getApplicationContext(),"Please enter a quantity",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }

        else {
            final Orders orders = new Orders();
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            orders.setRetailerId(user.getUid());
            orders.setStockistId(stockistId);
            orders.setMedicineName(medicineName.getText().toString());
            orders.setMedicineQty(quantity.getText().toString());
            orders.setModified(false);
            orders.setUnit(spinnerUnit.getSelectedItem().toString());


            if (isEdit) {
                //Toast.makeText(getApplicationContext(),orderId,Toast.LENGTH_SHORT).show();
                orders.setOrderId(orderId);
                orders.setOrderNumber(orderNumber);


                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("DraftOrders").child("Retailers").child(user.getUid()).child(stockistId).child(orderNumber).child(orderId);
                databaseReference.setValue(orders).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("DraftOrders").child("Stockists").child(stockistId).child(user.getUid()).child(orderNumber).child(orderId);
                        databaseReference1.setValue(orders).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getApplicationContext(), "Order Saved as Draft", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                Intent intent=new Intent();
                                intent.putExtra("orders",orders);
                                intent.putExtra("position",getIntent().getIntExtra("position",0));
                                setResult(1003,intent);
                                progressDialog.dismiss();
                                finish();

                            }
                        });
                    }
                });
            } else {


                if(isReorder)
                {
                    stockistId=stockistsList.get(stockistIdList.indexOf(spinnerStockist.getSelectedItem().toString())).getId();
                    final List<Orders> ordersList = new ArrayList<>();
                    Orders ord = (Orders) getIntent().getSerializableExtra("orders");
                    ord.setStockistId(stockistId);
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("RejectedOrders").child("Retailers").child(user.getUid()).child(ord.getOrderId());
                    databaseReference.removeValue();
                    DatabaseReference  databaseReference1 = FirebaseDatabase.getInstance().getReference().child("DraftOrders").child("Retailers").child(user.getUid()).child(stockistId);
                    databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                orderNumber = ds.getKey();
                               // textView.setText("Order # : " + orderNumber);
                                for (DataSnapshot dss : ds.getChildren()) {
                                    Orders orders = dss.getValue(Orders.class);
                                    ordersList.add(orders);
                                    orderNumber=orders.getOrderNumber();
                                    break;
                                }
                            }
                            if (ordersList.size() > 0)
                            {
                                isOrderPresent = true;
                                placeOrder2(orders,user);
                            }
                            else{
                                placeOrder2(orders,user);
                            }

                            progressDialog.dismiss();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else{
                    placeOrder2(orders,user);


            }


        }
    }
        }

        private void placeOrder2(Orders ord,FirebaseUser user1){
        final Orders orders=ord;
        final FirebaseUser user=user1;
            if (isOrderPresent) {
                orders.setOrderNumber(orderNumber);
            }

            else {

                String orderIdString = user.getUid().substring(0, 2) + stockistId.substring(0, 2);
                final long rand = (int) (Math.random() * 9999);
                orders.setOrderNumber(orderIdString + rand);
            }
            orders.setOrderId(System.currentTimeMillis() + "");


            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("DraftOrders").child("Retailers").child(user.getUid()).child(stockistId).child(orders.getOrderNumber()).child(orders.getOrderId());
            databaseReference.setValue(orders).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("DraftOrders").child("Stockists").child(stockistId).child(user.getUid()).child(orders.getOrderNumber()).child(orders.getOrderId());
                    databaseReference1.setValue(orders).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(), "Order Saved as Draft", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent();
                            intent.putExtra("orders",orders);
                            intent.putExtra("position",getIntent().getIntExtra("position",0));
                            setResult(1002,intent);
                            progressDialog.dismiss();
                            finish();
                        }
                    });
                }
            });
        }

    private void loadStockists() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Connections").child("Retailers").child(user.getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Connections connections = ds.getValue(Connections.class);
                    if (connections.getConnectionStatus().equalsIgnoreCase("2")) {
                        connectedStockistsIds.add(connections.getStockistId());
                    }
                }
                Log.d("connected Stockist ids", connectedStockistsIds.toString());

                fetchStockistDetails();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void fetchStockistDetails() {
        Log.d(" fetch Stockist ids", connectedStockistsIds.toString());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Stockists");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Stockists stockists = ds.getValue(Stockists.class);
                    if (connectedStockistsIds.contains(stockists.getId())) {
                        stockistsList.add(stockists);
                        stockistIdList.add(stockists.getEnterpriseName());
                    }
                }
                // Log.d(" fetch Stockist detsils",stockistLists.toString());

                ArrayAdapter<String> stockistadapter = new ArrayAdapter<String>(
                        OrderMedicineActivity.this, R.layout.spinner_xml, stockistIdList);
                spinnerStockist.setAdapter(stockistadapter);
                //stockistadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}