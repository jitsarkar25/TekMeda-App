package retailer.tekmeda.com.tekmedaretailerfmcg;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import retailer.tekmeda.com.tekmedaretailerfmcg.bean.Stockists;

public class StockistDetailActivity extends AppCompatActivity {

    private TextView tvStockistName,tvStockistAddress;
    private Stockists stockists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stockist_detail);
        tvStockistName = (TextView)findViewById(R.id.tvStockistDetailsName);
        tvStockistAddress = (TextView)findViewById(R.id.tvStockistDetailsAddress);
        stockists = (Stockists)getIntent().getSerializableExtra("stockistDetails");
        tvStockistName.setText(stockists.getEnterpriseName());
        tvStockistAddress.setText(stockists.getArea());

    }

    public void calStockist(View v)
    {
        String phone = stockists.getPhone();
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
        startActivity(intent);
    }
}
