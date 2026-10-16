package com.sistema.gestion.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
/**
 *
 * @author sosap
 */
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 20)
    private String rol; // Ejemplo: "ADMIN", "VENDEDOR"

    // Parametros de seguridad iniciales
    @Column(nullable = false)
    private boolean activo = true;

    @Column(nullable = false)
    private int intentosFallidos = 0;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    public Usuario() {
    }

    @PrePersist
    protected void alCrear() {
        this.fechaCreacion = LocalDateTime.now();
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public int getIntentosFallidos() { return intentosFallidos; }
    public void setIntentosFallidos(int intentosFallidos) { this.intentosFallidos = intentosFallidos; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }

    // Seguridad: Nunca incluimos la contraseña en el metodo toString()
    // para que no se filtre por accidente en los logs de la consola.
    @Override
    public String toString() {
        return "Usuario{id=" + id + ", username='" + username + "', rol='" + rol + "', activo=" + activo + "}";
    }
}

