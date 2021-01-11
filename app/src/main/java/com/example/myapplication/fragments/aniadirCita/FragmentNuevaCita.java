package com.example.myapplication.fragments.aniadirCita;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.R;
import com.example.myapplication.api.MiApi;
import com.example.myapplication.api.NetworkClient;
import com.example.myapplication.constantes.Constantes;
import com.example.myapplication.dto.CitaDTO;
import com.example.myapplication.dto.MedicoDTO;
import com.example.myapplication.dto.PacienteDTO;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import androidx.fragment.app.Fragment;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;

public class FragmentNuevaCita extends Fragment {

    private Context contexto;
    private SharedPreferences sharedPreferences;
    private EditText etFechaAniadir;
    private EditText hora;
    private Button btnPedirCita;
    private String nSS;
    private String nLicencia;
    private Calendar calendario = Calendar.getInstance();
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private RelativeLayout llContenedorControles;
    private LinearLayout llContenedorNoCitas;
    private Gson gson = new Gson();

    public static Date getDate(int anioC, int mesC, int diaC, int horaC, int minutoC, int segundoC) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, anioC);
        cal.set(Calendar.MONTH, mesC);
        cal.set(Calendar.DAY_OF_MONTH, diaC);
        cal.set(Calendar.HOUR_OF_DAY, horaC);
        cal.set(Calendar.MINUTE, minutoC);
        cal.set(Calendar.SECOND, segundoC);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        contexto = this.getActivity();
        sharedPreferences = getActivity().getSharedPreferences(Constantes.PREFERENCE_NAME, MODE_PRIVATE);
        nSS = sharedPreferences.getString(Constantes.PREFERENCES_KEY_ID_USUARIO, null);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(contexto);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        View root = inflater.inflate(R.layout.fragment_aniadir_cita, container, false);
        DatePickerDialog.OnDateSetListener listenerDatePickerDialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String fechaStr = dayOfMonth + "/" + (month + 1) + "/" + year;
                etFechaAniadir.setText(fechaStr);
            }
        };

        datePickerDialog = new DatePickerDialog(contexto, listenerDatePickerDialog,
                calendario.get(Calendar.YEAR), calendario.get(Calendar.MONTH), calendario.get(Calendar.DAY_OF_MONTH));
        TimePickerDialog.OnTimeSetListener listenerTimePickerDialog = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String horaStr = "";
                if (hourOfDay < 10) {
                    horaStr = horaStr + "0";
                }

                horaStr = horaStr + hourOfDay;
                horaStr = horaStr + ":";

                if (minute < 10) {
                    horaStr = horaStr + "0";
                }

                horaStr = horaStr + minute;

                hora.setText(horaStr);
            }
        };

        timePickerDialog = new TimePickerDialog(contexto, listenerTimePickerDialog, 8, 00, true);
        etFechaAniadir = (EditText) root.findViewById(R.id.etFechaAniadir);
        etFechaAniadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        hora = (EditText) root.findViewById(R.id.etHora);
        hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.show();
            }
        });

        btnPedirCita = (Button) root.findViewById(R.id.btnPedirCita);
        btnPedirCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aniadirCita();
            }
        });
        llContenedorNoCitas = root.findViewById(R.id.linearLayoutContenedorMensajeNoTieneCitas);
        llContenedorControles = root.findViewById(R.id.linearLayoutContenedorControles);

        buscarMedico(nSS);
        return root;
    }


    @Override
    public void onResume() {
        super.onResume();
        ocultarOMostrarSegunRecuperarCitas();
    }

    private boolean ocultarOMostrarSegunRecuperarCitas() {

        String token = sharedPreferences.getString(Constantes.PREFERENCES_KEY_TOKEN, null);
        Retrofit retrofit = NetworkClient.getRetrofitClientWithToken(token);
        MiApi myAPI = retrofit.create(MiApi.class);
        Call<List<CitaDTO>> call = myAPI.buscarXPaciente(nSS);

        call.enqueue(new Callback<List<CitaDTO>>() {
            @Override
            public void onResponse(Call<List<CitaDTO>> call, Response<List<CitaDTO>> response) {
                if (response.body() != null) {
                    List<CitaDTO> citasAux = response.body();
                    boolean tienesCitas = citasAux.size() > 0;
                    if (tienesCitas) {
                        llContenedorControles.setVisibility(View.VISIBLE);
                        llContenedorNoCitas.setVisibility(View.GONE);
                    } else {
                        llContenedorNoCitas.setVisibility(View.VISIBLE);
                        llContenedorControles.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(contexto, "Se ha producido un error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<CitaDTO>> call, Throwable t) {
                Log.e("PrimerFragment", "Se ha producido un error:" + t.getMessage());
            }
        });

        return false;
    }

    private void buscarMedico(String nSS) {
        String token = sharedPreferences.getString(Constantes.PREFERENCES_KEY_TOKEN, null);
        Retrofit retrofit = NetworkClient.getRetrofitClientWithToken(token);
        MiApi myAPI = retrofit.create(MiApi.class);
        Call<MedicoDTO> call = myAPI.buscarMiMedico(nSS);
        call.enqueue(new Callback<MedicoDTO>() {
            @Override
            public void onResponse(Call<MedicoDTO> call, Response<MedicoDTO> response) {
                if (response.isSuccessful()) {
                    nLicencia = response.body().getnLicencia();
                }
            }

            @Override
            public void onFailure(Call<MedicoDTO> call, Throwable t) {
            }
        });
    }

    public void aniadirCita() {
        String fechaStr = etFechaAniadir.getText().toString();
        String horaStr = hora.getText().toString();
        if (fechaStr == null || fechaStr.trim().isEmpty() || horaStr == null || horaStr.trim().isEmpty()) {
            if (fechaStr == null || fechaStr.trim().isEmpty()) {
                Toast.makeText(contexto, "La fecha es vacia. Rellenela", Toast.LENGTH_LONG).show();
            }
            if (horaStr == null || horaStr.trim().isEmpty()) {
                Toast.makeText(contexto, "La hora es vacia. Rellenela", Toast.LENGTH_LONG).show();
            }

        } else {
            String token = sharedPreferences.getString(Constantes.PREFERENCES_KEY_TOKEN, null);
            Retrofit retrofit = NetworkClient.getRetrofitClientWithToken(token);
            MiApi myAPI = retrofit.create(MiApi.class);
            CitaDTO dto = new CitaDTO();
            Date miFecha = new Date();
            String[] fechaSplitted = fechaStr.split("/");
            int dia = Integer.parseInt(fechaSplitted[0]);
            int mes = Integer.parseInt(fechaSplitted[1]) - 1;
            int anio = Integer.parseInt(fechaSplitted[2]);
            String[] horaSplitted = horaStr.split(":");
            int h = Integer.parseInt(horaSplitted[0]);
            int m = Integer.parseInt(horaSplitted[1]);

            List<Integer> horasValidas = Arrays.asList(new Integer[]{8,9,10,11,12,13,14,15});
            List<Integer> minutosValidos = Arrays.asList(new Integer[]{0,15,30,45});

            if (horasValidas.contains(h) && minutosValidos.contains(m)) {
                miFecha = getDate(anio, mes, dia, h, m, 00);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy HH:mm:ss");
                dto.setfHoraCita(miFecha);
                PacienteDTO pacienteDTO = new PacienteDTO();
                pacienteDTO.setnSS(nSS);
                MedicoDTO medicoDTO = new MedicoDTO();
                medicoDTO.setnLicencia(nLicencia);
                dto.setPaciente(pacienteDTO);
                dto.setMedico(medicoDTO);
                Call<CitaDTO> call = myAPI.aniadirCita(dto);
                call.enqueue(new Callback<CitaDTO>() {
                    @Override
                    public void onResponse(Call<CitaDTO> call, Response<CitaDTO> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(contexto, "Datos añadidos correctamente", Toast.LENGTH_LONG).show();
                        } else {
                            ResponseBody responseBody = response.errorBody();
                            Map map = null;
                            try {
                                map = gson.fromJson(responseBody.string(), Map.class);
                                String mensaje = (String) map.get("message");
                                Toast.makeText(contexto, mensaje, Toast.LENGTH_LONG).show();
                            } catch (IOException e) {
                                Toast.makeText(contexto, "Se ha producido un error " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CitaDTO> call, Throwable t) {
                        Toast.makeText(contexto, "Se ha producido un error:" + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
            else {
                Toast.makeText(contexto,
                        "Esa hora no esta soportada. Horas soportadas :" +
                                Arrays.toString(horasValidas.toArray()) + " Minutos validos: " +
                                Arrays.toString(minutosValidos.toArray()),
                        Toast.LENGTH_LONG).show();
            }

        }
    }
}