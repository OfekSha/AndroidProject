package com.example.androidproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class TableDialog extends DialogFragment {
    private String title,message,positive, negative;
    private IRespondDialog mainActivity;
    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    static TableDialog goodOrderDialog(IRespondDialog mainActivity) {
        TableDialog f = new TableDialog();
        f.mainActivity=mainActivity;
        f.title="Order Accept";
        f.message="Are you Sure";
        f.positive="Yes";
        f.negative="No";
        return f;
    }
    static TableDialog errorOrderDialog(IRespondDialog mainActivity, String error) {
        TableDialog f = new TableDialog();
        f.mainActivity=mainActivity;
        f.title="Order Error";
        f.message=error;
        f.positive=null;
        f.negative="OK";
        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (positive!=null)
        return new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setTitle(title)
                .setPositiveButton(positive,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                mainActivity.responseYES();
                            }
                        }
                )
                .setNegativeButton(negative,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                mainActivity.responseNOT();
                            }
                        }
                )
                .create();
        else{
            return new AlertDialog.Builder(getActivity())
                    .setMessage(message)
                    .setTitle(title)
                    .setNegativeButton(negative,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    mainActivity.responseNOT();
                                }
                            }
                    )
                    .create();
        }
    }
}
