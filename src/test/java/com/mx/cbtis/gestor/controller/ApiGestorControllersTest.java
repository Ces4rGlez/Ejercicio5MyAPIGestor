package com.mx.cbtis.gestor.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mx.cbtis.gestor.modelo.Asignaciones;
import com.mx.cbtis.gestor.modelo.Comentarios;
import com.mx.cbtis.gestor.modelo.Proyectos;
import com.mx.cbtis.gestor.modelo.Tareas;
import com.mx.cbtis.gestor.modelo.Usuario;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ApiGestorControllersTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private com.mx.cbtis.gestor.repository.UsuarioRepository usuarioRepository;

    @Autowired
    private com.mx.cbtis.gestor.repository.ProtectosRepository proyectosRepository;

    @Autowired
    private com.mx.cbtis.gestor.repository.TareasRepository tareasRepository;

    @Autowired
    private com.mx.cbtis.gestor.repository.AsignacionesRepository asignacionesRepository;

    @Autowired
    private com.mx.cbtis.gestor.repository.ComentariosRepository comentariosRepository;

    // ==========================================
    // USUARIO CONTROLLER TESTS
    // ==========================================

    @Test
    public void testUsuarioController_CrudFlow() throws Exception {
        Usuario u = new Usuario();
        u.setId_usuario(100);
        u.setNombre("Mario");
        u.setCorreo_electronico("mario@mail.com");
        u.setContrasena("secret123");
        u.setRol("DEV");
        u.setImagen_usuario("mario.png");

        // 1. addUsuario
        mockMvc.perform(post("/addUsuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(u)))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        // Guardar entidad para buscar por id existente
        Usuario savedUser = usuarioRepository.save(new Usuario(null, "Mario", "mario_buscar@mail.com", "pass_buscar", "DEV", "mario.png"));

        // 2. buscarUsuarios
        mockMvc.perform(post("/buscarUsuarios"))
                .andExpect(status().isOk());

        // 3. buscarUsuario por id existente
        mockMvc.perform(get("/buscarUsuario").param("id", String.valueOf(savedUser.getId_usuario())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Mario"));

        // 4. buscarUsuario inexistente
        mockMvc.perform(get("/buscarUsuario").param("id", "9999"))
                .andExpect(status().isOk());

        // 5. getUsuarioLogin exitoso
        Usuario loginParam = new Usuario();
        loginParam.setCorreo_electronico("mario@mail.com");
        loginParam.setContrasena("secret123");
        mockMvc.perform(post("/getUsuarioLogin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginParam)))
                .andExpect(status().isOk());

        // 6. getUsuarioLogin fallido (retorna nombre "000")
        Usuario loginFail = new Usuario();
        loginFail.setCorreo_electronico("noexiste@mail.com");
        loginFail.setContrasena("bad");
        mockMvc.perform(post("/getUsuarioLogin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginFail)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("000"));

        // 7. updateUsuario
        u.setNombre("Mario Actualizado");
        mockMvc.perform(post("/updateUsuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(u)))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        // 8. deleteUsuario
        mockMvc.perform(delete("/deleteUsuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(u)))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    // ==========================================
    // PROYECTOS CONTROLLER TESTS
    // ==========================================

    @Test
    public void testProyectosController_CrudFlow() throws Exception {
        Proyectos p = new Proyectos();
        p.setId_proyecto(200);
        p.setNombre("Portal CI/CD");
        p.setDescripcion("Sistema de pipelines");
        p.setFecha_inicio("2026-01-01");
        p.setFecha_finalizacion("2026-12-31");
        p.setEstado_proyecto("ACTIVO");
        p.setFondo("#ffffff");

        // 1. addProyecto
        mockMvc.perform(post("/addProyecto")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(p)))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        // Guardar entidad para buscar por id existente
        Proyectos savedP = proyectosRepository.save(new Proyectos(null, "Portal CI/CD", "Sistema de pipelines", "2026-01-01", "2026-12-31", "ACTIVO", "#ffffff"));

        // 2. buscarProyectos
        mockMvc.perform(post("/buscarProyectos"))
                .andExpect(status().isOk());

        // 3. buscarProyecto por id
        mockMvc.perform(get("/buscarProyecto").param("id_proyecto", String.valueOf(savedP.getId_proyecto())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Portal CI/CD"));

        // 4. buscarProyecto inexistente
        mockMvc.perform(get("/buscarProyecto").param("id_proyecto", "9999"))
                .andExpect(status().isOk());

        // 5. updateProyecto
        p.setNombre("Portal CI/CD v2");
        mockMvc.perform(post("/updateProyecto")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(p)))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        // 6. deleteProyecto
        mockMvc.perform(delete("/deleteProyecto")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(p)))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    // ==========================================
    // TAREAS CONTROLLER TESTS
    // ==========================================

    @Test
    public void testTareasController_CrudFlow() throws Exception {
        Tareas t = new Tareas();
        t.setId_tarea(300);
        t.setNombre("Desplegar Microservicios");
        t.setDescripcion("Despliegue automatizado");
        t.setFecha_vencimiento("2026-06-30");
        t.setLista("Backlog");
        t.setEstado("EN_PROCESO");
        t.setId_proyecto(200);

        // 1. addTareas
        mockMvc.perform(post("/addTareas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(t)))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        // Guardar entidad para buscar por id existente
        Tareas savedT = tareasRepository.save(new Tareas(null, "Desplegar Microservicios", "Despliegue automatizado", "2026-06-30", "Backlog", "EN_PROCESO", 200));

        // 2. buscarTareas
        mockMvc.perform(post("/buscarTareas"))
                .andExpect(status().isOk());

        // 3. buscarTarea por id
        mockMvc.perform(get("/buscarTarea").param("id_tarea", String.valueOf(savedT.getId_tarea())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Desplegar Microservicios"));

        // 4. buscarTarea inexistente
        mockMvc.perform(get("/buscarTarea").param("id_tarea", "9999"))
                .andExpect(status().isOk());

        // 5. getTarea (por id_proyecto)
        Tareas param = new Tareas();
        param.setId_proyecto(200);
        mockMvc.perform(post("/getTarea")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(param)))
                .andExpect(status().isOk());

        // 6. updateTareas
        t.setEstado("COMPLETADA");
        mockMvc.perform(post("/updateTareas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(t)))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        // 7. deleteTareas
        mockMvc.perform(delete("/deleteTareas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(t)))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    // ==========================================
    // ASIGNACIONES CONTROLLER TESTS
    // ==========================================

    @Test
    public void testAsignacionesController_CrudFlow() throws Exception {
        Asignaciones a = new Asignaciones();
        a.setId_asignacion(400);
        a.setId_usuario(100);
        a.setId_tarea(300);
        a.setFecha_asignacion("2026-04-15");

        // 1. addAsignacion
        mockMvc.perform(post("/addAsignacion")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(a)))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        // Guardar entidad para buscar por id existente
        Asignaciones savedA = asignacionesRepository.save(new Asignaciones(null, 100, 300, "2026-04-15"));

        // 2. buscarAsignaciones
        mockMvc.perform(post("/buscarAsignaciones"))
                .andExpect(status().isOk());

        // 3. buscarAsignacion por id
        mockMvc.perform(get("/buscarAsignacion").param("id", String.valueOf(savedA.getId_asignacion())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_usuario").value(100));

        // 4. buscarAsignacion inexistente
        mockMvc.perform(get("/buscarAsignacion").param("id", "9999"))
                .andExpect(status().isOk());

        // 5. updateAsignacion
        mockMvc.perform(post("/updateAsignacion")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(a)))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        // 6. deleteAsignacion
        mockMvc.perform(delete("/deleteAsignacion")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(a)))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    // ==========================================
    // COMENTARIOS CONTROLLER TESTS
    // ==========================================

    @Test
    public void testComentariosController_CrudFlow() throws Exception {
        Comentarios c = new Comentarios();
        c.setId_comentario(500);
        c.setId_usuario("100");
        c.setId_tarea(300);
        c.setTexto_comentario("Comentario de prueba QA");
        c.setFecha_hora("2026-04-16");

        // 1. addComentario
        mockMvc.perform(post("/addComentario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(c)))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        // Guardar entidad para buscar por id existente
        Comentarios savedC = comentariosRepository.save(new Comentarios(null, "100", 300, "Comentario de prueba QA", "2026-04-16"));

        // 2. buscarComentarios
        mockMvc.perform(post("/buscarComentarios"))
                .andExpect(status().isOk());

        // 3. buscarComentario por id
        mockMvc.perform(get("/buscarComentario").param("id", String.valueOf(savedC.getId_comentario())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.texto_comentario").value("Comentario de prueba QA"));

        // 4. buscarComentario inexistente
        mockMvc.perform(get("/buscarComentario").param("id", "9999"))
                .andExpect(status().isOk());

        // 5. getComentario (por id_tarea)
        Comentarios param = new Comentarios();
        param.setId_tarea(300);
        mockMvc.perform(post("/getComentario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(param)))
                .andExpect(status().isOk());

        // 6. updateComentario
        c.setTexto_comentario("Comentario actualizado");
        mockMvc.perform(post("/updateComentario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(c)))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        // 7. deleteComentario
        mockMvc.perform(delete("/deleteComentario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(c)))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }
}
