package src;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Main {

    // Procesa los archivos de ventas y genera un archivo de reporte de vendedores
    public static void generateSalesReport() {
        Map<String, Double> vendorEarnings = new HashMap<>();
        Map<String, String> vendorNames = new HashMap<>();

        // Primero, leer el archivo de vendedores para obtener los nombres y apellidos
        try (BufferedReader reader = new BufferedReader(new FileReader("vendedores.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                String id = parts[1];
                String name = parts[2] + " " + parts[3];
                vendorNames.put(id, name);
            }
        } catch (IOException e) {
            System.out.println("Error leyendo archivo de vendedores: " + e.getMessage());
        }

        // Procesar los archivos de ventas
        try {
            File folder = new File(".");  // Asume que los archivos están en el directorio actual
            File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt") && !name.equals("productos.txt") && !name.equals("vendedores.txt"));

            for (File file : files) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    String vendorId = null;
                    double totalSales = 0.0;
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("CC;")) {
                            vendorId = line.split(";")[1];
                        } else {
                            String[] parts = line.split(";");
                            int quantitySold = Integer.parseInt(parts[1]);
                            double productPrice = getProductPrice(Integer.parseInt(parts[0]));
                            totalSales += quantitySold * productPrice;
                        }
                    }
                    if (vendorId != null) {
                        vendorEarnings.put(vendorId, vendorEarnings.getOrDefault(vendorId, 0.0) + totalSales);
                    }
                }
            }

            // Escribir archivo de reporte de vendedores
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("reporte_vendedores.csv"))) {
                writer.write("Vendedor;TotalRecaudado\n"); // Encabezado
                vendorEarnings.entrySet().stream()
                        .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                        .forEach(entry -> {
                            try {
                                String vendorId = entry.getKey();
                                String vendorName = vendorNames.getOrDefault(vendorId, "Desconocido");
                                writer.write(vendorName + ";" + String.format("%.2f", entry.getValue()) + "\n");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
            }
            System.out.println("Reporte de ventas generado exitosamente.");
        } catch (IOException e) {
            System.out.println("Error procesando archivos de ventas: " + e.getMessage());
        }
    }

    // Método auxiliar para obtener el precio de un producto basado en su ID
    private static double getProductPrice(int productId) {
        try (BufferedReader reader = new BufferedReader(new FileReader("productos.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (Integer.parseInt(parts[0]) == productId) {
                    return Double.parseDouble(parts[2]); // Retorna el precio como double
                }
            }
        } catch (IOException e) {
            System.out.println("Error leyendo archivo de productos: " + e.getMessage());
        }
        return 0;  // Retorna 0 si no encuentra el producto
    }

    // Procesa los archivos de productos y genera un archivo de reporte de productos vendidos
    public static void generateProductReport() {
        Map<String, Integer> productSales = new HashMap<>();
        try {
            File folder = new File(".");  // Asume que los archivos están en el directorio actual
            File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt") && !name.equals("productos.txt") && !name.equals("vendedores.txt"));

            for (File file : files) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (!line.startsWith("CC;")) {
                            String[] parts = line.split(";");
                            int productId = Integer.parseInt(parts[0]);
                            int quantitySold = Integer.parseInt(parts[1]);
                            String productName = getProductName(productId);
                            productSales.put(productName, productSales.getOrDefault(productName, 0) + quantitySold);
                        }
                    }
                }
            }

            // Escribir archivo de reporte de productos
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("reporte_productos.csv"))) {
                writer.write("Producto;Precio\n"); // Encabezado
                productSales.entrySet().stream()
                        .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                        .forEach(entry -> {
                            try {
                                String productName = entry.getKey();
                                double productPrice = getProductPriceByName(productName);
                                writer.write(productName + ";" + String.format("%.2f", productPrice) + "\n");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
            }
            System.out.println("Reporte de productos generado exitosamente.");
        } catch (IOException e) {
            System.out.println("Error procesando archivos de productos: " + e.getMessage());
        }
    }

    // Método auxiliar para obtener el nombre de un producto basado en su ID
    private static String getProductName(int productId) {
        try (BufferedReader reader = new BufferedReader(new FileReader("productos.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (Integer.parseInt(parts[0]) == productId) {
                    return parts[1]; // Retorna el nombre del producto
                }
            }
        } catch (IOException e) {
            System.out.println("Error leyendo archivo de productos: " + e.getMessage());
        }
        return "Desconocido";  // Retorna un nombre por defecto si no encuentra el producto
    }

    // Método auxiliar para obtener el precio de un producto basado en su nombre
    private static double getProductPriceByName(String productName) {
        try (BufferedReader reader = new BufferedReader(new FileReader("productos.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts[1].equalsIgnoreCase(productName)) {
                    return Double.parseDouble(parts[2]); // Retorna el precio como double
                }
            }
        } catch (IOException e) {
            System.out.println("Error leyendo archivo de productos: " + e.getMessage());
        }
        return 0;  // Retorna 0 si no encuentra el producto
    }

    public static void main(String[] args) {
        generateSalesReport();
        generateProductReport();
    }
}
