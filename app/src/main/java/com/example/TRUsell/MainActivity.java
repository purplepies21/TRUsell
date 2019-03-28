package com.example.TRUsell;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {


    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    private ActionBarDrawerToggle actionBarDrawerToggle;
    private RecyclerView saleList;
    private DatabaseReference userReference;


    //FIREBASE TOOLS

    private FirebaseAuth firebaseAuth;

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=findViewById(R.id.toolbar_layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Items for sale!");
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this,drawerLayout, R.string.drawer_open,R.string.drawer_close );
       FirebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance();
        userReference = FirebaseDatabase.getInstance().getReference().child("Users");

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = findViewById(R.id.navigation_view);
        View headerView = navigationView.inflateHeaderView(R.layout.navigation_header);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


                MenuSelector(menuItem);


                return false;
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {

        return true;


        }
        return super.onOptionsItemSelected(item);
    }

    //This is to use the options in the navigation bar
    private void MenuSelector(MenuItem menuItem) {

        switch (menuItem.getItemId()){

            case  R.id.nav_home:

                break;
            case  R.id.nav_friends:
                Toast.makeText(this, "Friends!", Toast.LENGTH_SHORT).show();
                break;

            case  R.id.nav_logout:
                Toast.makeText(this, "logout", Toast.LENGTH_SHORT).show();

                break;
            case  R.id.nav_messages:

                Toast.makeText(this, "Messages!", Toast.LENGTH_SHORT).show();

                break;
            case  R.id.nav_search:
                Toast.makeText(this, "Search!", Toast.LENGTH_SHORT).show();

                break;
            case  R.id.nav_sell:
                Toast.makeText(this, "Sell!", Toast.LENGTH_SHORT).show();

                break;
            case  R.id.nav_settings:
                Toast.makeText(this, "settings!", Toast.LENGTH_SHORT).show();

                break;




            default:

        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if(currentUser == null){
            SendToLoginActivity();

        }else{
            CheckDBForUser();

        }
    }

    private void CheckDBForUser() {
        final String userID = firebaseAuth.getCurrentUser().getUid();
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(userID)){
                    SendtoSetupActivity();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void SendtoSetupActivity() {
        Intent setupIntent = new Intent(MainActivity.this, SetupActivity.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(setupIntent);

        finish();
    }

    private void SendToLoginActivity() {

        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(loginIntent);

        finish();
    }
}
