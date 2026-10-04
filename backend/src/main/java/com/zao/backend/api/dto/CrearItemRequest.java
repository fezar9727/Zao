package com.zao.backend.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Cuerpo de la peticion para agregar un item a un checklist. */
public record CrearItemRequest(
        @NotBlank @Size(max = 200) String descripcion,
        boolean critico) {
}
