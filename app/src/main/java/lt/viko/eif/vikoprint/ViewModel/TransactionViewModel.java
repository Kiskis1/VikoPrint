package lt.viko.eif.vikoprint.ViewModel;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import lt.viko.eif.vikoprint.Model.Profile;
import lt.viko.eif.vikoprint.Model.Transaction;
import lt.viko.eif.vikoprint.Repositories.ProfileRepository;
import lt.viko.eif.vikoprint.Repositories.TransactionRepository;

public class TransactionViewModel extends ViewModel {

    private TransactionRepository repository = TransactionRepository.getInstance();

    private MutableLiveData<List<Transaction>> trans = new MutableLiveData<>();
    private MutableLiveData<Transaction> trans2 = new MutableLiveData<>();
    private List<Transaction> listas = new ArrayList<>();

    public LiveData<Transaction> getTransactions(){
        repository.getTransactions().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                Transaction transaction = (Transaction) queryDocumentSnapshots.toObjects(Transaction.class);
                trans2.setValue(transaction);
            }
        });
        return trans2;
    }

    public LiveData<List<Transaction>> getTransactions2(){
        repository.getTransactions().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                List<Transaction> transaction = queryDocumentSnapshots.toObjects(Transaction.class);
                Log.d("GETTRANSACTIONS", transaction.get(0).toString());
                trans.setValue(transaction);
            }
        });

        return trans;
    }



    public void saveTransaction (Transaction trans) {
        repository.saveTransaction(trans);
    }



}
