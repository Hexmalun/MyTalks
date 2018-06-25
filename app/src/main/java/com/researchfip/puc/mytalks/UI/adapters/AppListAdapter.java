package com.researchfip.puc.mytalks.UI.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.researchfip.puc.mytalks.R;
import com.researchfip.puc.mytalks.UI.adapters.objects.App;

import java.util.List;

/**
 * Created by joaocastro on 01/11/17.
 */

public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.ViewHolder>{

    private List<App> appList;

    public AppListAdapter(List<App> appList){
        this.appList = appList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_item,parent,false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        App app = appList.get(position);
        holder.appName.setText(app.getName());
        holder.appUsage.setText(app.getUsage());
        holder.appLogo.setImageDrawable(app.getIcon());
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView appLogo;
        public TextView appName;
        public TextView appUsage;

        public ViewHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            appLogo = itemView.findViewById(R.id.iv_app_logo);
            appName = itemView.findViewById(R.id.tv_app_name);
            appUsage = itemView.findViewById(R.id.tv_app_quantidade);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
