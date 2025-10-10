// Ruta: src/main/java/com/tiendavirtual/modelo/Usuario.java
package com.tiendavirtual.modelo;

public class Usuario {
    private int id;
    private String nombre;
    private String email;
    private String password;
    private String rol;
    private boolean activo; // Nuevo atributo

    public Usuario() {
    }

    // --- Getters y Setters (incluyendo los nuevos para 'activo') ---
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}