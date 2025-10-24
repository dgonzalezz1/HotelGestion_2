package com.hotel.modelo;

/**
 * Clase que representa una Habitación del hotel
 * Aplica conceptos de POO: Encapsulamiento, Getters y Setters
 */
public class Habitacion {
    
    // Atributos privados
    private int id;
    private String numero;
    private String tipo;
    private double precioNoche;
    private int capacidad;
    private String estado;
    private String descripcion;
    
    // Constructor vacío
    public Habitacion() {
    }
    
    // Constructor con parámetros principales
    public Habitacion(String numero, String tipo, double precioNoche, int capacidad, String descripcion) {
        this.numero = numero;
        this.tipo = tipo;
        this.precioNoche = precioNoche;
        this.capacidad = capacidad;
        this.estado = "Disponible";
        this.descripcion = descripcion;
    }
    
    // Constructor completo
    public Habitacion(int id, String numero, String tipo, double precioNoche, 
                      int capacidad, String estado, String descripcion) {
        this.id = id;
        this.numero = numero;
        this.tipo = tipo;
        this.precioNoche = precioNoche;
        this.capacidad = capacidad;
        this.estado = estado;
        this.descripcion = descripcion;
    }
    
    // Getters y Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNumero() {
        return numero;
    }
    
    public void setNumero(String numero) {
        this.numero = numero;
    }
    
    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public double getPrecioNoche() {
        return precioNoche;
    }
    
    public void setPrecioNoche(double precioNoche) {
        this.precioNoche = precioNoche;
    }
    
    public int getCapacidad() {
        return capacidad;
    }
    
    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    // Métodos de negocio
    
    /**
     * Verifica si la habitación está disponible
     */
    public boolean estaDisponible() {
        return "Disponible".equals(estado);
    }
    
    /**
     * Marca la habitación como ocupada
     */
    public void ocupar() {
        this.estado = "Ocupada";
    }
    
    /**
     * Marca la habitación como disponible
     */
    public void liberar() {
        this.estado = "Disponible";
    }
    
    /**
     * Bloquea la habitación
     */
    public void bloquear() {
        this.estado = "Bloqueada";
    }
    
    /**
     * Calcula el precio total por número de noches
     */
    public double calcularPrecioTotal(int numeroNoches) {
        return precioNoche * numeroNoches;
    }
    
    @Override
    public String toString() {
        return "Habitacion{" +
                "numero='" + numero + '\'' +
                ", tipo='" + tipo + '\'' +
                ", precio=" + precioNoche +
                ", estado='" + estado + '\'' +
                '}';
    }
}