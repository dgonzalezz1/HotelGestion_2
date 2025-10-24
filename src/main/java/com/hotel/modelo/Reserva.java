package com.hotel.modelo;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Clase que representa una Reserva en el hotel
 * Aplica conceptos de POO: Encapsulamiento, Composición, Métodos de cálculo
 */
public class Reserva {
    
    // Atributos privados
    private int id;
    private int clienteId;
    private int habitacionId;
    private Date fechaEntrada;
    private Date fechaSalida;
    private int numeroPersonas;
    private double anticipo;
    private double total;
    private String estado;
    private Timestamp fechaReserva;
    
    // Atributos auxiliares (composición)
    private Cliente cliente;
    private Habitacion habitacion;
    
    // Constructor vacío
    public Reserva() {
    }
    
    // Constructor para nueva reserva
    public Reserva(int clienteId, int habitacionId, Date fechaEntrada, 
                   Date fechaSalida, int numeroPersonas, double anticipo) {
        this.clienteId = clienteId;
        this.habitacionId = habitacionId;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.numeroPersonas = numeroPersonas;
        this.anticipo = anticipo;
        this.estado = "Pendiente";
    }
    
    // Constructor completo
    public Reserva(int id, int clienteId, int habitacionId, Date fechaEntrada, 
                   Date fechaSalida, int numeroPersonas, double anticipo, 
                   double total, String estado, Timestamp fechaReserva) {
        this.id = id;
        this.clienteId = clienteId;
        this.habitacionId = habitacionId;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.numeroPersonas = numeroPersonas;
        this.anticipo = anticipo;
        this.total = total;
        this.estado = estado;
        this.fechaReserva = fechaReserva;
    }
    
    // Getters y Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getClienteId() {
        return clienteId;
    }
    
    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }
    
    public int getHabitacionId() {
        return habitacionId;
    }
    
    public void setHabitacionId(int habitacionId) {
        this.habitacionId = habitacionId;
    }
    
    public Date getFechaEntrada() {
        return fechaEntrada;
    }
    
    public void setFechaEntrada(Date fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }
    
    public Date getFechaSalida() {
        return fechaSalida;
    }
    
    public void setFechaSalida(Date fechaSalida) {
        this.fechaSalida = fechaSalida;
    }
    
    public int getNumeroPersonas() {
        return numeroPersonas;
    }
    
    public void setNumeroPersonas(int numeroPersonas) {
        this.numeroPersonas = numeroPersonas;
    }
    
    public double getAnticipo() {
        return anticipo;
    }
    
    public void setAnticipo(double anticipo) {
        this.anticipo = anticipo;
    }
    
    public double getTotal() {
        return total;
    }
    
    public void setTotal(double total) {
        this.total = total;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public Timestamp getFechaReserva() {
        return fechaReserva;
    }
    
    public void setFechaReserva(Timestamp fechaReserva) {
        this.fechaReserva = fechaReserva;
    }
    
    public Cliente getCliente() {
        return cliente;
    }
    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    public Habitacion getHabitacion() {
        return habitacion;
    }
    
    public void setHabitacion(Habitacion habitacion) {
        this.habitacion = habitacion;
    }
    
    // Métodos de negocio
    
    /**
     * Calcula el número de noches de la reserva
     */
    public long calcularNumeroNoches() {
        LocalDate entrada = fechaEntrada.toLocalDate();
        LocalDate salida = fechaSalida.toLocalDate();
        return ChronoUnit.DAYS.between(entrada, salida);
    }
    
    /**
     * Calcula el total de la reserva basado en el precio por noche
     */
    public double calcularTotal(double precioNoche) {
        return calcularNumeroNoches() * precioNoche;
    }
    
    /**
     * Calcula el saldo pendiente
     */
    public double calcularSaldo() {
        return total - anticipo;
    }
    
    /**
     * Verifica si la reserva está pagada completamente
     */
    public boolean estaPagada() {
        return anticipo >= total;
    }
    
    /**
     * Confirma la reserva
     */
    public void confirmar() {
        this.estado = "Confirmada";
    }
    
    /**
     * Realiza el check-in
     */
    public void hacerCheckIn() {
        this.estado = "Check-in";
    }
    
    /**
     * Realiza el check-out
     */
    public void hacerCheckOut() {
        this.estado = "Check-out";
    }
    
    /**
     * Cancela la reserva
     */
    public void cancelar() {
        this.estado = "Cancelada";
    }
    
    @Override
    public String toString() {
        return "Reserva{" +
                "id=" + id +
                ", clienteId=" + clienteId +
                ", habitacionId=" + habitacionId +
                ", fechaEntrada=" + fechaEntrada +
                ", fechaSalida=" + fechaSalida +
                ", estado='" + estado + '\'' +
                ", total=" + total +
                '}';
    }
}