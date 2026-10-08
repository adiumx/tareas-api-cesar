package mx.generation.tareas;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Casos de uso del gestor de tareas. Es la clase que usarían un controlador REST o una CLI.
 */
public class TareaServicio {

    private final TareaRepositorio repositorio;

    public TareaServicio(TareaRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    public Tarea crear(String titulo, String descripcion, Prioridad prioridad, LocalDate fechaLimite) {
        return repositorio.guardar(new Tarea(titulo, descripcion, prioridad, fechaLimite));
    }

    /** Marca la tarea como completada. Si el id no existe, lanza TareaNoEncontradaException. */
    public Tarea completar(int id) {
        Tarea tarea = obtener(id);
        tarea.marcarCompletada();
        return tarea;
    }

    public boolean eliminar(int id) {
        return repositorio.eliminar(id);
    }

    public List<Tarea> listarPendientes() {
        List<Tarea> pendientes = new ArrayList<>();
        for (Tarea t : repositorio.todas()) {
            if (!t.isCompletada()) {
                pendientes.add(t);
            }
        }
        return pendientes;
    }

    /**
     * Tareas pendientes con prioridad igual o mayor a la indicada.
     * Con MEDIA devuelve las MEDIA y las ALTA; con ALTA, solo las ALTA.
     */
    public List<Tarea> listarPorPrioridadMinima(Prioridad minima) {
        List<Tarea> resultado = new ArrayList<>();
        for (Tarea t : listarPendientes()) {
            if (t.getPrioridad().ordinal() > minima.ordinal()) {
                resultado.add(t);
            }
        }
        return resultado;
    }

    /**
     * Días que faltan para la fecha límite: positivo si está en el futuro, 0 si es hoy,
     * negativo si ya se venció.
     */
    public long diasRestantes(int id, LocalDate hoy) {
        Tarea tarea = obtener(id);
        if (tarea.getFechaLimite() == null) {
            return Long.MAX_VALUE;
        }
        return ChronoUnit.DAYS.between(tarea.getFechaLimite(), hoy);
    }

    /**
     * Reporte en texto plano para el resumen diario.
     */
    public String generarReporte(LocalDate hoy) {
        StringBuilder r = new StringBuilder();
        r.append("=== REPORTE DE TAREAS ===\n");

        EstadisticasTareas stats = calcularEstadisticas(hoy);

        r.append("Total: ").append(stats.total).append("\n");
        r.append("Completadas: ").append(stats.hechas).append("\n");
        r.append("Pendientes: ").append(stats.total - stats.hechas).append("\n");
        r.append("Vencidas: ").append(stats.vencidas).append("\n");
        r.append("Alta prioridad pendientes: ").append(stats.altas).append("\n");
        r.append(calificarAvance(stats.total, stats.hechas));
        r.append("--- Pendientes ---\n");
        r.append(listarPendientesTexto());

        return r.toString();
    }

    private EstadisticasTareas calcularEstadisticas(LocalDate hoy) {
        int total = 0;
        int hechas = 0;
        int vencidas = 0;
        int altas = 0;

        for (Tarea t : repositorio.todas()) {
            total++;
            if (t.isCompletada()) {
                hechas++;
            } else {
                if (estaVencida(t, hoy)) {
                    vencidas++;
                }
                if (t.getPrioridad() == Prioridad.ALTA) {
                    altas++;
                }
            }
        }

        return new EstadisticasTareas(total, hechas, vencidas, altas);
    }

    private boolean estaVencida(Tarea t, LocalDate hoy) {
        return t.getFechaLimite() != null && t.getFechaLimite().isBefore(hoy);
    }

    private String calificarAvance(int total, int hechas) {
        if (total == 0) {
            return "Estado: SIN TAREAS\n";
        }

        int porcentaje = hechas * 100 / total;
        if (porcentaje >= 80) {
            return "Estado: EXCELENTE (" + porcentaje + "%)\n";
        }
        if (porcentaje >= 50) {
            return "Estado: BIEN (" + porcentaje + "%)\n";
        }
        return "Estado: ATRASADO (" + porcentaje + "%)\n";
    }

    private String listarPendientesTexto() {
        StringBuilder r = new StringBuilder();
        for (Tarea t : repositorio.todas()) {
            if (!t.isCompletada()) {
                r.append("* ").append(t.getTitulo());
                if (t.getFechaLimite() != null) {
                    r.append(" (vence ").append(t.getFechaLimite()).append(")");
                }
                r.append("\n");
            }
        }
        return r.toString();
    }

    private static class EstadisticasTareas {
        final int total;
        final int hechas;
        final int vencidas;
        final int altas;

        EstadisticasTareas(int total, int hechas, int vencidas, int altas) {
            this.total = total;
            this.hechas = hechas;
            this.vencidas = vencidas;
            this.altas = altas;
        }
    }

    private Tarea obtener(int id) {
        Tarea tarea = repositorio.buscar(id);
        if (tarea == null) {
            throw new TareaNoEncontradaException(id);
        }
        return tarea;
    }
}
