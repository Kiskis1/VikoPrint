package lt.viko.eif.vikoprint.Fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import lt.viko.eif.vikoprint.Model.Profile;
import lt.viko.eif.vikoprint.R;
import lt.viko.eif.vikoprint.ViewModel.ProfileViewModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {


    public AboutFragment() {
        // Required empty public constructor
    }

    private ProfileViewModel profileViewModel;
    private TextView mEmailPreview, mPointsPreview;
    private ImageView mImagePreview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button logoutBtn = view.findViewById(R.id.SignOut);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Are you sure?")
                        .setMessage("Are you sure you want to log out?")
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                FirebaseAuth.getInstance().signOut();
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                        new LoginFragment()).commit();
                            }
                        })
                        .create().show();
            }
        });

        mPointsPreview = view.findViewById(R.id.pointsPreview);
        mEmailPreview = view.findViewById(R.id.emailPreview);
        mImagePreview = view.findViewById(R.id.profileImage);

        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);

        profileViewModel.getProfile().observe(this, new Observer<Profile>() {
            @Override
            public void onChanged(Profile profile) {
                mPointsPreview.setText(""+profile.getPoints());
                mEmailPreview.setText("" + profile.getEmail());

                if (profile.getImageURL() == null) {
                    Picasso.with(getContext()).load("http://cdn.journaldev.com/wp-content/uploads/2016/11/android-image-picker-project-structure.png").into(mImagePreview);
                }
                else {
                    Picasso.with(getContext()).load(profile.getImageURL()).into(mImagePreview);
                }
            }
        });

        Button changeImage = view.findViewById(R.id.changeImageBtn);
        final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        changeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText edittext = new EditText(getContext());
                alert.setMessage("Change Image");
                alert.setTitle("Enter Image URL");

                alert.setView(edittext);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String imageURL = edittext.getText().toString();
                        Profile updateProf = profileViewModel.getProfile().getValue();
                        updateProf.setImageURL(imageURL);
                        profileViewModel.saveProfile(updateProf);
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // what ever you want to do with No option.
                    }
                });

                alert.show();
            }
        });
    }
}
