package com.example.cw1_s2328015.view;



//
// Name                 Edward Gordon
// Student ID           S2328015
// Programme of Study   software development
//

// UPDATE THE PACKAGE NAME to include your Student Identifier

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.cw1_s2328015.R;
import com.example.cw1_s2328015.viewmodel.TrafficViewModel;


public class MainActivity extends AppCompatActivity {

    private TrafficViewModel viewModel;
    private ViewSwitcher viewSwitcher; // switches betweelist screen and map screen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        Bundle metaData;
        try {
            metaData = getPackageManager()
                    .getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA).metaData;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
        String clientId = metaData.getString("com.example.cw1_s2328015.MY_CLIENT_ID");
        String clientKey = metaData.getString("com.example.cw1_s2328015.MY_KEY");

        // created once, then reused after rotation
        viewModel = new ViewModelProvider(this).get(TrafficViewModel.class);
        viewModel.start(clientId, clientKey, getFilesDir());

        // Show a Toast when the ViewModel reports a failed download.
        viewModel.getMessage().observe(this, message -> {
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                viewModel.clearMessage();
            }
        });


        // The landscape layout has no switcher thats why its null
        viewSwitcher = (ViewSwitcher) findViewById(R.id.vwSwitch);
        if (viewSwitcher != null) {
            viewSwitcher.setDisplayedChild(viewModel.getCurrentScreen());
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameHolder, new EventListFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.list) {
            viewSwitcher.setDisplayedChild(0);
            viewModel.setCurrentScreen(0);
        } else if (item.getItemId() == R.id.map) {
            viewSwitcher.setDisplayedChild(1);
            viewModel.setCurrentScreen(1);
        } else if (item.getItemId() == R.id.refresh) {
            Toast.makeText(this, "Refreshing traffic data", Toast.LENGTH_SHORT).show();
            viewModel.loadEvents();
        } else {
            return super.onOptionsItemSelected(item);
        }

        return true;
    }
}
