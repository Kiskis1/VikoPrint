package lt.viko.eif.vikoprint.Fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import lt.viko.eif.vikoprint.MainActivity;
import lt.viko.eif.vikoprint.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyDialogFragment extends DialogFragment {

    public AlertDialog.Builder builder;

    public MyDialogFragment() {
        // Required empty public constructor
        setCancelable(false);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_dialog, container, false);
    }

    @Override public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Are you sure?"); // didelis textas
        builder.setMessage("Hey, a dialog!"); //vidinis textas



        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            @Override public void onClick(DialogInterface dialog, int which)
            {
                Toast.makeText(getContext(), R.string.test, Toast.LENGTH_LONG).show();
                dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override public void onClick(DialogInterface dialog, int which)
            {
                Toast.makeText(getContext(), R.string.cancel, Toast.LENGTH_LONG).show();
                dismiss();
            }
        });
        return builder.create();
    }

}
