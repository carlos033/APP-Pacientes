package com.example.myapplication.api;

import com.example.myapplication.dto.CitaDTO;
import com.example.myapplication.dto.InformeDTO;
import com.example.myapplication.dto.MedicoDTO;
import com.example.myapplication.dto.jwt.JwtRequestDTO;
import com.example.myapplication.dto.jwt.JwtResponseDTO;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface MiApi {

    @POST("/servidor/autenticacion/login")
    Call<JwtResponseDTO> login(@Body JwtRequestDTO request);

    @GET("/servidor/pacientes/{nSS}/citas")
    Call<List<CitaDTO>> buscarXPaciente(@Path("nSS") String nSS);

    @POST("/servidor/citas")
    Call<CitaDTO> aniadirCita(@Body CitaDTO citaDTO);

    @DELETE("/servidor/citas/{id}")
    Call<Void> eliminarCita(@Path("id") int id);

    @GET("/servidor/pacientes/{nSS}/informes")
    Call<List<InformeDTO>> buscarInformesXPaciente(@Path("nSS") String nSS);

    @GET("/servidor/citas/{nSS}/buscarMMedico")
    Call<MedicoDTO> buscarMiMedico(@Path("nSS") String nSS);

}
