package parqueo.modelo;

/**
 *
 * @author zetd6
 */
public class auto extends Vehiculo{
    
    public auto(String placa, int hora) {
        super(placa, hora, TipodeVehiculo.AUTO);
    }

    @Override
    public double calcularCostoTotal(double costoBase) {
        return costoBase * hora;
    }

    @Override
    public String mostrarInformacion() {
        return "Tipo: Automovil \n"
                + "Placa: " + placa + "\n"
                + "Hora de entrada: " + hora + "\n";
    }
}