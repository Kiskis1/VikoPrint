package lt.viko.eif.vikoprint.Fragments;


import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AdditionalUserInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import lt.viko.eif.vikoprint.Model.Profile;
import lt.viko.eif.vikoprint.R;
import lt.viko.eif.vikoprint.Repositories.ProfileRepository;
import lt.viko.eif.vikoprint.ViewModel.ProfileViewModel;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {


    public LoginFragment() {
        // Required empty public constructor
    }


    private static final int RC_SIGN_IN = 123;
    private BottomNavigationView navBar;
    private ProfileViewModel profileViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navBar = getActivity().findViewById(R.id.menu);
        navBar.setVisibility(View.GONE);

        Button signoutBtn = view.findViewById(R.id.SignOut);
        signoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        final MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.beep);

        Button loginBtn = view.findViewById(R.id.SignIn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<AuthUI.IdpConfig> providers = Arrays.asList(
                        new AuthUI.IdpConfig.EmailBuilder().build(),
                        new AuthUI.IdpConfig.GoogleBuilder().build());
                //new AuthUI.IdpConfig.PhoneBuilder().build(),
                //new AuthUI.IdpConfig.FacebookBuilder().build(),
                //new AuthUI.IdpConfig.TwitterBuilder().build());

                // Create and launch sign-in intent
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .build(),
                        RC_SIGN_IN);

                mp.start();
            }
        });
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });

    }



    // [START auth_fui_result]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                userExists2(firebaseUser);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).commit();
                navBar.setVisibility(View.VISIBLE);

            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                Toast.makeText(getContext(), "NEPAVYKO PRISIJUNGTI", Toast.LENGTH_LONG).show();
            }
        }
    }
    // [END auth_fui_result]

    public void signOut() {
        // [START auth_fui_signout]
        AuthUI.getInstance()
                .signOut(getView().getContext())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
        // [END auth_fui_signout]
    }
    private void userExists2(final FirebaseUser firebaseUser){

        FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
        mDatabase.collection("profiles")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        boolean egzistuoja = false;
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d("DOCUMENT", "" + document.getData());

                                if (document.getId().equals(firebaseUser.getUid())){
                                    egzistuoja = true;
                                    //Log.d("EGZISTUOJA", String.valueOf(egzistuoja));
                                }
                               // Log.d("FIREBASEDOCUMENT", document.getId() + " => " + document.getData());
                            }

                            if (!egzistuoja){
                                //Log.d("USEREMAIL", firebaseUser.getEmail());

                                Profile profile = new Profile(firebaseUser.getEmail(), firebaseUser.getPhotoUrl().toString(), 0);
                                //sukurti nauja irasa DB
                                profileViewModel.saveProfile(profile);
                                //Log.d("CREATIONDATE", String.valueOf(firebaseUser.getMetadata().getCreationTimestamp()));
                                //Log.d("LASTLOGIN", String.valueOf(firebaseUser.getMetadata().getLastSignInTimestamp()));
                            }
                        }
                        else {
                            Log.d("ERRORFIREBASEDOCUMENT", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}

/*
    public boolean exists;
    private boolean userExists(FirebaseUser firebaseUser){
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        DocumentReference docIdRef = rootRef.collection("profiles").document(firebaseUser.getUid());
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        exists = true;
                        Log.d("USEREXISTS", "Document exists!");

                    } else {
                        exists = false;
                        Log.d("USEREXISTS", "Document does not exist!");
                    }
                } else {
                    Log.d("USEREXISTS", "Failed with: ", task.getException());
                }
            }
        });
        return exists;
    }
*/
/*
                if (!userExists2(firebaseUser)) {
                    Log.d("USEREXISTS", firebaseUser.getEmail());
                    Profile profile = new Profile(firebaseUser.getEmail(), firebaseUser.getPhotoUrl(), 0);
                    //sukurti nauja irasa DB
                    profileViewModel.saveProfile(profile);
                    Log.d("CREATIONDATE", String.valueOf(firebaseUser.getMetadata().getCreationTimestamp()));
                    Log.d("LASTLOGIN", String.valueOf(firebaseUser.getMetadata().getLastSignInTimestamp()));
                }
*/

