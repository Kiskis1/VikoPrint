package lt.viko.eif.vikoprint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


import lt.viko.eif.vikoprint.Fragments.AboutFragment;
import lt.viko.eif.vikoprint.Fragments.HomeFragment;
import lt.viko.eif.vikoprint.Fragments.LoginFragment;
import lt.viko.eif.vikoprint.Fragments.ScanFragment;
import lt.viko.eif.vikoprint.Model.Profile;
import lt.viko.eif.vikoprint.Model.Transaction;
import lt.viko.eif.vikoprint.Repositories.TransactionRepository;
import lt.viko.eif.vikoprint.ViewModel.ProfileViewModel;
import lt.viko.eif.vikoprint.ViewModel.TransactionViewModel;


public class MainActivity extends AppCompatActivity {

    public Transaction trans;
    private FirebaseAuth firebaseUser = FirebaseAuth.getInstance();
    private TransactionViewModel transactionViewModel;
    private ProfileViewModel profileViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.menu);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new LoginFragment()).commit();
        }

        transactionViewModel = ViewModelProviders.of(this).get(TransactionViewModel.class);
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Log.d("MainActivity", "Scanned");
                Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();

                String json = result.getContents();
                try {
                    Gson gson = new Gson();
                    trans = gson.fromJson(json, Transaction.class);
                    trans.setFirebaseUser(firebaseUser.getUid());
                    transactionViewModel.saveTransaction(trans);
                    profileViewModel.deductPoints(trans);

                    //Log.d("profile", "" + profileViewModel.getProfile().getValue());
                    //deductPoints(trans, profileViewModel.getProfile().getValue());
                    Log.d("GSON", trans.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.navigation_scan:
                            selectedFragment = new ScanFragment();
                            break;
                        case R.id.navigation_profile:
                            selectedFragment = new AboutFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };

    }
