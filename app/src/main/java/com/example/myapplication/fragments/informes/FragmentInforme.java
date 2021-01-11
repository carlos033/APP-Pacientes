package com.example.myapplication.fragments.informes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
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
import com.example.myapplication.dto.InformeDTO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;

public class FragmentInforme extends Fragment {
    private List<InformeDTO> datos = new ArrayList<>();
    private RecyclerView rv1;
    private String nSS;
    private AdaptadorInformes adaptador;
    private Context contexto;
    private String url;
    private SharedPreferences sharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        contexto = this.getActivity();

        sharedPreferences = getActivity().getSharedPreferences(Constantes.PREFERENCE_NAME, MODE_PRIVATE);
        nSS = sharedPreferences.getString(Constantes.PREFERENCES_KEY_ID_USUARIO, null);
        View root = inflater.inflate(R.layout.fragment_informes, container, false);
        rv1 = root.findViewById(R.id.rv1);
        rv1.setHasFixedSize(true);
        adaptador = new AdaptadorInformes(datos, contexto);
        rv1.setAdapter(adaptador);
        rv1.setLayoutManager(new LinearLayoutManager(contexto, LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv1.getContext(),
                ((LinearLayoutManager) rv1.getLayoutManager()).getOrientation());
        rv1.addItemDecoration(dividerItemDecoration);
        registerForContextMenu(rv1);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarDatos(nSS);
    }

    private void cargarDatos(String nSS) {
        String token = sharedPreferences.getString(Constantes.PREFERENCES_KEY_TOKEN, null);
        Retrofit retrofit = NetworkClient.getRetrofitClientWithToken(token);
        MiApi myAPI = retrofit.create(MiApi.class);
        Call<List<InformeDTO>> call = myAPI.buscarInformesXPaciente(nSS);

        call.enqueue(new Callback<List<InformeDTO>>() {
            @Override
            public void onResponse(Call<List<InformeDTO>> call, Response<List<InformeDTO>> response) {
                if (response.body() != null) {
                    List<InformeDTO> informeAux = response.body();
                    datos.clear();
                    datos.addAll(informeAux);
                    adaptador.setDatos(datos);
                } else {
                    Toast.makeText(contexto, "Se ha producido un error", Toast.LENGTH_LONG).show();

                }
            }
            @Override
            public void onFailure(Call<List<InformeDTO>> call, Throwable t) {
                Toast.makeText(contexto, "Se ha producido un error", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int posicion = -1;
        try {
            posicion = adaptador.getPosicion();
            url = datos.get(posicion).getUrl();
        } catch (Exception e) {
            return super.onContextItemSelected(item);
        }
        switch (item.getItemId()) {
            case R.id.mi1:
                abrir(url);
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void abrir(String url) {
        if (URLUtil.isValidUrl(String.valueOf(url))) {
            Uri uri = Uri.parse(String.valueOf(url));
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            contexto.startActivity(intent);
        } else {
            Toast.makeText(contexto, "La url del informe no es valida", Toast.LENGTH_LONG).show();
        }
    }
}