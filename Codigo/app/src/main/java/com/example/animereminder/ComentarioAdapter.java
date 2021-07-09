package com.example.animereminder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import com.example.animereminder.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import android.os.Vibrator;

import org.jetbrains.annotations.NotNull;

public class ComentarioAdapter extends RecyclerView.Adapter<ComentarioAdapter.ViewHolder> {
    private List<ComentarioElement> mData;
    private LayoutInflater mInflater;
    private Context context;

    public ComentarioAdapter(List<ComentarioElement> mData, Context context) {
        this.mData = mData;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }


    @Override
    public ComentarioAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_comentario, null);
        return new ComentarioAdapter.ViewHolder(view);

    }
    @Override
    public void onBindViewHolder(final ComentarioAdapter.ViewHolder holder, final int position) {
        holder.bindData(mData.get(position));
        //eventos
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setItems(List<ComentarioElement> items) {
        mData = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre;
        TextView texto;
        Context context;
        ImageView imagen;
        String id;
        //FirebaseStorage storage = FirebaseStorage.getInstance();
        //StorageReference storageRef = storage.getReference();

        ViewHolder(View itemView) {
            super(itemView);
            //context = itemView.getContext();
            nombre = itemView.findViewById(R.id.nombre_comentario);
            texto = itemView.findViewById(R.id.texto_comentario);
        }

        void bindData(final ComentarioElement item){
            nombre.setText(item.getNombre());
            texto.setText(item.getTexto());



            //DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
            /*connectedRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean connected = snapshot.getValue(Boolean.class);
                    if (connected) {
                        long ONE_MEGABYTE = 1024 * 1024;
                        StorageReference storageRef = storage.getReference();
                        storageRef.child("Usuario/"+id).getBytes(ONE_MEGABYTE)
                                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        BitmapFactory.Options options = new BitmapFactory.Options();
                                        options.inScaled = false;
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                                        imagen.setImageBitmap(bitmap);
                                        ContextWrapper cw = new ContextWrapper(context);
                                        File dirImages = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                                        File myPath = new File(dirImages, id + ".jpg");
                                        FileOutputStream fos = null;
                                        try {
                                            fos = new FileOutputStream(myPath);
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                                            fos.flush();
                                        } catch (FileNotFoundException ex) {
                                            ex.printStackTrace();
                                        } catch (IOException ex) {
                                            ex.printStackTrace();
                                        }

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                    } else {

                        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), id + ".jpg");
                        if(file.exists()) {
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inScaled = false;
                            Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                            if (bmp != null) {
                                imagen.setImageBitmap(bmp);
                                bmp = null;
                            }
                            file = null;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });*/
        }

    }
}
