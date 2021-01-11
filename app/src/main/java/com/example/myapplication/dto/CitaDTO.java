/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.myapplication.dto;


import java.io.Serializable;
import java.util.Date;

public class CitaDTO implements Serializable {

    private static final long serialVersionUID = 6L;
    private Integer id;
    private Date fHoraCita;
    private PacienteDTO paciente;
    private MedicoDTO medico;

    public CitaDTO() {
    }

    public CitaDTO(Integer id, Date fHoraCita, PacienteDTO paciente, MedicoDTO medico) {
        this.id = id;
        this.fHoraCita = fHoraCita;
        this.paciente = paciente;
        this.medico = medico;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getfHoraCita() {
        return fHoraCita;
    }

    public void setfHoraCita(Date fHoraCita) {
        this.fHoraCita = fHoraCita;
    }

    public PacienteDTO getPaciente() {
        return paciente;
    }

    public void setPaciente(PacienteDTO paciente) {
        this.paciente = paciente;
    }

    public MedicoDTO getMedico() {
        return medico;
    }

    public void setMedico(MedicoDTO medico) {
        this.medico = medico;
    }

    @Override
    public String toString() {
        return "CitaDTO{" +
                "id=" + id +
                ", fHoraCita=" + fHoraCita +
                ", paciente=" + paciente +
                ", medico=" + medico +
                '}';
    }
}
