package com.example.animereminder;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.animereminder.controllers.AnimeController;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TempAdminFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TempAdminFragment extends Fragment {
    List<ListElement> elements;
    Button add_anime;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private AnimeController animeController;

    public TempAdminFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FirstFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TempAdminFragment newInstance(String param1, String param2) {
        TempAdminFragment fragment = new TempAdminFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_temp_admin, container, false);
        this.elements = new ArrayList<>();
        this.animeController = new AnimeController();
        String opcion = "a";
        this.animeController.listarAnime(vista, opcion);
        this.add_anime = vista.findViewById(R.id.add_anime);
        //this.init(vista);
        this.add_anime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(vista.getContext(), AgregarActivity.class);
                startActivity(intent);
            }
        });

        return vista;
    }

    public void init(View vista) {
        //elements = new ArrayList<>();
        //elements.add(new ListElement("hola","este es un ejemplo de descripción"));
        //elements.add(new ListElement("hola2","este es un ejemplo de descripción"));
        //elements.add(new ListElement("hola3", "este es un ejemplo de descripción"));
        //elements.add(new ListElement("hola4", "este es un ejemplo de descripción"));
        //elements.add(new ListElement("hola5", "este es un ejemplo de descripción"));
        //elements.add(new ListElement("hola6", "este es un ejemplo de descripción"));

        ListAdapter listAdapter = new ListAdapter(elements,vista.getContext());
        //Log.i("mensaje", "despues de adapter");
        RecyclerView recyclerView = vista.findViewById(R.id.list_anime_all);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(vista.getContext()));
        //Log.i("mensaje", "antes de set adapter");
        recyclerView.setAdapter(listAdapter);
        //Log.i("mensaje", "despues de set adapter");
    }
}