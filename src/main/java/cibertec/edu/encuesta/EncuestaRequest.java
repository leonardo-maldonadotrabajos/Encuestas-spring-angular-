package cibertec.edu.encuesta;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public class EncuestaRequest {

    @NotBlank(message = "El título es obligatorio")
    @Size(min = 5, max = 255, message = "El título debe tener entre 5 y 255 caracteres")
    private String titulo;

    @Size(max = 1000, message = "La descripción no puede superar 1000 caracteres")
    private String descripcion;

    private List<@NotBlank(message = "El texto de la opción es obligatorio") String> opciones;

    public String getTitulo()                          { return titulo; }
    public void   setTitulo(String titulo)             { this.titulo = titulo; }
    public String getDescripcion()                     { return descripcion; }
    public void   setDescripcion(String desc)          { this.descripcion = desc; }
    public List<String> getOpciones()                  { return opciones; }
    public void   setOpciones(List<String> opciones)   { this.opciones = opciones; }
}
