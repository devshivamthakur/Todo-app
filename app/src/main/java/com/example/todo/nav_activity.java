package com.example.todo;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class nav_activity extends AppCompatActivity {

    static Context c=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        utils.getInstance(getApplicationContext());
        c=nav_activity.this;
        setContentView(R.layout.activity_nav_activity);
        Toolbar toolbar=findViewById(R.id.toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }
    Long pressed=100L;
    @Override
    public void onBackPressed() {
        if(pressed+1000>System.currentTimeMillis()){
            super.onBackPressed();
        }
        Toast.makeText(this,"press again to exit",Toast.LENGTH_SHORT).show();
        pressed=System.currentTimeMillis();
    }
}