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
import retailer.tekmeda.com.tekmedaretailerfmcg.bean.Medicine;
import retailer.tekmeda.com.tekmedaretailerfmcg.bean.Stockists;

public class NewMedicineAdapter extends ArrayAdapter<Medicine> {

    public NewMedicineAdapter(@NonNull Context context, @NonNull List<Medicine> medicineList) {
        super(context, 0, medicineList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.newmedicinecontents, parent, false);

        Medicine medicine =getItem(position);
        TextView medicineName = (TextView)convertView.findViewById(R.id.tvNewMedName);
        TextView medicineCompany = (TextView)convertView.findViewById(R.id.tvNewMedCompany);
        TextView medicineType = (TextView)convertView.findViewById(R.id.tvNewMedType);
        medicineName.setText(medicine.getName());
        medicineCompany.setText(medicine.getCompany());
        medicineType.setText(medicine.getGeneric());


        return convertView;

    }
}
