package retailer.tekmeda.com.tekmedaretailerfmcg;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import retailer.tekmeda.com.tekmedaretailerfmcg.bean.Retailers;

public class ProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView profileName;
    private TextView tvNumberOfOrdersAccepted;
    private TextView tvNumberOfOrdersDelivered;
    private TextView tvNumberOfOrdersDraft;
    private TextView tvNumberOfOrdersPlaced;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        tvNumberOfOrdersAccepted = (TextView)findViewById(R.id.tvNumberOfOrdersAccepted);
        tvNumberOfOrdersDelivered= (TextView)findViewById(R.id.tvNumberOfOrdersDelivered);
        tvNumberOfOrdersDraft= (TextView)findViewById(R.id.tvNumberOfOrdersDraft);
        tvNumberOfOrdersPlaced = (TextView)findViewById(R.id.tvNumberOfOrdersPlaced);

        profileName = (TextView)findViewById(R.id.tvProfileName) ;
        user=FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Retailers");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    if (child.getKey().equals(user.getUid())) {

                      Retailers  retailers = child.getValue(Retailers.class);
                      profileName.setText(retailers.getRetailName());

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("AcceptedOrders").child("Retailers");
        databaseReference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.getKey().equalsIgnoreCase(user.getUid().toString())) {
                    int count = 0;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        count = count + (int) ds.getChildrenCount();
                    }
                    tvNumberOfOrdersAccepted.setText(count + "");
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.getKey().equalsIgnoreCase(user.getUid().toString())) {
                    int count = 0;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        count = count + (int) ds.getChildrenCount();
                    }
                    tvNumberOfOrdersAccepted.setText(count + "");
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getKey().equalsIgnoreCase(user.getUid().toString()))
                    tvNumberOfOrdersAccepted.setText(0+"");
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child("DeliveredOrders").child("Retailers");
        databaseReference2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.getKey().equalsIgnoreCase(user.getUid().toString())) {
                    int count = 0;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        count = count + (int) ds.getChildrenCount();
                    }
                    tvNumberOfOrdersDelivered.setText(count + "");
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.getKey().equalsIgnoreCase(user.getUid().toString())) {
                    int count = 0;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        count = count + (int) ds.getChildrenCount();
                    }
                    tvNumberOfOrdersDelivered.setText(count + "");
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getKey().equalsIgnoreCase(user.getUid().toString()))
                    tvNumberOfOrdersDelivered.setText(0+"");
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference().child("PlacedOrders").child("Retailers");
        databaseReference3.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.getKey().equalsIgnoreCase(user.getUid().toString())) {
                    int count = 0;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        count = count + (int) ds.getChildrenCount();
                    }
                    tvNumberOfOrdersPlaced.setText(count + "");
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.getKey().equalsIgnoreCase(user.getUid().toString())) {
                    int count = 0;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        count = count + (int) ds.getChildrenCount();
                    }
                    tvNumberOfOrdersPlaced.setText(count + "");
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getKey().equalsIgnoreCase(user.getUid().toString()))
                    tvNumberOfOrdersPlaced.setText(0+"");
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference databaseReference4 = FirebaseDatabase.getInstance().getReference().child("DraftOrders").child("Retailers");
        databaseReference4.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if(dataSnapshot.getKey().equalsIgnoreCase(user.getUid().toString())) {
                    int count = 0;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        count = count + (int) ds.getChildrenCount();
                    }
                    tvNumberOfOrdersDraft.setText(count + "");
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("Draft Child Changed ",dataSnapshot.getKey());
                Log.d("Draft Child bool",String.valueOf(dataSnapshot.getKey().contains(user.getUid().toString())));
                if(!dataSnapshot.getKey().contains(user.getUid().toString()))
                    tvNumberOfOrdersDraft.setText(0 + "");
                if(dataSnapshot.getKey().equalsIgnoreCase(user.getUid().toString())) {
                    int count = 0;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        count = count + (int) ds.getChildrenCount();
                    }
                    tvNumberOfOrdersDraft.setText(count + "");
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Log.d("Draft Child removed ",dataSnapshot.getKey());
                Log.d("Draft Child bool",String.valueOf(dataSnapshot.getKey().contains(user.getUid().toString())));
                if(dataSnapshot.getKey().equalsIgnoreCase(user.getUid().toString()))
                    tvNumberOfOrdersDraft.setText(0+"");
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            startActivity(new Intent(getApplicationContext(),NewOrderPageActivity.class));
        } else if (id == R.id.nav_slideshow) {
            startActivity(new Intent(getApplicationContext(),OrdersOnDeliveryStockistActivity.class));
        } else if (id == R.id.nav_manage) {
            startActivity(new Intent(getApplicationContext(),DeliveredOrdersStockistActivity.class));
        } else if (id == R.id.reject_order_nav) {
            startActivity(new Intent(getApplicationContext(),RejectedOrdersDetailsActivity.class));
        }
        else if (id == R.id.nav_share) {
            startActivity(new Intent(getApplicationContext(),StockistConnectionActivity.class));
        }
        else if (id == R.id.nav_newmed) {
            startActivity(new Intent(getApplicationContext(),NewMedicineActivity.class));
        }else if(id == R.id.nav_profile)
        {
            Intent intent = new Intent(getApplicationContext(),ProfileDetailsActivity.class);
            intent.putExtra("edit",true);
            startActivity(intent);
        }
        else if(id == R.id.nav_placed)
        {
            Intent intent = new Intent(getApplicationContext(),PlacedOrdersStockistActivity.class);
           // intent.putExtra("edit",true);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void draftorder(View v)
    {
        startActivity(new Intent(getApplicationContext(),NewOrderPageActivity.class));
    }
    public void myconnection(View v)
    {
        startActivity(new Intent(getApplicationContext(),StockistConnectionActivity.class));
    }
    public void newmedicine(View v)
    {
        startActivity(new Intent(getApplicationContext(),NewMedicineActivity.class));
    }
}
