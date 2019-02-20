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


public class PlacedOrderAdapter extends ArrayAdapter<Orders> {

    Context con;
    public PlacedOrderAdapter(@NonNull Context context, @NonNull List<Orders> ordersList) {
        super(context, 0, ordersList);
        con=context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.placedorderscontents, parent, false);

        final Orders orders =getItem(position);
        TextView medicineName = (TextView)convertView.findViewById(R.id.tvorderedMedicineNamePlaced);
        TextView medicineQty = (TextView)convertView.findViewById(R.id.tvorderedMedicineQtyPlaced);
        TextView orderNumb = (TextView)convertView.findViewById(R.id.tvOrderNumberPlaced);
        if(orders.isShowOrderNumber())
        {
            orderNumb.setText("Order# :"+orders.getOrderNumber());
        }
        else{
            orderNumb.setVisibility(View.GONE);
        }

        medicineName.setText(orders.getMedicineName());
        medicineQty.setText(orders.getMedicineQty()+" "+orders.getUnit());

        return convertView;

    }
}
