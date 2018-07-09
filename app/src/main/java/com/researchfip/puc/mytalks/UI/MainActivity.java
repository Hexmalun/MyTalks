package com.researchfip.puc.mytalks.UI;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.researchfip.puc.mytalks.R;
import com.researchfip.puc.mytalks.Services.CallService;
import com.researchfip.puc.mytalks.Services.SMSService;
import com.researchfip.puc.mytalks.UI.fragments.CallsAndSMSFragment2;
import com.researchfip.puc.mytalks.UI.fragments.DataFragment;
import com.researchfip.puc.mytalks.UI.fragments.HelpFragment;
import com.researchfip.puc.mytalks.UI.fragments.HomeFragment;
import com.researchfip.puc.mytalks.UI.fragments.MapFragment;
import com.researchfip.puc.mytalks.UI.fragments.PreferencesFragment;
import com.researchfip.puc.mytalks.general.PhoneInformation;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    Fragment fragment;
    FragmentTransaction ft;
    public NavigationView navigationView;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.content_main, new HomeFragment(),"My Talks");
        ft.commit();
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()){
            case R.id.nav_home:
                fragment = new HomeFragment();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment);
                ft.commit();
                break;
            case R.id.nav_calls:
                Bundle bundle = new Bundle();
                bundle.putInt("TYPE_EVENT", PhoneInformation.CALL_SERVICE_ID);
                //fragment = new CallsAndSMSFragment();
                fragment = new CallsAndSMSFragment2();
                fragment.setArguments(bundle);
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment);
                ft.commit();
                break;
            case R.id.nav_sms:
                bundle = new Bundle();
                bundle.putInt("TYPE_EVENT", PhoneInformation.SMS_SERVICE_ID);
                //fragment = new CallsAndSMSFragment();
                fragment = new CallsAndSMSFragment2();
                fragment.setArguments(bundle);
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment);
                ft.commit();
                break;
            case R.id.nav_data:
                fragment = new DataFragment();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment);
                ft.commit();
                break;
            case R.id.nav_map:
                fragment = new MapFragment();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment);
                ft.commit();
                break;
            case R.id.nav_preferences:
                fragment = new PreferencesFragment();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment);
                ft.commit();
                break;
            case R.id.nav_help:
                fragment = new HelpFragment();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment);
                ft.commit();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {

    }

    public void onClickHandlercall(View v) {
        Bundle bundle = new Bundle();
        bundle.putInt("TYPE_EVENT", PhoneInformation.CALL_SERVICE_ID);
        //fragment = new CallsAndSMSFragment();
        fragment = new CallsAndSMSFragment2();
        fragment.setArguments(bundle);
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_main, fragment);
        ft.commit();
    }

    public void onClickHandlersms(View v) {
        Bundle bundle = new Bundle();
        bundle.putInt("TYPE_EVENT", PhoneInformation.SMS_SERVICE_ID);
        fragment = new CallsAndSMSFragment2();
        //fragment = new CallsAndSMSFragment();
        fragment.setArguments(bundle);
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_main, fragment);
        ft.commit();
    }

}
