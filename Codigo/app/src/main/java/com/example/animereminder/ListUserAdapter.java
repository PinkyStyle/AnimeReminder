package com.example.animereminder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.animereminder.controllers.AnimeController;
import com.example.animereminder.controllers.UsuarioController;
import com.example.animereminder.model.Usuario;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ListUserAdapter extends RecyclerView.Adapter<ListUserAdapter.ViewHolder> {
    private List<ListElement> mData;
    private LayoutInflater mInflater;
    private Context context;



    public ListUserAdapter(List<ListElement> itemlist, Context context) {
        this.mData = itemlist;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public ListUserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_list_user, null);
        return new ListUserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListUserAdapter.ViewHolder holder, final int position) {
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
        CheckBox checkListUser;
        Context context;
        ImageView imagen;
        boolean checked;
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
            checkListUser = itemView.findViewById(R.id.checkListUser);
        }
        void bindData(final ListElement item){
            checked=item.isChecked();
            if (checked){
                checkListUser.setChecked(true);
            }
            titulo.setText(item.getTitulo());
            description.setText(item.getDescription());
            id = item.getId();
            StorageReference storageRef = storage.getReference();
            storageRef.child("anime/"+id).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(context).load(uri).into(imagen);
                }
            });
        }

        void setOnClickListeners() {
            btnAnimeForo.setOnClickListener(this);
            all_anime.setOnClickListener(this);
            checkListUser.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnAnimeForo:
                    System.out.println("foro");
                    Intent intent = new Intent(context, HomeActivity.class);
                    intent.putExtra("texto","foro");
                    context.startActivity(intent);
                    break;
                case R.id.all_anime:
                    Intent intent2 = new Intent(context, HomeActivity.class);
                    intent2.putExtra("texto","info anime");
                    context.startActivity(intent2);
                    break;
                case R.id.checkListUser:
                    if (checkListUser.isChecked()) {
                        UsuarioController.agregarAnimeMiLista(id);
                    }
                    else{
                        UsuarioController.eliminarAnimeMiLista(id);
                    }


                    break;
            }
        }
    }
}

