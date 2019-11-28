package lt.viko.eif.vikoprint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


import java.io.IOException;
import java.util.Iterator;

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

    private static final String SCHEMA = "{\n" +
            "  \"$schema\": \"http://json-schema.org/draft-04/schema#\",\n" +
            "  \"type\": \"object\",\n" +
            "  \"properties\": {\n" +
            "    \"date\": {\n" +
            "      \"type\": \"string\"\n" +
            "    },\n" +
            "    \"points\": {\n" +
            "      \"type\": \"integer\"\n" +
            "    },\n" +
            "    \"pageCount\": {\n" +
            "      \"type\": \"integer\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"required\": [\n" +
            "    \"date\",\n" +
            "    \"points\",\n" +
            "    \"pageCount\"\n" +
            "  ]\n" +
            "}";

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
                //Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();

                String json = result.getContents();
                if (validate(json,SCHEMA)){
                    try {
                        Gson gson = new Gson();
                        trans = gson.fromJson(json, Transaction.class);
                        trans.setFirebaseUser(firebaseUser.getUid());
                        transactionViewModel.saveTransaction(trans);
                        profileViewModel.deductPoints(trans);
                        Log.d("GSON", trans.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

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

    public boolean validate(String jsonData, String jsonSchema) {
        ProcessingReport report = null;
        boolean result = false;
        try {
            System.out.println("Applying schema: @<@<"+jsonSchema+">@>@ to data: #<#<"+jsonData+">#>#");
            JsonNode schemaNode = JsonLoader.fromString(jsonSchema);
            JsonNode data = JsonLoader.fromString(jsonData);
            JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
            JsonSchema schema = factory.getJsonSchema(schemaNode);
            report = schema.validate(data);
        } catch (JsonParseException jpex) {
            System.out.println("Error. Something went wrong trying to parse json data: #<#<"+jsonData+
                    ">#># or json schema: @<@<"+jsonSchema+">@>@. Are the double quotes included? "+jpex.getMessage());
            //jpex.printStackTrace();
        } catch (ProcessingException pex) {
            System.out.println("Error. Something went wrong trying to process json data: #<#<"+jsonData+
                    ">#># with json schema: @<@<"+jsonSchema+">@>@ "+pex.getMessage());
            //pex.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error. Something went wrong trying to read json data: #<#<"+jsonData+
                    ">#># or json schema: @<@<"+jsonSchema+">@>@");
            //e.printStackTrace();
        }
        if (report != null) {
            Iterator<ProcessingMessage> iter = report.iterator();
            while (iter.hasNext()) {
                ProcessingMessage pm = iter.next();
                System.out.println("Processing Message: "+pm.getMessage());
            }
            result = report.isSuccess();
        }
        System.out.println(" Result=" +result);
        return result;
    }

    }
