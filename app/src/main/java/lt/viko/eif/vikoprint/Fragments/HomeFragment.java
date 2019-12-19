package lt.viko.eif.vikoprint.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.emoji.text.EmojiCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

import lt.viko.eif.vikoprint.Adapter.TransactionAdapter;
import lt.viko.eif.vikoprint.Adapter.TransactionAdapter.Interaction;
import lt.viko.eif.vikoprint.Model.Profile;
import lt.viko.eif.vikoprint.Model.Transaction;
import lt.viko.eif.vikoprint.R;
import lt.viko.eif.vikoprint.Repositories.StorageRepository;
import lt.viko.eif.vikoprint.ViewModel.ProfileViewModel;
import lt.viko.eif.vikoprint.ViewModel.TransactionViewModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements Interaction{


    public HomeFragment() {
        // Required empty public constructor
    }
    private TextView textElement;
    private TextView emailElement;
    private ImageView imageElement;
    private Button mAdButton;

    private RewardedAd rewardedAd;

    private RecyclerView recyclerView;
    private ProfileViewModel profileViewModel;
    private TransactionAdapter transactionAdapter;
    private TransactionViewModel transactionViewModel;
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private CharSequence processed = EmojiCompat.get().process("\uD83C\uDF1E");


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textElement = view.findViewById(R.id.pointsView);
        emailElement = view.findViewById(R.id.userEmail);
        imageElement = view.findViewById(R.id.profilePicture);
        mAdButton = view.findViewById(R.id.adButton);

        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);

        profileViewModel.getProfile().observe(this, new Observer<Profile>() {
            @Override
            public void onChanged(Profile profile) {
                textElement.setText(getString(R.string.pointsValue, profile.getPoints(), processed));
                emailElement.setText("" + profile.getEmail());

                if (profile.getImageURL() == null) {
                    Picasso.with(getContext()).load(StorageRepository.getInstance().getDefaultImage()).into(imageElement);
                }
                else {
                    Picasso.with(getContext()).load(profile.getImageURL()).into(imageElement);
                }
            }
        });

        mAdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rewardedAd.isLoaded()) {
                    RewardedAdCallback adCallback = new RewardedAdCallback() {
                        @Override
                        public void onRewardedAdOpened() {
                            // Ad opened.
                        }

                        @Override
                        public void onRewardedAdClosed() {
                            // Ad closed.
                        }

                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem reward) {
                            // User earned reward.
                            profileViewModel.addPoints(5);
                            //Toast.makeText(getContext(), "USER GOT REWARD", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onRewardedAdFailedToShow(int errorCode) {
                            // Ad failed to display.

                            Toast.makeText(getContext(), "AD FAILED", Toast.LENGTH_SHORT).show();
                        }
                    };
                    rewardedAd.show(getActivity(), adCallback);
                } else {
                    Log.d("TAG", "The rewarded ad wasn't loaded yet.");
                }
            }
        });
        //configureView(view);

        transactionAdapter = new TransactionAdapter(this);

        recyclerView = view.findViewById(R.id.transaction_recycler);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);



        transactionViewModel = ViewModelProviders.of(this).get(TransactionViewModel.class);

        //transactionAdapter.submitList(transactionViewModel.getTransactions().getValue());
        recyclerView.setAdapter(transactionAdapter);
       // transactionAdapter.notifyDataSetChanged();


        transactionViewModel.getTransactions().observe(this, new Observer<List<Transaction>>() {
            @Override
            public void onChanged(List<Transaction> transactions) {
                transactionAdapter.submitList(transactions);
                //transactionAdapter.notifyDataSetChanged();
            }
        });

    }
/*
    public void populateListView(){
        transactionViewModel = ViewModelProviders.of(this).get(TransactionViewModel.class);
        transactionViewModel.init();
        transactionAdapter = new TransactionAdapter(this);

        //transactionAdapter.submitList(transactionViewModel.getItems().getValue());
        transactionAdapter.submitList(transactionViewModel.getTransactions().getValue());
        System.out.println(transactionViewModel.getTransactions().getValue());
        transactionAdapter.notifyDataSetChanged();
    }

    private void configureView(View v) {
        recyclerView = v.findViewById(R.id.transaction_recycler);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(transactionAdapter);
}
*/
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //populateListView();
        //transactionAdapter.notifyDataSetChanged();

        rewardedAd = new RewardedAd(getContext(),
                "ca-app-pub-3940256099942544/5224354917");

        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
            }

            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
                // Ad failed to load.
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

}
