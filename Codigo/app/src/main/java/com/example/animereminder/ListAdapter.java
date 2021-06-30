package com.example.animereminder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.animereminder.controllers.AnimeController;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private List<ListElement> mData;
    private LayoutInflater mInflater;
    private Context context;

    public ListAdapter(List<ListElement> itemlist, Context context) {
        this.mData = itemlist;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_list, null);
        return new ListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListAdapter.ViewHolder holder, final int position) {
        holder.bindData(mData.get(position));
        //eventos
        holder.setOnClickListeners();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setItems(List<ListElement> items) {
        mData = items;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titulo;
        TextView description;
        String id;
        Button btnAnimeForo;
        TextView all_anime;
        ImageView edit_anime;
        ImageView delete_anime;
        Context context;
        ImageView imagen;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        ViewHolder(View itemView) {
            super(itemView);
            imagen = itemView.findViewById(R.id.img_anime);
            context = itemView.getContext();
            titulo = itemView.findViewById(R.id.titulo);
            description = itemView.findViewById(R.id.description);
            btnAnimeForo = itemView.findViewById(R.id.btnAnimeForo);
            all_anime = itemView.findViewById(R.id.all_anime);
            edit_anime = itemView.findViewById(R.id.edit_anime);
            delete_anime = itemView.findViewById(R.id.delete_anime);
        }
        void bindData(final ListElement item){
            titulo.setText(item.getTitulo());
            description.setText(item.getDescription());
            id = item.getId();
            StorageReference storageRef = storage.getReference();
            storageRef.child("anime/"+id).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(context).load(uri).into(imagen);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("Error", e.toString());
                }
            });
        }

        void setOnClickListeners() {
            btnAnimeForo.setOnClickListener(this);
            all_anime.setOnClickListener(this);
            edit_anime.setOnClickListener(this);
            delete_anime.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnAnimeForo:
                    Intent intent = new Intent(context, ForoActivity.class);
                    context.startActivity(intent);
                    break;
                case R.id.all_anime:
                    Intent intent2 = new Intent(context, InfoActivity.class);
                    Bundle a = new Bundle();
                    a.putString("id",id);
                    intent2.putExtras(a);
                    context.startActivity(intent2);
                    break;
                case R.id.edit_anime:
                    Intent intent3 = new Intent(context, EditarActivity.class);
                    Bundle b = new Bundle();
                    b.putString("id",id);
                    intent3.putExtras(b);
                    context.startActivity(intent3);
                    break;
                case R.id.delete_anime:
                    AlertDialog.Builder alerta = new AlertDialog.Builder(new ContextThemeWrapper(ListAdapter.this.context,R.style.AlertDialog));
                    alerta.setMessage("Estas seguro que deseas eliminar el anime "+titulo.getText()+" ?");
                    alerta.setCancelable(false);
                    alerta.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AnimeController.eliminarAnime(id);
                            dialog.cancel();
                        }
                    });
                    alerta.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //no quiere eliminarlo
                            dialog.cancel();
                        }
                    });
                    AlertDialog titulo = alerta.create();
                    titulo.setTitle("Eliminar Anime");
                    titulo.show();
                    break;
            }
        }
    }
}
