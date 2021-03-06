package com.researchfip.puc.mytalks.UI.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.researchfip.puc.mytalks.Dialogs.DialogData;
import com.researchfip.puc.mytalks.R;

/**
 * Created by joaocastro on 23/10/17.
 */

public class PreferencesFragment extends Fragment {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.item_preferences);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_fragment, container, false);
        Button button = (Button) view.findViewById(R.id.upPersonal);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DialogData newFragment = new DialogData();
                newFragment.show(getFragmentManager(), "dataPicker");
            }
        });
        return view;
    }

}
