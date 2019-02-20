package retailer.tekmeda.com.tekmedaretailerfmcg.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import retailer.tekmeda.com.tekmedaretailerfmcg.AlreadyOrderedMedicineActivity;
import retailer.tekmeda.com.tekmedaretailerfmcg.MainActivity;
import retailer.tekmeda.com.tekmedaretailerfmcg.R;
import retailer.tekmeda.com.tekmedaretailerfmcg.bean.Orders;


public class OrdersAdapter extends ArrayAdapter<Orders> {

    Context con;
    public OrdersAdapter(@NonNull Context context, @NonNull List<Orders> ordersList) {
        super(context, 0, ordersList);
        con=context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.orderedmedicineview, parent, false);

        final Orders orders =getItem(position);
        TextView medicineName = (TextView)convertView.findViewById(R.id.tvMedicineName);
        TextView medicineQty = (TextView)convertView.findViewById(R.id.tvMedicineQty);
        TextView modified = (TextView)convertView.findViewById(R.id.tvModified);
        if(!orders.isModified())
            modified.setVisibility(View.GONE);
       // orderNumb.setText("Order# :"+orders.getOrderNumber());*/
                medicineName.setText(orders.getMedicineName());
        medicineQty.setText(orders.getMedicineQty()+" "+orders.getUnit());
        ImageView deleteBtn = (ImageView) convertView.findViewById(R.id.ivDelete);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(con)
                        .setTitle("Delete Order")
                        .setMessage("Do you really want to remove order for "+ orders.getMedicineName() +" ?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                AlreadyOrderedMedicineActivity.ordersList.remove(orders);
                                OrdersAdapter.this.notifyDataSetChanged();
                                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("DraftOrders").child("Retailers").child(user.getUid()).child(orders.getStockistId()).child(orders.getOrderNumber()).child(orders.getOrderId());
                                databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child("DraftOrders").child("Stockists").child(orders.getStockistId()).child(user.getUid()).child(orders.getOrderNumber()).child(orders.getOrderId());
                                        databaseReference2.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(con,"Order Removed",Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }
                                });


                            }})
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();
            }
        });
        return convertView;

    }
}
