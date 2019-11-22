package lt.viko.eif.vikoprint.Repositories;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import lt.viko.eif.vikoprint.Model.Profile;

public class ProfileRepository {
    private static final ProfileRepository ourInstance = new ProfileRepository();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseUser = FirebaseAuth.getInstance();

    public static ProfileRepository getInstance() {
        return ourInstance;
    }

    private ProfileRepository() {
    }

    public Task<Void> saveProfile(Profile profile){
        return db.collection("profiles").document(firebaseUser.getCurrentUser().getUid()).set(profile);
    }

    public CollectionReference getProfiles(){
        return db.collection("profiles");
    }

    public DocumentReference getProfile(){
        return db.collection("profiles").document(firebaseUser.getCurrentUser().getUid());
    }

    public Task<DocumentSnapshot> getProfilis(){
        return db.collection("profiles").document(firebaseUser.getCurrentUser().getUid()).get();
    }

}
