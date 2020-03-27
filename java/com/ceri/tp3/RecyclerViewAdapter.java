package com.ceri.tp3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    private Context context;
    private List<Team> equipes;


    public RecyclerViewAdapter(Context context, List<Team> equipes) {
        this.context = context;
        this.equipes = equipes;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");

        final Team equipe = this.equipes.get(position);
        final String nomEquipe = equipe.getName();
        String dernierMatch = equipe.getLastEvent().toString();

//        ((ViewHolder) holder).image.setImageResource(context.getResources().getIdentifier(equipe.getTeamBadge()+"_icon","drawable",context.getPackageName()));
        ((ViewHolder) holder).nomEquipe.setText(nomEquipe);
        ((ViewHolder) holder).dernierMatch.setText(dernierMatch);

        ((ViewHolder) holder).parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on : "+nomEquipe);
//                Toast.makeText(context, labels[position],Toast.LENGTH_LONG).show();

                Intent intent = new Intent(v.getContext(),TeamActivity.class);
                intent.putExtra(Team.TAG, equipe);
                ((Activity)v.getContext()).startActivityForResult(intent, MainActivity.UPDATE_TEAM_REQUEST);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.equipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView nomEquipe;
        TextView dernierMatch;
        LinearLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            nomEquipe = itemView.findViewById(R.id.nomEquipe);
            dernierMatch = itemView.findViewById(R.id.dernierMatch);
            parentLayout = itemView.findViewById(R.id.parentLayout);
        }
    }
}
