package retailer.tekmeda.com.tekmedaretailerfmcg;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import org.w3c.dom.Text;

import java.util.ArrayList;

import retailer.tekmeda.com.tekmedaretailerfmcg.bean.Connections;
import retailer.tekmeda.com.tekmedaretailerfmcg.bean.Stockists;
import retailer.tekmeda.com.tekmedaretailerfmcg.util.NewRequestAdapter;
import retailer.tekmeda.com.tekmedaretailerfmcg.util.StockistListAdapter;

public class StockistConnectionActivity extends AppCompatActivity {

    private TextView tvSearchedStockist;
    private Boolean stockistExists = false;
    private Stockists searchedStockists;
    private ListView lvConnectedStockists,newReq;
    private ArrayList<String> connectedStockistsIds;
    public static ArrayList<Stockists> stockistLists;
    public static StockistListAdapter stockistListAdapter;
    private ProgressDialog progressDialog;
    private boolean isPresent,req=false;
    public static ArrayList<Connections> connectionsArrayList,connectedArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stockist_connection);
        tvSearchedStockist = (TextView) findViewById(R.id.tvSearchedStockistName);
        lvConnectedStockists = (ListView) findViewById(R.id.lvConnectedStockists);
        newReq = (ListView) findViewById(R.id.lvNewRequest);
        connectedStockistsIds = new ArrayList<>();
        stockistLists = new ArrayList<>();
        progressDialog = new ProgressDialog(StockistConnectionActivity.this);
        progressDialog.setMessage("Fetching Connections");
        progressDialog.setTitle("Please Wait");
        progressDialog.show();
        connectionsArrayList = new ArrayList<>();
        connectedArrayList = new ArrayList<>();
    FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Connections").child("Retailers").child(user.getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    Connections connections = ds.getValue(Connections.class);
                    if(connections.getConnectionStatus().equalsIgnoreCase("1") && !connections.getSender().equalsIgnoreCase("Retailer"))
                    {
                        connectionsArrayList.add(connections);
                        req=true;
                    }
                    else if(connections.getConnectionStatus().equalsIgnoreCase("2"))
                        connectedArrayList.add(connections);
                }
                NewRequestAdapter newRequestAdapter = new NewRequestAdapter(StockistConnectionActivity.this,connectionsArrayList);
                newReq.setAdapter(newRequestAdapter);

                if(req!=true)
                    ((TextView)findViewById(R.id.tvNewReq)).setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        loadStockists();

        lvConnectedStockists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),StockistDetailActivity.class);
                intent.putExtra("stockistDetails",stockistLists.get(i));
                startActivity(intent);
            }
        });
    }

    private void loadStockists(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Connections").child("Retailers").child(user.getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    Connections connections = ds.getValue(Connections.class);
                    if(connections.getConnectionStatus().equalsIgnoreCase("2"))
                    {
                        connectedStockistsIds.add(connections.getStockistId());
                    }
                }
                Log.d("connected Stockist ids",connectedStockistsIds.toString());

                fetchStockistDetails();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void fetchStockistDetails(){
        Log.d(" fetch Stockist ids",connectedStockistsIds.toString());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Stockists");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    Stockists stockists =  ds.getValue(Stockists.class);
                    if(connectedStockistsIds.contains(stockists.getId()))
                    {
                        stockistLists.add(stockists);
                    }
                }
                Log.d(" fetch Stockist detsils",stockistLists.toString());
                stockistListAdapter = new StockistListAdapter(getApplicationContext(),stockistLists);
                lvConnectedStockists.setAdapter(stockistListAdapter);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void searchedStockist(View v)
    {
     EditText et = (EditText) findViewById(R.id.etStockistId);
        final String stId = et.getText().toString();
        stockistExists=false;
       /* if(stId.equals("123456"))
        {
            ((LinearLayout)findViewById(R.id.searchResult)).setVisibility(View.VISIBLE);
        }u
        else
        {
            Toast.makeText(getApplicationContext(),"Stockist not found",Toast.LENGTH_SHORT).show();
        }*/

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Stockists");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    if(ds.getKey().equalsIgnoreCase(stId))
                    {
                        ((LinearLayout)findViewById(R.id.searchResult)).setVisibility(View.VISIBLE);
                         searchedStockists = ds.getValue(Stockists.class);
                        tvSearchedStockist.setText(searchedStockists.getEnterpriseName());
                        stockistExists=true;
                        break;
                    }
                }
                if(!stockistExists)
                {
                    Toast.makeText(getApplicationContext(),"Stockist not found",Toast.LENGTH_SHORT).show();
                    ((LinearLayout)findViewById(R.id.searchResult)).setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void SendReq(View v)
    {
        isPresent=false;
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Connections").child("Retailers").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    Connections connections = ds.getValue(Connections.class);
                    if(connections.getStockistId().equalsIgnoreCase(searchedStockists.getId()) )
                    {
                        if(connections.getConnectionStatus().equalsIgnoreCase("1"))
                            Toast.makeText(getApplicationContext(),"Connection Request already sent",Toast.LENGTH_SHORT).show();
                        else if(connections.getConnectionStatus().equalsIgnoreCase("2"))
                            Toast.makeText(getApplicationContext(),"Connection already present",Toast.LENGTH_SHORT).show();

                        isPresent=true;
                    }

                }
                if(!isPresent) {
                    final Connections connections = new Connections();
                    connections.setRetailerId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    connections.setStockistId(searchedStockists.getId());
                    connections.setConnectionStatus("1");
                    connections.setSender("Retailer");
                    final int random = (int) (Math.random() * 99999999);
                    connections.setId(random + "");

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Connections").child("Retailers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(random + "");
                    databaseReference.setValue(connections).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child("Connections").child("Stockists").child(searchedStockists.getId()).child(random + "");
                            databaseReference2.setValue(connections).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(getApplicationContext(), "Request Sent To Stockist", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        }




    public void removestockist(View v)
    {
       startActivity(new Intent(getApplicationContext(),StockistDetailActivity.class));
    }

}
