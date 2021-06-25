package com.example.animereminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

public class PerfilActivity extends AppCompatActivity {
    Button btnCamaraPerfil;
    Button btnGuardarCambios;
    EditText correo_usuario_perfil;
    EditText nombre_usuario_perfil;
    ImageView foto_usuario_perfil;
    String rutaImagen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        getSupportActionBar().setTitle("Perfil Usuario");
        btnCamaraPerfil = findViewById(R.id.btnCamaraPerfil);
        btnGuardarCambios = findViewById(R.id.btnGuardarCambios);
        correo_usuario_perfil = findViewById(R.id.correo_usuario_perfil);
        nombre_usuario_perfil = findViewById(R.id.nombre_usuario_perfil);
        foto_usuario_perfil = findViewById(R.id.foto_usuario_perfil);

        this.btnCamaraPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCamara();
            }
        });

        this.btnGuardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //guardar los cambios en el perfil
            }
        });
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