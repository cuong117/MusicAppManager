package com.example.musicupload.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.musicupload.R;
import com.example.musicupload.databinding.NavBarLayoutBinding;
import com.example.musicupload.fragment.DevicemusicFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.musicupload.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private NavBarLayoutBinding binding;
    private DrawerLayout main;
    private final int MY_PERMISSION_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("Main", "Alo");
        super.onCreate(savedInstanceState);

        binding = NavBarLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        main = binding.drawerLayout;
        NavigationView navigationView =binding.navView;
        grantedPermission();

        setSupportActionBar(binding.main.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_upload, R.id.nav_cloud, R.id.nav_user).setOpenableLayout(main).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void grantedPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Snackbar snackbar = Snackbar.make(main, "Provide the Storage Permission", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                                .findFragmentById(R.id.nav_host_fragment_content_main);
                        DevicemusicFragment devicemusicFragment =(DevicemusicFragment) navHostFragment
                                .getChildFragmentManager().getFragments().get(0);
                        devicemusicFragment.setSongList();
                    }
                    else {
                        Snackbar snackbar = Snackbar.make(main, "Provide the Storage Permission", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                }
        }
    }
}