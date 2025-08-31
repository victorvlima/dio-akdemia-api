package br.com.akdemia.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.akdemia.api.dto.AlunoDTO;
import br.com.akdemia.api.enums.TipoUsuario;
import br.com.akdemia.api.service.AlunoService;

@WebMvcTest(AlunoController.class)
@DisplayName("Testes do AlunoController")
class AlunoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlunoService alunoService;

    @Autowired
    private ObjectMapper objectMapper;

    private AlunoDTO alunoDTO;
    private List<AlunoDTO> listaAlunos;

    @BeforeEach
    void setUp() {
        alunoDTO = new AlunoDTO();
        alunoDTO.setId(1L);
        alunoDTO.setNome("João Silva");
        alunoDTO.setEmail("joao@email.com");
        alunoDTO.setTipo(TipoUsuario.ALUNO);

        AlunoDTO aluno2 = new AlunoDTO();
        aluno2.setId(2L);
        aluno2.setNome("Maria Santos");
        aluno2.setEmail("maria@email.com");
        aluno2.setTipo(TipoUsuario.ALUNO);

        listaAlunos = Arrays.asList(alunoDTO, aluno2);
    }

    @Test
    @DisplayName("Deve criar um novo aluno com sucesso")
    void deveCriarNovoAlunoComSucesso() throws Exception {
        // Given
        when(alunoService.criar(any(AlunoDTO.class))).thenReturn(alunoDTO);

        // When & Then
        mockMvc.perform(post("/alunos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(alunoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("João Silva"))
                .andExpect(jsonPath("$.email").value("joao@email.com"));
    }

    @Test
    @DisplayName("Deve retornar erro 400 ao criar aluno com dados inválidos")
    void deveRetornarErro400AoCriarAlunoComDadosInvalidos() throws Exception {
        // Given
        AlunoDTO alunoInvalido = new AlunoDTO();
        // DTO sem dados obrigatórios

        // When & Then
        mockMvc.perform(post("/alunos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(alunoInvalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve listar todos os alunos com sucesso")
    void deveListarTodosOsAlunosComSucesso() throws Exception {
        // Given
        when(alunoService.listarTodos()).thenReturn(listaAlunos);

        // When & Then
        mockMvc.perform(get("/alunos/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nome").value("João Silva"))
                .andExpect(jsonPath("$[1].nome").value("Maria Santos"));
    }

    @Test
    @DisplayName("Deve buscar aluno por ID com sucesso")
    void deveBuscarAlunoPorIdComSucesso() throws Exception {
        // Given
        when(alunoService.buscarPorId(1L)).thenReturn(alunoDTO);

        // When & Then
        mockMvc.perform(get("/alunos/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("João Silva"))
                .andExpect(jsonPath("$.email").value("joao@email.com"));
    }

    @Test
    @DisplayName("Deve listar alunos por tipo com sucesso")
    void deveListarAlunosPorTipoComSucesso() throws Exception {
        // Given
        when(alunoService.listarPorTipo(TipoUsuario.ALUNO)).thenReturn(listaAlunos);

        // When & Then
        mockMvc.perform(get("/alunos/tipo/{tipo}", TipoUsuario.ALUNO))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("Deve atualizar aluno com sucesso")
    void deveAtualizarAlunoComSucesso() throws Exception {
        // Given
        AlunoDTO alunoAtualizado = new AlunoDTO();
        alunoAtualizado.setId(1L);
        alunoAtualizado.setNome("João Silva Atualizado");
        alunoAtualizado.setEmail("joao.atualizado@email.com");

        when(alunoService.atualizar(eq(1L), any(AlunoDTO.class))).thenReturn(alunoAtualizado);

        // When & Then
        mockMvc.perform(put("/alunos/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(alunoAtualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("João Silva Atualizado"))
                .andExpect(jsonPath("$.email").value("joao.atualizado@email.com"));
    }

    @Test
    @DisplayName("Deve retornar erro 400 ao atualizar aluno com dados inválidos")
    void deveRetornarErro400AoAtualizarAlunoComDadosInvalidos() throws Exception {
        // Given
        AlunoDTO alunoInvalido = new AlunoDTO();
        // DTO sem dados obrigatórios

        // When & Then
        mockMvc.perform(put("/alunos/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(alunoInvalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve desativar aluno com sucesso")
    void deveDesativarAlunoComSucesso() throws Exception {
        // Given
        doNothing().when(alunoService).desativar(1L);

        // When & Then
        mockMvc.perform(delete("/alunos/{id}", 1L))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

    @Test
    @DisplayName("Deve buscar alunos por nome com sucesso")
    void deveBuscarAlunosPorNomeComSucesso() throws Exception {
        // Given
        String nomeBusca = "João";
        List<AlunoDTO> alunosEncontrados = Arrays.asList(alunoDTO);
        when(alunoService.buscarPorNome(nomeBusca)).thenReturn(alunosEncontrados);

        // When & Then
        mockMvc.perform(get("/alunos/buscar")
                .param("nome", nomeBusca))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nome").value("João Silva"));
    }

    @Test
    @DisplayName("Deve retornar lista vazia ao buscar por nome inexistente")
    void deveRetornarListaVaziaAoBuscarPorNomeInexistente() throws Exception {
        // Given
        String nomeBusca = "Inexistente";
        when(alunoService.buscarPorNome(nomeBusca)).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/alunos/buscar")
                .param("nome", nomeBusca))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("Deve retornar erro 400 ao buscar sem parâmetro nome")
    void deveRetornarErro400AoBuscarSemParametroNome() throws Exception {
        // When & Then
        mockMvc.perform(get("/alunos/buscar"))
                .andExpect(status().isBadRequest());
    }
}