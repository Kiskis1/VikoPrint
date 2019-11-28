package lt.viko.eif.vikoprint.ViewModel;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.common.collect.TreeRangeMap;
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

    private MutableLiveData<List<Transaction>> trans = new MutableLiveData<>();

    private List<Transaction> listas = new ArrayList<>();

    private MutableLiveData<List<Transaction>> items = new MutableLiveData<>();


    public LiveData<List<Transaction>> getTransactions(){
        listas.clear();
        repository.getTransactions().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    Transaction item = queryDocumentSnapshot.toObject(Transaction.class);
                    listas.add(item);
                }
                trans.setValue(listas);
            }
        });

        return trans;
    }

    public void init () {
        items = repository.getMutableLiveDataItems();
    }
    public LiveData<List<Transaction>> getItems(){
        return items;
    }


    public void saveTransaction (Transaction trans) {
        repository.saveTransaction(trans);
    }



}
