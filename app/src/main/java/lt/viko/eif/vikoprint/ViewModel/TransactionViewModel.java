package lt.viko.eif.vikoprint.ViewModel;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.common.collect.TreeRangeMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import lt.viko.eif.vikoprint.Model.Transaction;
import lt.viko.eif.vikoprint.Repositories.TransactionRepository;

public class TransactionViewModel extends ViewModel {

    private TransactionRepository repository = TransactionRepository.getInstance();

    private FirebaseAuth firebaseUser = FirebaseAuth.getInstance();

    private MutableLiveData<List<Transaction>> trans = new MutableLiveData<>();

    private List<Transaction> listas = new ArrayList<>();



    public LiveData<List<Transaction>> getTransactions(){
        listas.clear();
        repository.getTransactions().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    Transaction item = queryDocumentSnapshot.toObject(Transaction.class);
                    if (item.getFirebaseUser().equals(firebaseUser.getCurrentUser().getUid()))
                        listas.add(item);
                }
                trans.setValue(listas);
            }
        });

        return trans;
    }

    public void saveTransaction (Transaction trans) {
        repository.saveTransaction(trans);
    }



}
