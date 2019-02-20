package retailer.tekmeda.com.tekmedaretailerfmcg.util;


import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import retailer.tekmeda.com.tekmedaretailerfmcg.R;
import retailer.tekmeda.com.tekmedaretailerfmcg.StockistConnectionActivity;
import retailer.tekmeda.com.tekmedaretailerfmcg.bean.Connections;
import retailer.tekmeda.com.tekmedaretailerfmcg.bean.Stockists;


public class NewRequestAdapter extends ArrayAdapter<Connections> {

    private String Id;
    private Context con;
    private ProgressDialog progressDialog;
    public NewRequestAdapter(@NonNull Context context, @NonNull List<Connections> connectionsList) {
        super(context, 0, connectionsList);
        con=context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.newrequest, parent, false);
        progressDialog = new ProgressDialog(con);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Fetching Data");
       // progressDialog.show();
        final Connections connections =getItem(position);
        Id = connections.getId();
        final TextView medicineName = (TextView)convertView.findViewById(R.id.retailerName);
       // final TextView retailAddress = (TextView)convertView.findViewById(R.id.tvStockistAreaNew);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Stockists");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {

                    Stockists stockists = ds.getValue(Stockists.class);
                    if(stockists.getId().equalsIgnoreCase(connections.getStockistId()))
                    {
                        medicineName.setText(stockists.getEnterpriseName());
                       // retailAddress.setText(retailers.getAddress());
                        //progressDialog.dismiss();
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Button bAccept = (Button)convertView.findViewById(R.id.bAccept);
        bAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Stockists");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds: dataSnapshot.getChildren())
                        {
                            Stockists stockists = ds.getValue(Stockists.class);
                            if(stockists.getId().equalsIgnoreCase(connections.getStockistId()))
                            {
                               connections.setConnectionStatus("2");
                            }
                        }
                        DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("Connections").child("Retailers").child(connections.getRetailerId()).child(Id);
                        dr.setValue(connections).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                DatabaseReference dr2 = FirebaseDatabase.getInstance().getReference().child("Connections").child("Stockists").child(connections.getStockistId()).child(Id);
                                dr2.setValue(connections).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(con,"Accepted Request",Toast.LENGTH_SHORT).show();
                                        StockistConnectionActivity.connectionsArrayList.remove(connections);
                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Stockists").child(connections.getStockistId());
                                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                StockistConnectionActivity.stockistLists.add(dataSnapshot.getValue(Stockists.class));
                                                StockistConnectionActivity.stockistListAdapter.notifyDataSetChanged();
                                                NewRequestAdapter.this.notifyDataSetChanged();
                                                progressDialog.dismiss();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });


        Button bReject = (Button)convertView.findViewById(R.id.bReject);
        bReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();

                DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("Connections").child("Retailers").child(connections.getRetailerId()).child(Id);
                dr.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        DatabaseReference dr2 = FirebaseDatabase.getInstance().getReference().child("Connections").child("Stockists").child(connections.getStockistId()).child(Id);
                        dr2.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(con, "Rejected Request", Toast.LENGTH_SHORT).show();
                                StockistConnectionActivity.connectionsArrayList.remove(connections);
                                NewRequestAdapter.this.notifyDataSetChanged();
                                progressDialog.dismiss();
                            }
                        });
                    }
                });
            }
        });
        return convertView;

    }
}
