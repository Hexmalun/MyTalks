package com.researchfip.puc.mytalks.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.researchfip.puc.mytalks.R;
import com.researchfip.puc.mytalks.database.Cell;
import com.researchfip.puc.mytalks.database.DataBaseController;


/**
 * Created by Mateus on 12/09/2016.
 */
public class DialogDataErro extends DialogFragment {
    private DataBaseController db;
    private Context C;
    private Cell c;
    EditText et;
    View V;

    Spinner spinner, spinner2;
    private DialogInterface.OnDismissListener onDismissListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        C = getContext();
        db = new DataBaseController(C);
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        Context C = getContext();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
         V = inflater.inflate(R.layout.dialog_data, null);
        spinner2 = (Spinner)V.findViewById(R.id.spinnerD2);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(C,
                R.array.dates, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner2.setAdapter(adapter);
        spinner = (Spinner) V.findViewById(R.id.spinnerD);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(C,
                R.array.types, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter2);
        et = (EditText) V.findViewById(R.id.editTextD);
        builder.setTitle("Por favor insira um tamanho para seu plano:");
        builder.setView(V)
                // Add action buttons
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null) {
            onDismissListener.onDismiss(dialog);
        }
    }


    @Override
    public void onStart()
    {
        super.onStart();
        AlertDialog d = (AlertDialog)getDialog();
        if(d != null)
        {
            Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(et.getText().toString().length() <= 0){
                        DialogFragment newFragment = new DialogDataErro();
                        newFragment.show(getFragmentManager(), "dataPicker");
                    }else if (Integer.parseInt(et.getText().toString()) <=0){
                        DialogFragment newFragment = new DialogDataErro();
                        newFragment.show(getFragmentManager(), "dataPicker");
                    }else{
                        c = new Cell();
                        c.setType(spinner.getSelectedItem().toString());
                        c.setDay(spinner2.getSelectedItem().toString());
                        c.setData(et.getText().toString());
                        db.addPersonalData(c);
                        dismiss();
                    }

                }
            });
        }
    }
}