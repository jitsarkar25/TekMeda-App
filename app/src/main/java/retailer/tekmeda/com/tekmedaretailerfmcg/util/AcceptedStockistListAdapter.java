package retailer.tekmeda.com.tekmedaretailerfmcg.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import retailer.tekmeda.com.tekmedaretailerfmcg.R;
import retailer.tekmeda.com.tekmedaretailerfmcg.bean.Stockists;

public class AcceptedStockistListAdapter extends ArrayAdapter<Stockists> {

    public AcceptedStockistListAdapter(@NonNull Context context, @NonNull List<Stockists> stockistsList) {
        super(context, 0, stockistsList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.acceptedstockistlistview, parent, false);

        Stockists stockists =getItem(position);
        TextView enterpriseName = (TextView)convertView.findViewById(R.id.tvStockistNameAccepted);
       // TextView area = (TextView)convertView.findViewById(R.id.tvStockistArea);
        enterpriseName.setText(stockists.getEnterpriseName());
       // area.setText(stockists.getArea());

        return convertView;

    }
}
