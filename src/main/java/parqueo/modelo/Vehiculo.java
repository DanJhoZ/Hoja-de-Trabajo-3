/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package parqueo.modelo;

/**
 *
 * @author zetd6
 */
public abstract class Vehiculo {
    protected int id;
    protected String placa;
    protected int hora;
    protected TipodeVehiculo tipovehiculo;

    public Vehiculo(String placa, int hora, TipodeVehiculo tipovehiculo) {
        this.placa = placa;
        this.hora = hora;
        this.tipovehiculo = tipovehiculo;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getplaca() { return placa; }
    public int gethora() { return hora; }
    public TipodeVehiculo getTipoVehiculo() { return tipovehiculo; }

    // Cada subclase (auto, moto) debe sobrescribir este método con su propia fórmula
    public abstract double calcularCostoTotal(double costoBase);

    public double calcularCostoTotal(double costoBase, double porcentajeBeca) {
        double costo = calcularCostoTotal(costoBase);
        double descuento = costo * (porcentajeBeca / 100.0);
        return costo - descuento;
    }

    public abstract String mostrarInformacion();
}