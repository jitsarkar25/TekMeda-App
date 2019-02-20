package retailer.tekmeda.com.tekmedaretailerfmcg.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retailer.tekmeda.com.tekmedaretailerfmcg.AlreadyOrderedMedicineActivity;
import retailer.tekmeda.com.tekmedaretailerfmcg.MainActivity;
import retailer.tekmeda.com.tekmedaretailerfmcg.OrderMedicineActivity;
import retailer.tekmeda.com.tekmedaretailerfmcg.R;
import retailer.tekmeda.com.tekmedaretailerfmcg.bean.Orders;


public class RejectedOrderAdapter extends ArrayAdapter<Orders> {

    Context con;
    public RejectedOrderAdapter(@NonNull Context context, @NonNull List<Orders> ordersList) {
        super(context, 0, ordersList);
        con=context;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.rejectedmedicinescontent, parent, false);

        final Orders orders =getItem(position);
        TextView medicineName = (TextView)convertView.findViewById(R.id.tvMedicineNameRejected);
        TextView medicineQty = (TextView)convertView.findViewById(R.id.tvMedicineQtyRejected);
        TextView time = (TextView)convertView.findViewById(R.id.tvReorder);

        medicineName.setText(orders.getMedicineName());
        medicineQty.setText(orders.getMedicineQty()+" "+orders.getUnit());


        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(con, OrderMedicineActivity.class);
                intent.putExtra("orders",orders);
                intent.putExtra("stockistId",orders.getStockistId());
                intent.putExtra("isreorder",true);
                intent.putExtra("position",position);
                ((Activity)con).startActivityForResult(intent,1002);
            }
        });

        return convertView;

    }

}
