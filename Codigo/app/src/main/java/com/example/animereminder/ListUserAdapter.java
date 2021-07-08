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
import android.os.Vibrator;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.animereminder.controllers.AnimeController;
import com.example.animereminder.controllers.UsuarioController;
import com.example.animereminder.model.Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class ListUserAdapter extends RecyclerView.Adapter<ListUserAdapter.ViewHolder> {
    private List<ListElement> mData;
    private LayoutInflater mInflater;
    private Context context;

    private UsuarioController usuarioController = new UsuarioController();



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
            DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
            connectedRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean connected = snapshot.getValue(Boolean.class);
                    if (connected) {
                        long ONE_MEGABYTE = 1024 * 1024;
                        StorageReference storageRef = storage.getReference();
                        storageRef.child("anime/"+id).getBytes(ONE_MEGABYTE)
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
                    //System.out.println("foro");
                    Intent intent = new Intent(context, ForoActivity.class);
                    Bundle b = new Bundle();
                    b.putString("id",id);
                    intent.putExtras(b);
                    context.startActivity(intent);
                    break;
                case R.id.all_anime:
                    Intent intent2 = new Intent(context, InfoActivity.class);
                    Bundle a = new Bundle();
                    a.putString("id",id);
                    intent2.putExtras(a);
                    context.startActivity(intent2);
                    break;
                case R.id.checkListUser:
                    if (checkListUser.isChecked()) {
                        usuarioController.agregarAnimeMiLista(id);
                    }
                    else{
                        Vibrator vi = (Vibrator) ListUserAdapter.this.context.getSystemService(Context.VIBRATOR_SERVICE);
                        vi.vibrate(400);
                        UsuarioController.eliminarAnimeMiLista(id);
                    }


                    break;
            }
        }
    }
}

