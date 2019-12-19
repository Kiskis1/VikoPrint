package lt.viko.eif.vikoprint.Repositories;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class StorageRepository {
    private static final StorageRepository ourInstance = new StorageRepository();

    public static StorageRepository getInstance() {
        return ourInstance;
    }
    private StorageRepository() {}

    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    private StorageReference pathReference = mStorageRef.child("download.png");

    private String URL;

    public String getDefaultImage(){
        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                URL = uri.toString();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.d("getimage", "failed");
            }
        });
        return URL;
    }



}
