package src;

import java.io.*;
import java.util.*;

public class GenerateInfoFiles {

    // Genera un archivo pseudoaleatorio de ventas de un vendedor
    public static void createSalesMenFile(int randomSalesCount, String firstName, String lastName, long id) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(firstName + lastName + "_" + id + ".txt"))) {
            Random rand = new Random();
            writer.write("CC;" + id + "\n");  // TipoDocumentoVendedor y NúmeroDocumentoVendedor

            for (int i = 0; i < randomSalesCount; i++) {
                int products = rand.nextInt(5) + 1;  // Genera entre 1 y 5 productos vendidos
                for (int j = 0; j < products; j++) {
                    writer.write((j + 1) + ";" + (rand.nextInt(10) + 1) + ";\n");  // IDProducto y CantidadProductoVendido
                }
            }
            System.out.println("Archivo de ventas para " + firstName + " " + lastName + " generado exitosamente.");
        } catch (IOException e) {
            System.out.println("Error generando archivo de ventas: " + e.getMessage());
        }
    }

    // Genera un archivo con información pseudoaleatoria de productos relacionados con electrodomésticos y tecnología
    public static void createProductsFile(int productsCount) {
        String[] productNames = {
                "Televisión", "Laptop", "Smartphone", "Tablet", "Cámara",
                "Auriculares", "Smartwatch", "Consola de videojuegos", "Refrigerador", "Lavadora",
                "Secadora", "Microondas", "Cafetera", "Aspiradora", "Plancha",
                "Parlante Bluetooth", "Monitor", "Teclado", "Mouse", "Impresora"
        };

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("productos.txt"))) {
            Random rand = new Random();
            for (int i = 0; i < productsCount; i++) {
                String productName = productNames[i % productNames.length];  // Rotar nombres de productos
                double price = (rand.nextInt(1500) + 100) + rand.nextDouble();  // Generar precio entre 100 y 1500 dólares
                writer.write((i + 1) + ";" + productName + ";" + String.format("%.2f", price).replaceAll(",",".") + "\n");  // ID, nombre, precio
            }
            System.out.println("Archivo de productos generado exitosamente.");
        } catch (IOException e) {
            System.out.println("Error generando archivo de productos: " + e.getMessage());
        }
    }

    // Genera un archivo con información pseudoaleatoria de vendedores
    public static void createSalesManInfoFile(int salesmanCount, String[] firstNames, String[] lastNames, long[] ids) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("vendedores.txt"))) {
            for (int i = 0; i < salesmanCount; i++) {
                writer.write("CC;" + ids[i] + ";" + firstNames[i] + ";" + lastNames[i] + "\n");
                createSalesMenFile(3, firstNames[i], lastNames[i], ids[i]);  // Crear el archivo de ventas para cada vendedor
            }
            System.out.println("Archivo de información de vendedores generado exitosamente.");
        } catch (IOException e) {
            System.out.println("Error generando archivo de información de vendedores: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Información de vendedores
        String[] firstNames = {"Juan", "Maria", "Carlos", "Ana", "Pedro"};
        String[] lastNames = {"Perez", "Lopez", "Gomez", "Rodriguez", "Sanchez"};
        long[] ids = {123456789L, 234567891L, 345678912L, 456789123L, 567891234L};

        // Generar archivo de vendedores y sus archivos de ventas
        createSalesManInfoFile(5, firstNames, lastNames, ids);

        // Crear archivo de productos con 20 productos de electrodomésticos y tecnología
        createProductsFile(20);
    }
}
