package com.example.myapplication.fragments.citas;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.dto.CitaDTO;

import java.text.SimpleDateFormat;
import java.util.List;

public class AdaptadorDeCitas extends RecyclerView.Adapter {
    Context contexto;
    private List<CitaDTO> datos;
    private int posicion;

    public AdaptadorDeCitas(List<CitaDTO> datos, Context contexto) {
        this.datos = datos;
        this.contexto = contexto;
    }

    public List<CitaDTO> getDatos() {
        return datos;
    }

    public void setDatos(List<CitaDTO> datos) {
        this.datos = datos;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(contexto)
                .inflate(R.layout.recycler_view_citas, parent, false);
        CitaViewHolder citas = new CitaViewHolder(layout);
        return citas;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ((CitaViewHolder) holder).bindCitas(datos.get(position));
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

    private class CitaViewHolder extends RecyclerView.ViewHolder
            implements View.OnCreateContextMenuListener {

        private TextView tvDoctor;
        private TextView tvFecha;
        private TextView tvConsulta;
        private TextView tvId;

        public CitaViewHolder(View itemView) {
            super(itemView);
            tvDoctor = itemView.findViewById(R.id.tvDoctor);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvConsulta = itemView.findViewById(R.id.tvConsulta);
            tvId = itemView.findViewById(R.id.tvId);
            itemView.setOnCreateContextMenuListener(this);
        }

        public void bindCitas(CitaDTO c) {
            tvDoctor.setText("Doctor: " + c.getMedico().getNombre());
            SimpleDateFormat simpleformat = new SimpleDateFormat("dd/MMMM/yyyy HH:mm");
            tvFecha.setText(simpleformat.format(c.getfHoraCita()));
            tvConsulta.setText("Consulta: " + c.getMedico().getConsulta());
            String id = String.valueOf(c.getId());
            tvId.setText("Cita nº: " + id);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuInflater inflater = new MenuInflater(contexto);
            inflater.inflate(R.menu.mimenuc, menu);
        }
    }
}