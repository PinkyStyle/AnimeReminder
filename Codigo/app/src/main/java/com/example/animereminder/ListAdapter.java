package com.example.animereminder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

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
        Button btnAnimeForo;
        TextView all_anime;
        ImageView edit_anime;
        ImageView delete_anime;
        Context context;
        ViewHolder(View itemView) {
            super(itemView);
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
                    Intent intent = new Intent(context, HomeActivity.class);
                    intent.putExtra("texto","foro");
                    context.startActivity(intent);
                    break;
                case R.id.all_anime:
                    Intent intent2 = new Intent(context, HomeActivity.class);
                    intent2.putExtra("texto","info anime");
                    context.startActivity(intent2);
                    break;
                case R.id.edit_anime:
                    Intent intent3 = new Intent(context, EditarActivity.class);
                    context.startActivity(intent3);
                    break;
                case R.id.delete_anime:
                    AlertDialog.Builder alerta = new AlertDialog.Builder(new ContextThemeWrapper(ListAdapter.this.context,R.style.AlertDialog));
                    alerta.setMessage("Estas seguro que deseas eliminar el anime "+titulo.getText()+" ?");
                    alerta.setCancelable(false);
                    alerta.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //si quiere eliminarlo
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
