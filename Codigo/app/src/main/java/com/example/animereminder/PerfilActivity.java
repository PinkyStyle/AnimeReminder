package com.example.animereminder;

import androidx.annotation.NonNull;
import org.jetbrains.annotations.NotNull;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.animereminder.controllers.AnimeController;
import com.example.animereminder.controllers.UsuarioController;
import com.example.animereminder.model.Anime;
import com.example.animereminder.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class PerfilActivity extends AppCompatActivity {
    Button btnCamaraPerfil;
    Button btnGuardarCambios;
    EditText correo_usuario_perfil;
    EditText nombre_usuario_perfil;
    ImageView foto_usuario_perfil;
    String rutaImagen;
    String contraseña;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Perfil Usuario");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnCamaraPerfil = findViewById(R.id.btnCamaraPerfil);
        btnGuardarCambios = findViewById(R.id.btnGuardarCambios);
        correo_usuario_perfil = findViewById(R.id.correo_usuario_perfil);
        nombre_usuario_perfil = findViewById(R.id.nombre_usuario_perfil);
        foto_usuario_perfil = findViewById(R.id.foto_usuario_perfil);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference.child("Usuario").child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){

                    Usuario usuario = task.getResult().getValue(Usuario.class);
                    nombre_usuario_perfil.setText(usuario.getNickname());
                    correo_usuario_perfil.setText(usuario.getCorreo());
                    StorageReference storageRef = storage.getReference();
                    storageRef.child("usuario/" + user.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(PerfilActivity.this).load(uri).into(foto_usuario_perfil);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Error", e.toString());
                        }
                    });

                }
                else{
                    System.out.println("No se obtuvieron los datos");
                }
            }
        });

        this.btnCamaraPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCamara();
            }
        });

        this.btnGuardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(PerfilActivity.this);
                builder.setTitle("Ingrese contraseña");

                final EditText input = new EditText(PerfilActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(input);

                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        contraseña = input.getText().toString();
                        modificarUsuario(contraseña);
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });

                builder.show();


            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    private void modificarUsuario(String contraseña){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String nombre = nombre_usuario_perfil.getText().toString();
        String correo = correo_usuario_perfil.getText().toString();

        int cantErrores = 0;

        if (nombre.isEmpty()) {
            nombre_usuario_perfil.setError("Debe ingresar un título para el anime");
            cantErrores++;
        }
        if (correo.isEmpty()) {
            correo_usuario_perfil.setError("Debe ingresar una descripción para el anime");
            cantErrores++;
        }
        if(cantErrores >0){
            return;
        }

        try {
            UsuarioController.modificarUsuario(user.getUid(), correo, nombre, contraseña);
            StorageReference refImage = storageRef.child("usuario/"+user.getUid());
            foto_usuario_perfil.setDrawingCacheEnabled(true);
            foto_usuario_perfil.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) foto_usuario_perfil.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = refImage.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                }
            });

            finish();
        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(PerfilActivity.this,"Error al modificar el perfil",Toast.LENGTH_SHORT).show();
        }
    }

    private void abrirCamara(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if ( intent.resolveActivity(getPackageManager()) != null) {
            File imagenArchivo = null;
            try {
                imagenArchivo = crearImagen();
            } catch (IOException ex){
                Log.e("Error", ex.toString());
            }
            if (imagenArchivo != null) {
                Uri fotoUri = FileProvider.getUriForFile(this,"com.example.animereminder.fileprovider",imagenArchivo);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fotoUri);
                startActivityForResult(intent,1);
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            //Bundle extras = data.getExtras();
            Bitmap foto = BitmapFactory.decodeFile(rutaImagen);
            foto_usuario_perfil.setImageBitmap(foto);
        }
    }

    private File crearImagen() throws IOException {
        String nombreImagen = "foto_perfil_";
        File directorio = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imagen = File.createTempFile(nombreImagen,".jpg", directorio);
        rutaImagen = imagen.getAbsolutePath();
        return imagen;
    }
}