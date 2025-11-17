package Flyweight;

import java.util.HashMap;
import java.util.Map;

public class FlyweightFactory {
    private static FlyweightFactory instance;
    private Map<String, CaracteristicasCompartidas> flyweights;

    private FlyweightFactory() {
        flyweights = new HashMap<>();
    }

    public static synchronized FlyweightFactory getInstance() {
        if (instance == null) {
            instance = new FlyweightFactory();
        }
        return instance;
    }

    public CaracteristicasCompartidas getCaracteristicas(String categoria, String marca,
                                                         String edadRecomendada, double pesoKg) {
        String key = categoria + "-" + marca + "-" + edadRecomendada + "-" + pesoKg;

        CaracteristicasCompartidas caracteristicas = flyweights.get(key);

        if (caracteristicas == null) {
            caracteristicas = new CaracteristicasCompartidas(
                    categoria, marca, edadRecomendada, pesoKg);
            flyweights.put(key, caracteristicas);
            System.out.println("Creado nuevo Flyweight: " + key);
        } else {
            System.out.println("Reutilizando Flyweight: " + key);
        }

        return caracteristicas;
    }

    public int getCantidadFlyweights() {
        return flyweights.size();
    }

    public void limpiarCache() {
        flyweights.clear();
    }
}
