package com.asur;

import com.asur.controlador.ControladorPrincipal;
import java.util.Scanner;

public class Main {
    public static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=================================");
        System.out.println("  ASUR - Sistema de Gestion");
        System.out.println("  Asociacion de Sordos del Uruguay");
        System.out.println("=================================\n");

        ControladorPrincipal controlador = new ControladorPrincipal();
        controlador.iniciar();

        sc.close();
    }
}
