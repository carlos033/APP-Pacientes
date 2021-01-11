/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.myapplication.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MedicoDTO implements Serializable {

    private static final long serialVersionUID = 9L;
    private String nLicencia;
    private String nombre;
    private String especialidad;
    private int consulta;
    private HospitalDTO hospital;
    private List<CitaDTO> listaCitas;
    private List<InformeDTO> listaInformes;

    public MedicoDTO() {
        this.listaInformes = new ArrayList<>();
        this.listaCitas = new ArrayList<>();
    }

    public MedicoDTO(String nLicencia, String nombre, String especialidad, int consulta, HospitalDTO hospital, List<CitaDTO> citas, List<InformeDTO> listaInformes) {
        this.nLicencia = nLicencia;
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.consulta = consulta;
        this.hospital = hospital;
        this.listaCitas = citas;
        this.listaInformes = listaInformes;
    }

    public String getnLicencia() {
        return nLicencia;
    }

    public void setnLicencia(String nLicencia) {
        this.nLicencia = nLicencia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public int getConsulta() {
        return consulta;
    }

    public void setConsulta(int consulta) {
        this.consulta = consulta;
    }

    public HospitalDTO getHospital() {
        return hospital;
    }

    public void setHospital(HospitalDTO hospital) {
        this.hospital = hospital;
    }

    public List<CitaDTO> getCitas() {
        return listaCitas;
    }

    public void setCitas(List<CitaDTO> citas) {
        this.listaCitas = citas;
    }

    public List<InformeDTO> getListaInformes() {
        return listaInformes;
    }

    public void setListaInformes(List<InformeDTO> listaInformes) {
        this.listaInformes = listaInformes;
    }

    @Override
    public String toString() {
        return "MedicoDTO{" +
                "nLicencia=" + nLicencia +
                ", nombre='" + nombre + '\'' +
                ", especialidad='" + especialidad + '\'' +
                ", consulta=" + consulta +
                ", hospital=" + hospital +
                ", listaCitas=" + listaCitas +
                ", listaInformes=" + listaInformes +
                '}';
    }
}
