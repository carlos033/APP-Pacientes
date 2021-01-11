package com.example.myapplication.fragments.citas;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.api.MiApi;
import com.example.myapplication.api.NetworkClient;
import com.example.myapplication.constantes.Constantes;
import com.example.myapplication.dto.CitaDTO;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;

public class FragmentCitas extends Fragment {
    private Context contexto;
    private RecyclerView rv;
    private AdaptadorDeCitas adaptador;
    private List<CitaDTO> datos = new ArrayList<>();
    private int id;
    private String nSS;
    private FloatingActionButton extendedFAB;
    private SharedPreferences sharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        contexto = this.getActivity();

        sharedPreferences = getActivity().getSharedPreferences(Constantes.PREFERENCE_NAME, MODE_PRIVATE);
        nSS = sharedPreferences.getString(Constantes.PREFERENCES_KEY_ID_USUARIO, null);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(contexto);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        View root = inflater.inflate(R.layout.fragment_citas, container, false);
        rv = root.findViewById(R.id.rv);
        rv.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv.getContext(),
                ((LinearLayoutManager) rv.getLayoutManager()).getOrientation());
        rv.addItemDecoration(dividerItemDecoration);
        adaptador = new AdaptadorDeCitas(datos, contexto);
        rv.setAdapter(adaptador);
        registerForContextMenu(rv);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarDatos(nSS);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int posicion = -1;
        try {
            posicion = adaptador.getPosicion();
            id = datos.get(posicion).getId();
        } catch (Exception e) {
            return super.onContextItemSelected(item);
        }
        switch (item.getItemId()) {
            case R.id.mi1:
                eliminar(id);
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void eliminar(int id) {
        String token = sharedPreferences.getString(Constantes.PREFERENCES_KEY_TOKEN, null);

        Retrofit retrofit = NetworkClient.getRetrofitClientWithToken(token);
        MiApi myAPI = retrofit.create(MiApi.class);
        Call<Void> call = myAPI.eliminarCita(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 204) {
                    Toast.makeText(getContext(), "Cita eliminada correctamente", Toast.LENGTH_LONG).show();
                    cargarDatos(nSS);
                } else {
                    Toast.makeText(getContext(), response.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Se ha producido un error:" + t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }

    private void cargarDatos(String nSS) {
        String token = sharedPreferences.getString(Constantes.PREFERENCES_KEY_TOKEN, null);
        Retrofit retrofit = NetworkClient.getRetrofitClientWithToken(token);
        MiApi myAPI = retrofit.create(MiApi.class);
        Call<List<CitaDTO>> call = myAPI.buscarXPaciente(nSS);

        call.enqueue(new Callback<List<CitaDTO>>() {
            @Override
            public void onResponse(Call<List<CitaDTO>> call, Response<List<CitaDTO>> response) {
                if (response.body() != null) {
                    List<CitaDTO> citasAux = response.body();
                    datos.clear();
                    datos.addAll(citasAux);
                    adaptador.setDatos(datos);
                    adaptador.notifyDataSetChanged();

                } else {
                    Toast.makeText(contexto, "Se ha producido un error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<CitaDTO>> call, Throwable t) {
                Toast.makeText(contexto, "Se ha producido un error:", Toast.LENGTH_LONG).show();
                }
    });
    }
}
