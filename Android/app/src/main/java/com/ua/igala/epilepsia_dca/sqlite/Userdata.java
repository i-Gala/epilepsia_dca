package com.ua.igala.epilepsia_dca.sqlite;
import java.util.UUID;

public class Userdata {
    interface ColumnasUsuario {
        String ID = "id";
        String EMAIL = "email";
        String NOMBRE = "nombre";
        String APELLIDOS = "apellidos";
        String PASSWORD = "password";
        String FIRST_CONEXION = "first_conexion";
        String ALARMA_BLUETOOTH = "alarma_bluetooth";
        String ALARMA_TELEFONO = "alarma_telefono";
        String MAX_HR = "max_hr";
        String MIN_HR = "min_hr";
        String TIEMPO_ESPERA = "tiempo_espera";
    }

    interface ColumnasTelefonoEmergencias {
        String ID = "id";
        String ID_USUARIO = "id_usuario";
        String TELEFONO = "telefono";
    }

    interface ColumnasRegistroDiario {
        String ID = "id";
        String ID_USUARIO = "id_usuario";
        String FECHA = "fecha";
    }

    interface ColumnasRegistroSemanal {
        String ID = "id";
        String ID_USUARIO = "id_usuario";
        String DIA = "dia";
        String MES = "mes";
        String ANYO = "anyo";
    }

    interface ColumnasRegistroMensual {
        String ID = "id";
        String ID_USUARIO = "id_usuario";
        String MES = "mes";
        String ANYO = "anyo";
    }

    interface ColumnasRegistroAnual {
        String ID = "id";
        String ID_USUARIO = "id_usuario";
        String ANYO = "anyo";
    }

    public static class Usuarios implements ColumnasUsuario {
        public static String generarIdUsuario() {
            return "USU-" + UUID.randomUUID().toString();
        }
    }

    public static class TelefonosEmergencias implements ColumnasTelefonoEmergencias {
        public static String generarIdTelefonoEmergencias() {
            return "TE-" + UUID.randomUUID().toString();
        }
    }

    public static class RegistrosDiarios implements ColumnasRegistroDiario {
        public static String generarIdRegistroDiario() {
            return "REG-" + UUID.randomUUID().toString();
        }
    }

    public static class RegistrosSemanales implements ColumnasRegistroSemanal {
        public static String generarIdRegistroSemanal() {
            return "REGS-" + UUID.randomUUID().toString();
        }
    }

    public static class RegistrosMensuales implements ColumnasRegistroMensual {
        public static String generarIdRegistroMensual() {
            return "REGM-" + UUID.randomUUID().toString();
        }
    }

    public static class RegistrosAnuales implements ColumnasRegistroAnual {
        public static String generarIdRegistroAnual() {
            return "REGA-" + UUID.randomUUID().toString();
        }
    }

    private Userdata() {

    }
}
