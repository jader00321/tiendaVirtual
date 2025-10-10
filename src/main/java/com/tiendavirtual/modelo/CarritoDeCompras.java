package com.tiendavirtual.modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarritoDeCompras {
    private final Map<Integer, CarritoItem> items = new HashMap<>();

    public void agregarItem(Producto producto, int cantidad) {
        int productoId = producto.getId();
        if (items.containsKey(productoId)) {
            CarritoItem itemExistente = items.get(productoId);
            itemExistente.setCantidad(itemExistente.getCantidad() + cantidad);
        } else {
            items.put(productoId, new CarritoItem(producto, cantidad));
        }
    }

    public void actualizarCantidad(int productoId, int cantidad) {
        if (items.containsKey(productoId)) {
            if (cantidad > 0) {
                items.get(productoId).setCantidad(cantidad);
            } else {
                eliminarItem(productoId);
            }
        }
    }

    public void eliminarItem(int productoId) {
        items.remove(productoId);
    }

    public List<CarritoItem> getItems() {
        return new ArrayList<>(items.values());
    }

    public double getTotal() {
        return items.values().stream()
                    .mapToDouble(CarritoItem::getSubtotal)
                    .sum();
    }
    
    public int getNumeroItems() {
        return items.values().stream()
                    .mapToInt(CarritoItem::getCantidad)
                    .sum();
    }
    
    public void limpiar() {
        items.clear();
    }
}