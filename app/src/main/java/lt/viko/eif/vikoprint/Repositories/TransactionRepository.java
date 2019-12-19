package lt.viko.eif.vikoprint.Repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import lt.viko.eif.vikoprint.Model.Transaction;

public class TransactionRepository {
    private static final TransactionRepository ourInstance = new TransactionRepository();

    public static TransactionRepository getInstance() {
        return ourInstance;
    }

    private TransactionRepository() {
    }

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseUser = FirebaseAuth.getInstance();

    public Task<Void> saveTransaction(Transaction trans){
        return db.collection("transactions").document().set(trans);
    }

    public CollectionReference getTransactions(){
        return db.collection("transactions");
    }

    public void getUserTransactions(){
         db.collection("transactions")
                .whereEqualTo("firebaseUser", firebaseUser.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("transrepdoc", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("transrepdoc", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }


}
