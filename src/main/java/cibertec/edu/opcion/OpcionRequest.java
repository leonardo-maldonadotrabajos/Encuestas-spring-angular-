package cibertec.edu.opcion;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class OpcionRequest {

    @NotBlank(message = "El texto de la opción es obligatorio")
    @Size(min = 1, max = 500, message = "El texto debe tener entre 1 y 500 caracteres")
    private String texto;

    @NotNull(message = "El orden es obligatorio")
    @Min(value = 0, message = "El orden debe ser mayor o igual a 0")
    private Integer orden;

    public String  getTexto()              { return texto; }
    public void    setTexto(String texto)  { this.texto = texto; }
    public Integer getOrden()              { return orden; }
    public void    setOrden(Integer orden) { this.orden = orden; }
}