package lt.viko.eif.vikoprint.ViewModel;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.List;

import lt.viko.eif.vikoprint.MainActivity;
import lt.viko.eif.vikoprint.Model.Profile;
import lt.viko.eif.vikoprint.Model.Transaction;
import lt.viko.eif.vikoprint.Repositories.ProfileRepository;

public class ProfileViewModel extends ViewModel {

    private ProfileRepository repository = ProfileRepository.getInstance();

    private MutableLiveData<Profile> profile  = new MutableLiveData<>();


    public LiveData<Profile> getProfile(){
        repository.getProfile().addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                Profile prof = documentSnapshot.toObject(Profile.class);
                profile.setValue(prof);
        }
        });
        return profile;
    }

    public void saveProfile (Profile profile) {
        repository.saveProfile(profile);
    }

    public void deductPoints(final Transaction trans){
        repository.getProfilis().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        Profile prof  = document.toObject(Profile.class);
                        if (prof.getPoints() > trans.getPoints()){
                            prof.setPoints(prof.getPoints()-trans.getPoints());
                            repository.saveProfile(prof);
                            Log.d("document", prof.toString());
                        }
                    }
                    else {
                        Log.d("Document","DOCUMENT NULL");
                    }
                }
                else {
                    Log.d("document", "task failed");
                }
            }
        });

    }

    public void addPoints(final int points){
        repository.getProfilis().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        Profile prof  = document.toObject(Profile.class);
                            prof.setPoints(prof.getPoints()+points);
                            repository.saveProfile(prof);
                            Log.d("document", prof.toString());
                    }
                    else {
                        Log.d("Document","DOCUMENT NULL");
                    }
                }
                else {
                    Log.d("document", "task failed");
                }
            }
        });
    }
}
