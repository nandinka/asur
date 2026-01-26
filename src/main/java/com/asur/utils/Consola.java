package com.asur.utils;

import com.asur.Main;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.function.Function;

public class Consola {

    public static int pedirInt(String texto) {
        System.out.print(texto);
        Integer valor = null;
        try {
            valor = Main.sc.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("debe ser un numero entero");
        }
        Main.sc.nextLine();
        return valor != null ? valor : pedirInt(texto);
    }

    public static int pedirInt(String texto, Function<Integer, String> validador) {
        System.out.print(texto);
        Integer valor = null;
        try {
            valor = Main.sc.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("debe ser un numero entero");
        }
        Main.sc.nextLine();
        if (valor == null) return pedirInt(texto, validador);
        String error = validador.apply(valor);
        if (error == null) return valor;
        System.out.println(error);
        return pedirInt(texto, validador);
    }

    public static int[] pedirInts(String texto) {
        System.out.printf("%s (ej. \"1, 2, 3\"): ", texto);
        String entrada = Main.sc.nextLine();
        if (!entrada.matches("^[0-9,\\s]+$")) {
            System.out.println("formato invalido");
            return pedirInts(texto);
        }
        int[] valores = Arrays.stream(entrada.split("\\s*,\\s*"))
                .mapToInt(Integer::parseInt)
                .toArray();
        if (valores.length < 1) {
            System.out.println("debe ingresar al menos un valor");
            return pedirInts(texto);
        }
        return valores;
    }

    public static boolean pedirBoolean(String texto) {
        System.out.print(texto);
        String entrada = Main.sc.nextLine().toLowerCase();
        return switch (entrada) {
            case "true", "si", "s", "y", "1" -> true;
            case "false", "no", "n", "0" -> false;
            default -> {
                System.out.println("responda si o no");
                yield pedirBoolean(texto);
            }
        };
    }

    public static String pedirString(String texto) {
        System.out.print(texto);
        return Main.sc.nextLine();
    }

    public static String pedirString(String texto, Function<String, String> validador) {
        System.out.print(texto);
        String entrada = Main.sc.nextLine();
        String error = validador.apply(entrada);
        if (error == null) return entrada;
        System.out.println(error);
        return pedirString(texto, validador);
    }

    public static String pedirStringOpcional(String texto) {
        System.out.print(texto + " (enter para omitir): ");
        String entrada = Main.sc.nextLine();
        return entrada.isBlank() ? null : entrada;
    }

    public static Date pedirDate(String texto) {
        System.out.printf("%s (AAAA-MM-DD): ", texto);
        String entrada = Main.sc.nextLine();
        try {
            return Date.valueOf(entrada);
        } catch (IllegalArgumentException e) {
            System.out.println("formato de fecha invalido");
            return pedirDate(texto);
        }
    }

    public static Date pedirDatePasada(String texto) {
        Date fecha = pedirDate(texto);
        if (fecha.after(new Date(System.currentTimeMillis()))) {
            System.out.println("la fecha debe ser anterior a hoy");
            return pedirDatePasada(texto);
        }
        return fecha;
    }

    public static Date pedirDateFutura(String texto) {
        Date fecha = pedirDate(texto);
        if (fecha.before(new Date(System.currentTimeMillis()))) {
            System.out.println("la fecha debe ser posterior a hoy");
            return pedirDateFutura(texto);
        }
        return fecha;
    }

    public static Time pedirTime(String texto) {
        System.out.printf("%s (HH:MM:SS): ", texto);
        String entrada = Main.sc.nextLine();
        try {
            return Time.valueOf(entrada);
        } catch (IllegalArgumentException e) {
            System.out.println("formato de hora invalido");
            return pedirTime(texto);
        }
    }

    public static Timestamp pedirTimestamp(String texto) {
        System.out.printf("%s (AAAA-MM-DD HH:MM:SS): ", texto);
        String entrada = Main.sc.nextLine();
        try {
            return Timestamp.valueOf(entrada);
        } catch (IllegalArgumentException e) {
            System.out.println("formato invalido");
            return pedirTimestamp(texto);
        }
    }

    public static Timestamp pedirTimestampFuturo(String texto) {
        Timestamp ts = pedirTimestamp(texto);
        if (ts.before(new Timestamp(System.currentTimeMillis()))) {
            System.out.println("debe ser una fecha futura");
            return pedirTimestampFuturo(texto);
        }
        return ts;
    }

    private static boolean continuar = true;

    public static void pedirSeleccion(String titulo, Opcion... opciones) {
        pedirSeleccion(titulo, new ArrayList<>(Arrays.asList(opciones)));
    }

    public static void pedirSeleccion(String titulo, ArrayList<Opcion> opciones) {
        if (opciones == null || opciones.isEmpty()) {
            throw new IllegalArgumentException("debe haber opciones");
        }
        if (!opciones.get(opciones.size() - 1).titulo.equals("Salir")) {
            opciones.add(opcion("Salir", () -> continuar = false));
        }

        mostrarOpciones(titulo, opciones);
        int seleccion = pedirOpcion(opciones.size());
        opciones.get(seleccion - 1).accion.run();

        if (continuar) {
            pedirSeleccion(titulo, opciones);
        }
        continuar = true;
    }

    private static void mostrarOpciones(String titulo, List<Opcion> opciones) {
        System.out.println("\n" + titulo);
        System.out.println("-".repeat(titulo.length()));
        for (int i = 0; i < opciones.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, opciones.get(i).titulo);
        }
    }

    public static int pedirOpcion(int cantidad) {
        int opcion = pedirInt("opcion: ");
        if (opcion > 0 && opcion <= cantidad) return opcion;
        System.out.println("opcion invalida");
        return pedirOpcion(cantidad);
    }

    public static Opcion opcion(String titulo, Runnable accion) {
        return new Opcion(titulo, accion);
    }

    public static ArrayList<Opcion> opciones(Opcion... opciones) {
        return new ArrayList<>(Arrays.asList(opciones));
    }

    public static class Opcion {
        String titulo;
        Runnable accion;

        public Opcion(String titulo, Runnable accion) {
            this.titulo = titulo;
            this.accion = accion;
        }
    }
}
