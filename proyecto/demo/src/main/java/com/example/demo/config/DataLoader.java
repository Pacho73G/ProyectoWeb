/* Archivo documentado: Carga datos semilla cuando la aplicación inicia y la base aún está vacía. */
package com.example.demo.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.example.demo.model.Administrador;
import com.example.demo.model.CheckIn;
import com.example.demo.model.CheckpointRecorrido;
import com.example.demo.model.ConfiguracionSistema;
import com.example.demo.model.Coordinador;
import com.example.demo.model.Docente;
import com.example.demo.model.EstadoReasignacion;
import com.example.demo.model.EstadoRecorrido;
import com.example.demo.model.EstadoTurno;
import com.example.demo.model.Incidente;
import com.example.demo.model.MapaCalor;
import com.example.demo.model.MetodoCheckIn;
import com.example.demo.model.MetricaDocente;
import com.example.demo.model.Notificacion;
import com.example.demo.model.Reasignacion;
import com.example.demo.model.Reconocimiento;
import com.example.demo.model.Recorrido;
import com.example.demo.model.RegistroLimpieza;
import com.example.demo.model.RolUsuario;
import com.example.demo.model.SeveridadIncidente;
import com.example.demo.model.TipoIncidente;
import com.example.demo.model.TipoNotificacion;
import com.example.demo.model.TipoReconocimiento;
import com.example.demo.model.Turno;
import com.example.demo.model.Zona;
import com.example.demo.service.SistemaService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.seed.enabled", havingValue = "true", matchIfMissing = true)
public class DataLoader implements CommandLineRunner {

    private final SistemaService sistemaService;

    @Override
    public void run(String... args) {

        try {
            if (sistemaService.totalDocentes() > 0) {
                System.out.println("Base con datos existentes. Seed cancelado.");
                return;
            }
        } catch (Exception e) {
            System.out.println("No se pudo validar usuarios. Seed cancelado.");
            return;
        }

        Administrador admin = new Administrador();
        admin.setNombre("Laura Admin");
        admin.setEmail("laura.admin@colegio.edu");
        admin.setPasswordHash("hash-admin");
        admin.setRol(RolUsuario.ADMINISTRADOR);
        admin.setCargo("Directora TIC");
        admin = sistemaService.guardar(admin);

        Coordinador coordinador = new Coordinador();
        coordinador.setNombre("Ana Coordinadora");
        coordinador.setEmail("ana.coord@colegio.edu");
        coordinador.setPasswordHash("hash-coord");
        coordinador.setRol(RolUsuario.COORDINADOR);
        coordinador.setNivel("Secundaria");
        coordinador = sistemaService.guardar(coordinador);

        Docente docente1 = docente("Carlos Rodríguez", "carlos@colegio.edu", "Matemáticas, Física", 2, 950);
        Docente docente2 = docente("María López", "maria@colegio.edu", "Español, Sociales", 1, 910);
        Docente docente3 = docente("Juan Pérez", "juan@colegio.edu", "Educación Física", 3, 870);

        ConfiguracionSistema configuracion = new ConfiguracionSistema();
        configuracion.setAdministrador(admin);
        configuracion.setMinutosAlertaAusencia(10);
        configuracion.setSegundosVentanaReasignacion(300);
        configuracion.setMinutosInactividad(15);
        configuracion.setUmbralIngreso(80);
        configuracion.setMinutosRecordatorio1(10);
        configuracion.setMinutosRecordatorio2(5);
        sistemaService.guardar(configuracion);

        Zona patio = zona("Patio Principal", "Vigilancia general del recreo", "Bloque A", 300, true);
        Zona cafeteria = zona("Cafetería", "Control de filas y convivencia", "Pabellón central", 150, true);
        Zona deportiva = zona("Zona Deportiva", "Canchas y graderías", "Bloque B", 220, true);

        Turno turno1 = turno(docente1, patio, LocalDate.now(), "Recreo mañana primaria", "09:30", "09:50", EstadoTurno.EN_CURSO);
        Turno turno2 = turno(docente2, cafeteria, LocalDate.now(), "Cambio de bloque media mañana", "11:20", "11:35", EstadoTurno.PENDIENTE);
        Turno turno3 = turno(docente3, cafeteria, LocalDate.now(), "Almuerzo bachillerato", "13:00", "13:30", EstadoTurno.PENDIENTE);
        Turno turno4 = turno(docente1, deportiva, LocalDate.now(), "Recreo tarde", "15:15", "15:45", EstadoTurno.SIN_COBERTURA);
        Turno turno5 = turno(docente2, patio, LocalDate.now().plusDays(1), "Ingreso jornada mañana", "06:40", "07:00", EstadoTurno.PENDIENTE);
        Turno turno6 = turno(docente3, deportiva, LocalDate.now().plusDays(1), "Salida estudiantil", "16:30", "17:00", EstadoTurno.PENDIENTE);

        CheckIn checkIn = new CheckIn();
        checkIn.setTurno(turno1);
        checkIn.setDocente(docente1);
        checkIn.setZona(patio);
        checkIn.setTimestamp(LocalDateTime.now().minusMinutes(20));
        checkIn.setMetodo(MetodoCheckIn.QR);
        checkIn.setEvidencia("qr://patio-principal");
        checkIn.setValido(true);
        sistemaService.guardar(checkIn);

        Incidente incidente = new Incidente();
        incidente.setTurno(turno1);
        incidente.setDocente(docente1);
        incidente.setZona(patio);
        incidente.setTipo(TipoIncidente.CONVIVENCIA);
        incidente.setSeveridad(SeveridadIncidente.S2_SEGUIMIENTO);
        incidente.setDescripcion("Discusión entre estudiantes durante el recreo.");
        incidente.setObservacionSocial("Se recomendó seguimiento por orientación.");
        incidente.setRegistradoEn(LocalDateTime.now().minusMinutes(10));
        incidente.setRequiereSeguimiento(true);
        sistemaService.guardar(incidente);

        Reasignacion reasignacion = new Reasignacion();
        reasignacion.setTurno(turno4);
        reasignacion.setDocenteSolicitante(docente1);
        reasignacion.setDocenteReemplazo(docente2);
        reasignacion.setMotivo("Cobertura temporal por reunión académica.");
        reasignacion.setEstado(EstadoReasignacion.ACEPTADA);
        reasignacion.setPropuestaEn(LocalDateTime.now().minusMinutes(30));
        reasignacion.setRespondidaEn(LocalDateTime.now().minusMinutes(25));
        reasignacion.setSegundosVentana(300);
        sistemaService.guardar(reasignacion);

        RegistroLimpieza limpieza = new RegistroLimpieza();
        limpieza.setTurno(turno1);
        limpieza.setEscala(4);
        limpieza.setObservaciones("Zona entregada en buen estado.");
        limpieza.setRegistradoEn(LocalDateTime.now().minusMinutes(5));
        sistemaService.guardar(limpieza);

        Notificacion notificacion = new Notificacion();
        notificacion.setTurno(turno3);
        notificacion.setTipo(TipoNotificacion.RECORDATORIO_10MIN);
        notificacion.setMensaje("Tu turno de almuerzo bachillerato inicia en 10 minutos.");
        notificacion.setEnviadaEn(LocalDateTime.now().minusMinutes(1));
        notificacion.setLeida(false);
        notificacion.setMinutosAnticipacion(10);
        sistemaService.guardar(notificacion);

        Recorrido recorrido = new Recorrido();
        recorrido.setDocente(docente1);
        recorrido.setTurno(turno1);
        recorrido.setIniciadoEn(LocalDateTime.now().minusMinutes(18));
        recorrido.setFinalizadoEn(LocalDateTime.now().minusMinutes(3));
        recorrido.setEstado(EstadoRecorrido.COMPLETADO);
        recorrido.setDuracionMinutos(15);
        recorrido = sistemaService.guardar(recorrido);

        CheckpointRecorrido checkpoint = new CheckpointRecorrido();
        checkpoint.setZona(patio);
        checkpoint.setRecorrido(recorrido);
        checkpoint.setCodigoQR("QR-PATIO-001");
        checkpoint.setDescripcion("Entrada principal del patio");
        checkpoint.setOrden(1);
        checkpoint.setEscaneadoEn(LocalDateTime.now().minusMinutes(17));
        sistemaService.guardar(checkpoint);

        MapaCalor mapaCalor = new MapaCalor();
        mapaCalor.setZona(patio);
        mapaCalor.setFranja("10:00-11:00");
        mapaCalor.setTipoIncidente(TipoIncidente.CONVIVENCIA);
        mapaCalor.setTotalIncidentes(12);
        mapaCalor.setPorcentaje(42.5f);
        mapaCalor.setPeriodoInicio(LocalDate.now().minusDays(30));
        mapaCalor.setPeriodoFin(LocalDate.now());
        sistemaService.guardar(mapaCalor);

        MetricaDocente metrica = new MetricaDocente();
        metrica.setDocente(docente1);
        metrica.setPuntualidad(98);
        metrica.setCobertura(95);
        metrica.setRetrasos(1);
        metrica.setRecorridosCompletados(25);
        metrica.setIncidentesRegistrados(4);
        metrica.setReasignacionesAceptadas(3);
        metrica.setPuntajeTotal(950);
        metrica.setPeriodo("2026-Q1");
        metrica = sistemaService.guardar(metrica);

        Reconocimiento reconocimiento = new Reconocimiento();
        reconocimiento.setMetricaDocente(metrica);
        reconocimiento.setTitulo("Patrullero del trimestre");
        reconocimiento.setDescripcion("Mayor cumplimiento de vigilancia preventiva.");
        reconocimiento.setTipo(TipoReconocimiento.RECORRIDOS);
        reconocimiento.setOtorgadoEn(LocalDate.now());
        reconocimiento.setTrimestre("2026-T1");
        sistemaService.guardar(reconocimiento);

        System.out.println("Datos semilla cargados correctamente.");
    }

    private Docente docente(String nombre, String email, String materias, int carga, int puntaje) {
        Docente docente = new Docente();
        docente.setNombre(nombre);
        docente.setEmail(email);
        docente.setPasswordHash("hash-docente");
        docente.setRol(RolUsuario.DOCENTE);
        docente.setMaterias(materias);
        docente.setCargaActual(carga);
        docente.setPuntajeGamificacion(puntaje);
        return sistemaService.guardar(docente);
    }

    private Zona zona(String nombre, String descripcion, String ubicacion, int capacidad, boolean activa) {
        Zona zona = new Zona();
        zona.setNombre(nombre);
        zona.setDescripcion(descripcion);
        zona.setUbicacion(ubicacion);
        zona.setCapacidadMaxima(capacidad);
        zona.setActiva(activa);
        return sistemaService.guardar(zona);
    }

    private Turno turno(Docente docente, Zona zona, LocalDate fecha, String franja,
                        String inicio, String fin, EstadoTurno estado) {
        Turno turno = new Turno();
        turno.setDocente(docente);
        turno.setZona(zona);
        turno.setFecha(fecha);
        turno.setFranja(franja);
        turno.setHoraInicio(LocalTime.parse(inicio));
        turno.setHoraFin(LocalTime.parse(fin));
        turno.setEstado(estado);
        turno.setAbiertoEn(LocalDateTime.now().minusMinutes(30));
        return sistemaService.guardar(turno);
    }
}