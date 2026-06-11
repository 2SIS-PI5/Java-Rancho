package sptech.school.projeto_rancho.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Schema(description = "Escala de trabalho")
public class EscalaDTO {

    @Schema(description = "ID da escala", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Data da escala", example = "2025-06-20", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Data é obrigatória")
    private LocalDate dataEscala;

    @Schema(description = "Hora de início", example = "08:00", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Hora de início é obrigatória")
    private LocalTime horaInicio;

    @Schema(description = "Hora de término", example = "18:00", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Hora de término é obrigatória")
    private LocalTime horaFim;

    @Schema(description = "Status da escala", example = "Agendada",
            allowableValues = {"Agendada", "Realizada", "Cancelada"})
    private String statusEscala = "Agendada";

    @Schema(description = "Observações adicionais", example = "Evento especial de fim de semana")
    private String observacoes;

    @Schema(description = "Data/hora de criação do registro", accessMode = Schema.AccessMode.READ_ONLY)
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
