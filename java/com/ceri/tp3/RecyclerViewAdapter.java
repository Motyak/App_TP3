package com.ceri.tp3;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
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

//        badge
        String dirPath = this.context.getExternalFilesDir(null).toString();
        File imageFile = new File(dirPath, equipe.getId() + ".png");
        if(imageFile.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            Bitmap img = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
            ((ViewHolder) holder).image.setImageBitmap(img);
        }


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
