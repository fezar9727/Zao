package com.zao.backend;

import com.zao.backend.turnos.modelo.Turno;
import com.zao.backend.turnos.repositorio.TurnoRepository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class PruebaTurnoRepository {

    public static void main(String[] args) {
        TurnoRepository repository = new TurnoRepository();

        try {
            System.out.println("== 1. INSERTAR ==");
            Turno nuevo = new Turno(null, 1L, 1L, LocalDate.now(), LocalTime.of(8, 0), LocalTime.of(16, 0), false);
            nuevo = repository.insertar(nuevo);
            System.out.println("Turno insertado: " + nuevo);

            System.out.println("== 2. CONSULTAR TODOS ==");
            List<Turno> turnos = repository.consultarTodos();
            turnos.forEach(System.out::println);

            System.out.println("== 3. CONSULTAR POR ID ==");
            Turno consultado = repository.consultarPorId(nuevo.getIdTurno());
            System.out.println("Turno encontrado: " + consultado);

            System.out.println("== 4. ACTUALIZAR ==");
            consultado.setChecklistCompletado(true);
            boolean actualizado = repository.actualizar(consultado);
            System.out.println("¿Se actualizó?: " + actualizado);

            System.out.println("== 5. ELIMINAR ==");
            boolean eliminado = repository.eliminar(consultado.getIdTurno());
            System.out.println("¿Se eliminó?: " + eliminado);

        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}