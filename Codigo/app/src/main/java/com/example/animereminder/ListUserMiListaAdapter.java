package com.example.animereminder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import androidx.core.content.FileProvider;
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
import android.content.ContextWrapper;



import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.animereminder.controllers.AnimeController;
import com.example.animereminder.controllers.UsuarioController;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import android.os.Vibrator;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


public class ListUserMiListaAdapter extends RecyclerView.Adapter<ListUserMiListaAdapter.ViewHolder> {
    private List<ListElement> mData;
    private LayoutInflater mInflater;
    private Context context;

    public ListUserMiListaAdapter(List<ListElement> itemlist, Context context) {
        this.mData = itemlist;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public ListUserMiListaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_list_user, null);
        return new ListUserMiListaAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListUserMiListaAdapter.ViewHolder holder, final int position) {
        try {
            holder.bindData(mData.get(position));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            titulo = itemView.findViewById(R.id.titulo);
            description = itemView.findViewById(R.id.description);
            btnAnimeForo = itemView.findViewById(R.id.btnAnimeForo);
            all_anime = itemView.findViewById(R.id.all_anime);
            checkListUser = itemView.findViewById(R.id.checkListUser);
            imagen = itemView.findViewById(R.id.img_anime);
        }
        void bindData(final ListElement item) throws IOException {
            titulo.setText(item.getTitulo());
            description.setText(item.getDescription());
            id = item.getId();
            checkListUser.setChecked(true);

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
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }

                public void onFailure(@NonNull Exception exception) {
                    // File not found
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
                    UsuarioController.eliminarAnimeMiLista(id);
                    Vibrator vi = (Vibrator) ListUserMiListaAdapter.this.context.getSystemService(Context.VIBRATOR_SERVICE);
                    vi.vibrate(400);
                    break;
            }
        }
    }

}
