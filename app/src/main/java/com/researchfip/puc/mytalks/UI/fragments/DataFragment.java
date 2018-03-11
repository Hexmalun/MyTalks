package com.researchfip.puc.mytalks.UI.fragments;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.researchfip.puc.mytalks.R;
import com.researchfip.puc.mytalks.UI.adapters.AppListAdapter;
import com.researchfip.puc.mytalks.UI.adapters.objects.App;

import java.util.ArrayList;
import java.util.List;

import mobi.gspd.segmentedbarview.Segment;
import mobi.gspd.segmentedbarview.SegmentedBarView;
import mobi.gspd.segmentedbarview.SegmentedBarViewSideStyle;

/**
 * Created by joaocastro on 23/10/17.
 */

public class DataFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<App> appList;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.item_data);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.data_fragment, container, false);

        List<Segment> segments = new ArrayList<>();
        Segment segment = new Segment(0, 3, "Low", Color.parseColor("#EF3D2F"));
        segments.add(segment);
        Segment segment2 = new Segment(4, 6, "Optimal", Color.parseColor("#f4f400"));
        segments.add(segment2);
        Segment segment3 = new Segment(7, 10, "High", Color.parseColor("#8CC63E"));
        segments.add(segment3);

        SegmentedBarView barView = (SegmentedBarView) v.findViewById(R.id.bar_view);
        barView.setSegments(segments);
        barView.setValue(6f);
        barView.setShowDescriptionText(true);
        barView.setSideStyle(SegmentedBarViewSideStyle.ROUNDED);



        this.appList = new ArrayList<>();
        recyclerView = (RecyclerView) v.findViewById(R.id.rv_apps);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        App app = new App ("WhatsApp", "550MB", null);
        App app2 = new App ("Facebook", "250MB", null);
        App app3 = new App ("Instagram", "300MB", null);
        App app4 = new App ("Twitter", "400MB", null);

        appList.add(app);
        appList.add(app2);
        appList.add(app3);
        appList.add(app4);

        AppListAdapter adapter = new AppListAdapter(appList);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());;

        return v;
    }
}
