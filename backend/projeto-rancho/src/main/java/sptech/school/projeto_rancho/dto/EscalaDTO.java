package sptech.school.projeto_rancho.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class EscalaDTO {

    private Long id;

    @NotNull(message = "Data é obrigatória")
    private LocalDate dataEscala;

    @NotNull(message = "Hora de início é obrigatória")
    private LocalTime horaInicio;

    @NotNull(message = "Hora de término é obrigatória")
    private LocalTime horaFim;

    private String statusEscala = "Agendada";

    private String observacoes;

    private LocalDateTime criadoEm;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDataEscala() { return dataEscala; }
    public void setDataEscala(LocalDate dataEscala) { this.dataEscala = dataEscala; }

    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }

    public LocalTime getHoraFim() { return horaFim; }
    public void setHoraFim(LocalTime horaFim) { this.horaFim = horaFim; }

    public String getStatusEscala() { return statusEscala; }
    public void setStatusEscala(String statusEscala) { this.statusEscala = statusEscala; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
}
