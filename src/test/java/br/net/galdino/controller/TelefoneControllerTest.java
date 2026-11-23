package br.net.galdino.controller;

import br.net.galdino.dto.TelefoneDTO;
import br.net.galdino.enums.TipoTelefone;
import br.net.galdino.model.Telefone;
import br.net.galdino.service.TelefoneService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TelefoneController.class)
class TelefoneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TelefoneService telefoneService;

    @Autowired
    private ObjectMapper objectMapper;

    private TelefoneDTO telefoneDTO;
    private Telefone telefone;

    @BeforeEach
    void setUp() {
        telefoneDTO = new TelefoneDTO();
        telefoneDTO.setCodigoPais("55");
        telefoneDTO.setDdd("11");
        telefoneDTO.setNumero("999999999");
        telefoneDTO.setTipoTelefone(TipoTelefone.CELULAR);

        telefone = new Telefone();
        telefone.setId(1L);
        telefone.setCodigoPais("55");
        telefone.setDdd("11");
        telefone.setNumero("999999999");
        telefone.setTipoTelefone(TipoTelefone.CELULAR);
    }

    @Test
    void listarTodos_deveriaRetornarListaDeTelefones() throws Exception {
        when(telefoneService.listarTodos()).thenReturn(List.of(telefone));
        when(telefoneService.toDto(telefone)).thenReturn(telefoneDTO);

        mockMvc.perform(get("/api/telefones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numero").value("999999999"));
    }

    @Test
    void listarTodos_deveriaRetornarListaVaziaQuandoNaoExistirTelefones() throws Exception {
        when(telefoneService.listarTodos()).thenReturn(List.of());

        mockMvc.perform(get("/api/telefones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void buscarPorId_deveriaRetornarTelefoneQuandoExistir() throws Exception {
        when(telefoneService.buscarPorId(1L)).thenReturn(telefone);
        when(telefoneService.toDto(telefone)).thenReturn(telefoneDTO);

        mockMvc.perform(get("/api/telefones/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numero").value("999999999"));
    }

    @Test
    void buscarPorId_deveriaRetornarNotFoundQuandoNaoExistir() throws Exception {
        when(telefoneService.buscarPorId(999L)).thenThrow(new RuntimeException("Telefone não encontrado"));

        mockMvc.perform(get("/api/telefones/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void listarPorContato_deveriaRetornarTelefonesDoContato() throws Exception {
        when(telefoneService.listarPorContato(1L)).thenReturn(List.of(telefone));
        when(telefoneService.toDto(telefone)).thenReturn(telefoneDTO);

        mockMvc.perform(get("/api/telefones/contato/{contatoId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numero").value("999999999"));
    }

    @Test
    void salvar_deveriaCriarTelefoneERetornarStatus201() throws Exception {
        when(telefoneService.salvar(eq(1L), any(TelefoneDTO.class))).thenReturn(telefone);
        when(telefoneService.toDto(telefone)).thenReturn(telefoneDTO);

        mockMvc.perform(post("/api/telefones/contato/{contatoId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(telefoneDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numero").value("999999999"));
    }

    @Test
    void salvar_deveriaRetornarBadRequestQuandoCodigoPaisForVazio() throws Exception {
        TelefoneDTO dtoInvalido = new TelefoneDTO();
        dtoInvalido.setCodigoPais("");
        dtoInvalido.setDdd("11");
        dtoInvalido.setNumero("999999999");
        dtoInvalido.setTipoTelefone(TipoTelefone.CELULAR);

        mockMvc.perform(post("/api/telefones/contato/{contatoId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoInvalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void atualizar_deveriaAtualizarTelefoneExistente() throws Exception {
        when(telefoneService.atualizar(eq(1L), any(TelefoneDTO.class))).thenReturn(telefone);
        when(telefoneService.toDto(telefone)).thenReturn(telefoneDTO);

        mockMvc.perform(put("/api/telefones/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(telefoneDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numero").value("999999999"));
    }

    @Test
    void atualizar_deveriaRetornarNotFoundQuandoTelefoneNaoExistir() throws Exception {
        when(telefoneService.atualizar(eq(999L), any(TelefoneDTO.class)))
                .thenThrow(new RuntimeException("Telefone não encontrado"));

        mockMvc.perform(put("/api/telefones/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(telefoneDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void excluir_deveriaRemoverTelefoneERetornarStatus204() throws Exception {
        doNothing().when(telefoneService).excluir(1L);

        mockMvc.perform(delete("/api/telefones/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void excluir_deveriaRetornarNotFoundQuandoTelefoneNaoExistir() throws Exception {
        doThrow(new RuntimeException("Telefone não encontrado"))
                .when(telefoneService).excluir(999L);

        mockMvc.perform(delete("/api/telefones/{id}", 999L))
                .andExpect(status().isNotFound());
    }
}
