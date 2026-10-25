package com.mx.cbtis.gestor.modelo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ModelosBackendTest {

    @Test
    public void testUsuario() {
        Usuario u1 = new Usuario();
        u1.setId_usuario(1);
        u1.setNombre("Fernando");
        u1.setCorreo_electronico("fer@mail.com");
        u1.setContrasena("secret");
        u1.setRol("ADMIN");
        u1.setImagen_usuario("avatar.jpg");

        assertEquals(1, u1.getId_usuario());
        assertEquals("Fernando", u1.getNombre());
        assertEquals("fer@mail.com", u1.getCorreo_electronico());
        assertEquals("secret", u1.getContrasena());
        assertEquals("ADMIN", u1.getRol());
        assertEquals("avatar.jpg", u1.getImagen_usuario());

        Usuario u2 = new Usuario(2, "Luis", "luis@mail.com", "pass", "DEV", "luis.png");
        assertEquals(2, u2.getId_usuario());
        assertEquals("Luis", u2.getNombre());
    }

    @Test
    public void testProyectos() {
        Proyectos p1 = new Proyectos();
        p1.setId_proyecto(10);
        p1.setNombre("Proyecto Alpha");
        p1.setDescripcion("Descripcion de prueba");
        p1.setFecha_inicio("2026-01-01");
        p1.setFecha_finalizacion("2026-12-31");
        p1.setEstado_proyecto("ACTIVO");
        p1.setFondo("#ffffff");

        assertEquals(10, p1.getId_proyecto());
        assertEquals("Proyecto Alpha", p1.getNombre());
        assertEquals("Descripcion de prueba", p1.getDescripcion());
        assertEquals("2026-01-01", p1.getFecha_inicio());
        assertEquals("2026-12-31", p1.getFecha_finalizacion());
        assertEquals("ACTIVO", p1.getEstado_proyecto());
        assertEquals("#ffffff", p1.getFondo());

        Proyectos p2 = new Proyectos(11, "Proyecto Beta", "Desc Beta", "2026-02-01", "2026-06-30", "PENDIENTE", "#000000");
        assertEquals(11, p2.getId_proyecto());
        assertEquals("Proyecto Beta", p2.getNombre());
    }

    @Test
    public void testTareas() {
        Tareas t1 = new Tareas();
        t1.setId_tarea(100);
        t1.setNombre("Configurar CI/CD");
        t1.setDescripcion("Pipeline en Jenkins");
        t1.setFecha_vencimiento("2026-05-01");
        t1.setLista("Pendiente");
        t1.setEstado("EN_PROGRESO");
        t1.setId_proyecto(10);

        assertEquals(100, t1.getId_tarea());
        assertEquals("Configurar CI/CD", t1.getNombre());
        assertEquals("Pipeline en Jenkins", t1.getDescripcion());
        assertEquals("2026-05-01", t1.getFecha_vencimiento());
        assertEquals("Pendiente", t1.getLista());
        assertEquals("EN_PROGRESO", t1.getEstado());
        assertEquals(10, t1.getId_proyecto());

        Tareas t2 = new Tareas(101, "SonarScanner", "Configurar analisis", "2026-05-02", "Done", "LISTO", 10);
        assertEquals(101, t2.getId_tarea());
        assertEquals("SonarScanner", t2.getNombre());
    }

    @Test
    public void testAsignaciones() {
        Asignaciones a1 = new Asignaciones();
        a1.setId_asignacion(1);
        a1.setId_usuario(1);
        a1.setId_tarea(100);
        a1.setFecha_asignacion("2026-04-01");

        assertEquals(1, a1.getId_asignacion());
        assertEquals(1, a1.getId_usuario());
        assertEquals(100, a1.getId_tarea());
        assertEquals("2026-04-01", a1.getFecha_asignacion());

        Asignaciones a2 = new Asignaciones(2, 2, 101, "2026-04-02");
        assertEquals(2, a2.getId_asignacion());
        assertEquals(2, a2.getId_usuario());
    }

    @Test
    public void testComentarios() {
        Comentarios c1 = new Comentarios();
        c1.setId_comentario(500);
        c1.setId_usuario("1");
        c1.setId_tarea(100);
        c1.setTexto_comentario("Tarea finalizada con exito");
        c1.setFecha_hora("2026-04-20");

        assertEquals(500, c1.getId_comentario());
        assertEquals("1", c1.getId_usuario());
        assertEquals(100, c1.getId_tarea());
        assertEquals("Tarea finalizada con exito", c1.getTexto_comentario());
        assertEquals("2026-04-20", c1.getFecha_hora());

        Comentarios c2 = new Comentarios(501, "2", 101, "Revisando PR", "2026-04-21");
        assertEquals(501, c2.getId_comentario());
        assertEquals("Revisando PR", c2.getTexto_comentario());
    }
}
