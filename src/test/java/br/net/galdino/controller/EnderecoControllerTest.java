package br.net.galdino.controller;

import br.net.galdino.dto.EnderecoDTO;
import br.net.galdino.enums.TipoEndereco;
import br.net.galdino.model.Endereco;
import br.net.galdino.service.EnderecoService;
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

@WebMvcTest(EnderecoController.class)
class EnderecoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EnderecoService enderecoService;

    @Autowired
    private ObjectMapper objectMapper;

    private EnderecoDTO enderecoDTO;
    private Endereco endereco;

    @BeforeEach
    void setUp() {
        enderecoDTO = new EnderecoDTO();
        enderecoDTO.setLogradouro("Rua das Flores");
        enderecoDTO.setNumero("123");
        enderecoDTO.setComplemento("Apto 10");
        enderecoDTO.setBairro("Centro");
        enderecoDTO.setCidade("São Paulo");
        enderecoDTO.setEstado("SP");
        enderecoDTO.setTipoEndereco(TipoEndereco.RESIDENCIAL);

        endereco = new Endereco();
        endereco.setId(1L);
        endereco.setLogradouro("Rua das Flores");
        endereco.setNumero("123");
        endereco.setComplemento("Apto 10");
        endereco.setBairro("Centro");
        endereco.setCidade("São Paulo");
        endereco.setEstado("SP");
        endereco.setTipoEndereco(TipoEndereco.RESIDENCIAL);
    }

    @Test
    void listarTodos_deveriaRetornarListaDeEnderecos() throws Exception {
        when(enderecoService.listarTodos()).thenReturn(List.of(endereco));
        when(enderecoService.toDto(endereco)).thenReturn(enderecoDTO);

        mockMvc.perform(get("/api/enderecos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].logradouro").value("Rua das Flores"));
    }

    @Test
    void listarTodos_deveriaRetornarListaVaziaQuandoNaoExistirEnderecos() throws Exception {
        when(enderecoService.listarTodos()).thenReturn(List.of());

        mockMvc.perform(get("/api/enderecos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void buscarPorId_deveriaRetornarEnderecoQuandoExistir() throws Exception {
        when(enderecoService.buscarPorId(1L)).thenReturn(endereco);
        when(enderecoService.toDto(endereco)).thenReturn(enderecoDTO);

        mockMvc.perform(get("/api/enderecos/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.logradouro").value("Rua das Flores"));
    }

    @Test
    void buscarPorId_deveriaRetornarNotFoundQuandoNaoExistir() throws Exception {
        when(enderecoService.buscarPorId(999L)).thenThrow(new RuntimeException("Endereço não encontrado"));

        mockMvc.perform(get("/api/enderecos/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void listarPorContato_deveriaRetornarEnderecosDoContato() throws Exception {
        when(enderecoService.listarPorContato(1L)).thenReturn(List.of(endereco));
        when(enderecoService.toDto(endereco)).thenReturn(enderecoDTO);

        mockMvc.perform(get("/api/enderecos/contato/{contatoId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].logradouro").value("Rua das Flores"));
    }

    @Test
    void salvar_deveriaCriarEnderecoERetornarStatus201() throws Exception {
        when(enderecoService.salvar(eq(1L), any(EnderecoDTO.class))).thenReturn(endereco);
        when(enderecoService.toDto(endereco)).thenReturn(enderecoDTO);

        mockMvc.perform(post("/api/enderecos/contato/{contatoId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(enderecoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.logradouro").value("Rua das Flores"));
    }

    @Test
    void salvar_deveriaRetornarBadRequestQuandoLogradouroForVazio() throws Exception {
        EnderecoDTO dtoInvalido = new EnderecoDTO();
        dtoInvalido.setLogradouro("");
        dtoInvalido.setNumero("123");
        dtoInvalido.setBairro("Centro");
        dtoInvalido.setCidade("São Paulo");
        dtoInvalido.setEstado("SP");
        dtoInvalido.setTipoEndereco(TipoEndereco.RESIDENCIAL);

        mockMvc.perform(post("/api/enderecos/contato/{contatoId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoInvalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void atualizar_deveriaAtualizarEnderecoExistente() throws Exception {
        when(enderecoService.atualizar(eq(1L), any(EnderecoDTO.class))).thenReturn(endereco);
        when(enderecoService.toDto(endereco)).thenReturn(enderecoDTO);

        mockMvc.perform(put("/api/enderecos/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(enderecoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.logradouro").value("Rua das Flores"));
    }

    @Test
    void atualizar_deveriaRetornarNotFoundQuandoEnderecoNaoExistir() throws Exception {
        when(enderecoService.atualizar(eq(999L), any(EnderecoDTO.class)))
                .thenThrow(new RuntimeException("Endereço não encontrado"));

        mockMvc.perform(put("/api/enderecos/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(enderecoDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void excluir_deveriaRemoverEnderecoERetornarStatus204() throws Exception {
        doNothing().when(enderecoService).excluir(1L);

        mockMvc.perform(delete("/api/enderecos/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void excluir_deveriaRetornarNotFoundQuandoEnderecoNaoExistir() throws Exception {
        doThrow(new RuntimeException("Endereço não encontrado"))
                .when(enderecoService).excluir(999L);

        mockMvc.perform(delete("/api/enderecos/{id}", 999L))
                .andExpect(status().isNotFound());
    }
}
