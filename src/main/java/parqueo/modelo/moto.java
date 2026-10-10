package parqueo.modelo;

/**
 *
 * @author zetd6
 */
public class moto extends Vehiculo{

    public moto(String placa, int hora) {
        super(placa, hora, TipodeVehiculo.MOTO);
    }

    @Override
    public double calcularCostoTotal(double costoBase) {
        return costoBase * hora * 0.5;
    }

    @Override
    public String mostrarInformacion() {
        return "Tipo: Motocicleta\n"
                + "Placa: " + placa+ "\n"
                + "Hora de Entrada: " + hora + "\n";
    }
}

