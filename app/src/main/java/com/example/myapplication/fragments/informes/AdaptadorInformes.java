package com.example.myapplication.fragments.informes;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.dto.InformeDTO;

import java.util.List;

public class AdaptadorInformes extends RecyclerView.Adapter {
    Context contexto;
    private List<InformeDTO> datos;
    private int posicion;

    public AdaptadorInformes(List<InformeDTO> datos, Context contexto) {
        this.datos = datos;
        this.contexto = contexto;
    }

    public List<InformeDTO> getDatos() {
        return datos;
    }

    public void setDatos(List<InformeDTO> datos) {
        this.datos = datos;
        notifyDataSetChanged();
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_informes, parent, false);
        InformeViewHolder informe = new InformeViewHolder(layout);
        return informe;
    }

    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ((InformeViewHolder) holder).bindInforme(datos.get(position));
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosicion(holder.getAdapterPosition());
                return false;
            }
        });
    }


    @Override
    public int getItemCount() {
        return datos.size();
    }

    private class InformeViewHolder extends RecyclerView.ViewHolder
            implements View.OnCreateContextMenuListener {

        private TextView tvTitulo;
        private TextView tvUrl;

        public InformeViewHolder(View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvUrl = itemView.findViewById(R.id.tvUrl);
            itemView.setOnCreateContextMenuListener(this);
        }

        public void bindInforme(InformeDTO i) {
            tvTitulo.setText("Nombre: " + i.getNombreInf());
            tvUrl.setText("Url: " + i.getUrl());
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuInflater inflater = new MenuInflater(contexto);
            inflater.inflate(R.menu.mimenui, menu);
        }
    }
}
