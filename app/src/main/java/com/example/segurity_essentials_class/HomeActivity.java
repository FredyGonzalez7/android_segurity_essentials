package com.example.segurity_essentials_class;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {

    private TextView emailDBRealTime, nameDBRealTime, nameDBLocal, latLong;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference database;

    private static String TAG = "Fredy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        nameDBRealTime = findViewById(R.id.textViewNameDBRealTime);
        emailDBRealTime = findViewById(R.id.textViewEmailDBRealTime);
        nameDBLocal = findViewById(R.id.textViewNameDBLocal);
        latLong = findViewById(R.id.textViewLatLong);
        Button signOut = findViewById(R.id.buttonSignOut);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
                nameDBRealTime.setText("");
                emailDBRealTime.setText("");
                nameDBLocal.setText("");
                latLong.setText("");
                goToMainActivity();
            }
        });

        latLong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLatLong();
            }
        });

        informationUser();
        permissionLocation();
    }

    private void permissionLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                setLatLong();
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
                // setLatLong();
                // 1 identifica cuando termine la ejecucion
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            setLatLong();
        }
    }

    private void signOut() { firebaseAuth.signOut(); }
    private void goToMainActivity(){ startActivity(new Intent(HomeActivity.this,MainActivity.class)); }

    @SuppressLint("SetTextI18n")
    private void informationUser() {
        FirebaseUser userAuth = FirebaseAuth.getInstance().getCurrentUser();
        if (userAuth != null) {
            // verifica si el usuario de la sesion tiene nombre, en caso de estar registrado por google...
            /*
            if (Objects.equals(userAuth.getDisplayName(), "") || userAuth.getDisplayName() == null) {

                nameDBRealTime.setText("Unregistered names");
            }
            else {
                nameDBRealTime.setText(userAuth.getDisplayName());
            }
            */
            database.child("user").child(userAuth.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user =  dataSnapshot.getValue(User.class);
                        if (user != null) {
                            nameDBRealTime.setText(user.getName());
                            emailDBRealTime.setText(user.getEmail());
                        }
                        else {
                            nameDBRealTime.setText("Unregistered names");
                            emailDBRealTime.setText("Unregistered email");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        //error en con el usuerio en DBRealTime
                        Log.d(TAG, "onCancelled: ");
                    }
                });
            DBHelper dbHelper = new DBHelper(getApplicationContext());
            String nameUserDBLocal = dbHelper.getUserName(getApplicationContext(),userAuth.getEmail());
            nameDBLocal.setText(nameUserDBLocal);
        } else {
            Log.d(TAG, "informationUser: No User");
            startActivity(new Intent(HomeActivity.this, MainActivity.class));
        }
    }

    @SuppressLint("SetTextI18n")
    private void setLatLong() {
        // Acquire a reference to the system Location Manager
        Log.d("dentro", "setLatLong: ");
        LocationManager locationManager = (LocationManager) HomeActivity.this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            @SuppressLint("SetTextI18n")
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                latLong.setText("Lat:"+location.getLatitude()+" - Long:"+location.getLongitude());
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            @SuppressLint("SetTextI18n")
            public void onProviderDisabled(String provider) {
            }
        };

        // Register the listener with the Location Manager to receive location updates
        if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            assert locationManager != null;
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }
        else {
            latLong.setText("Unregistered Location");
            //Toast.makeText(HomeActivity.this, "Location access permission not enabled",Toast.LENGTH_SHORT).show();
        }
    }
}