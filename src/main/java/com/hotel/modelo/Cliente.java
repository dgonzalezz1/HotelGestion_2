package com.hotel.modelo;

import java.sql.Timestamp;

/**
 * Clase que representa un Cliente del hotel
 * Aplica conceptos de POO: Encapsulamiento, Getters y Setters
 */
public class Cliente {
    
    // Atributos privados (Encapsulamiento)
    private int id;
    private String nombre;
    private String apellido;
    private String dpi;
    private String telefono;
    private String email;
    private String direccion;
    private Timestamp fechaRegistro;
    
    // Constructor vacío
    public Cliente() {
    }
    
    // Constructor con parámetros principales
    public Cliente(String nombre, String apellido, String dpi, String telefono, String email, String direccion) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dpi = dpi;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
    }
    
    // Constructor completo
    public Cliente(int id, String nombre, String apellido, String dpi, String telefono, 
                   String email, String direccion, Timestamp fechaRegistro) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dpi = dpi;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.fechaRegistro = fechaRegistro;
    }
    
    // Getters y Setters
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
    
    public String getApellido() {
        return apellido;
    }
    
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    
    public String getDpi() {
        return dpi;
    }
    
    public void setDpi(String dpi) {
        this.dpi = dpi;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getDireccion() {
        return direccion;
    }
    
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    public Timestamp getFechaRegistro() {
        return fechaRegistro;
    }
    
    public void setFechaRegistro(Timestamp fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
    
    // Método para obtener nombre completo
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
    
    // Método toString para imprimir información del cliente
    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", dpi='" + dpi + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}