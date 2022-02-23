package client.application.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import client.application.R;

public class SaveRecordingDialogFragment extends DialogFragment {
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //Builder class implementation
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_save_recording)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){

                    }
                })
                .setNegativeButton(R.string.no_thanks, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        //Create AlertDialog object and return
        return builder.create();
    }
}
