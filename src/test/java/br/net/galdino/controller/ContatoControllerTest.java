package br.net.galdino.controller;

import br.net.galdino.dto.ContatoDTO;
import br.net.galdino.dto.EnderecoDTO;
import br.net.galdino.dto.EmailDTO;
import br.net.galdino.dto.TelefoneDTO;
import br.net.galdino.enums.TipoEmail;
import br.net.galdino.enums.TipoEndereco;
import br.net.galdino.enums.TipoTelefone;
import br.net.galdino.model.Contato;
import br.net.galdino.service.ContatoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContatoController.class)
class ContatoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ContatoService contatoService;

    @Autowired
    private ObjectMapper objectMapper;

    private ContatoDTO contatoDTO;
    private Contato contato;

    @BeforeEach
    void setUp() {
        contatoDTO = criarContatoDTO();
        contato = criarContato();
    }

    @Test
    void listarTodos_deveriaRetornarListaDeContatos() throws Exception {
        when(contatoService.listarTodos()).thenReturn(List.of(contato));
        when(contatoService.toDto(contato)).thenReturn(contatoDTO);

        mockMvc.perform(get("/api/contatos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("João Silva"));

        verify(contatoService).listarTodos();
    }

    @Test
    void listarTodos_deveriaRetornarListaVaziaQuandoNaoExistirContatos() throws Exception {
        when(contatoService.listarTodos()).thenReturn(List.of());

        mockMvc.perform(get("/api/contatos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void buscarPorId_deveriaRetornarContatoQuandoExistir() throws Exception {
        when(contatoService.buscarPorId(1L)).thenReturn(Optional.of(contato));
        when(contatoService.toDto(contato)).thenReturn(contatoDTO);

        mockMvc.perform(get("/api/contatos/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João Silva"));
    }

    @Test
    void buscarPorId_deveriaRetornarNotFoundQuandoNaoExistir() throws Exception {
        when(contatoService.buscarPorId(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/contatos/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void buscarPorNome_deveriaRetornarContatosQueCorrespondem() throws Exception {
        when(contatoService.buscarPorNome("João")).thenReturn(List.of(contato));
        when(contatoService.toDto(contato)).thenReturn(contatoDTO);

        mockMvc.perform(get("/api/contatos/pesquisa").param("nome", "João"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("João Silva"));
    }

    @Test
    void salvar_deveriaCriarContatoERetornarStatus201() throws Exception {
        when(contatoService.salvar(any(ContatoDTO.class))).thenReturn(contato);
        when(contatoService.toDto(contato)).thenReturn(contatoDTO);

        mockMvc.perform(post("/api/contatos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contatoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("João Silva"));

        verify(contatoService).salvar(any(ContatoDTO.class));
    }

    @Test
    void salvar_deveriaRetornarBadRequestQuandoNomeForVazio() throws Exception {
        ContatoDTO dtoInvalido = new ContatoDTO();
        dtoInvalido.setNome("");

        mockMvc.perform(post("/api/contatos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoInvalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void atualizar_deveriaAtualizarContatoExistente() throws Exception {
        when(contatoService.atualizar(eq(1L), any(ContatoDTO.class))).thenReturn(contato);
        when(contatoService.toDto(contato)).thenReturn(contatoDTO);

        mockMvc.perform(put("/api/contatos/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contatoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João Silva"));

        verify(contatoService).atualizar(eq(1L), any(ContatoDTO.class));
    }

    @Test
    void atualizar_deveriaRetornarNotFoundQuandoContatoNaoExistir() throws Exception {
        when(contatoService.atualizar(eq(999L), any(ContatoDTO.class)))
                .thenThrow(new RuntimeException("Contato não encontrado com id: 999"));

        mockMvc.perform(put("/api/contatos/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contatoDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void excluir_deveriaRemoverContatoERetornarStatus204() throws Exception {
        doNothing().when(contatoService).excluir(1L);

        mockMvc.perform(delete("/api/contatos/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(contatoService).excluir(1L);
    }

    @Test
    void excluir_deveriaRetornarNotFoundQuandoContatoNaoExistir() throws Exception {
        doThrow(new RuntimeException("Contato não encontrado com id: 999"))
                .when(contatoService).excluir(999L);

        mockMvc.perform(delete("/api/contatos/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    // --- Helpers ---

    private ContatoDTO criarContatoDTO() {
        ContatoDTO dto = new ContatoDTO();
        dto.setNome("João Silva");

        EnderecoDTO enderecoDTO = new EnderecoDTO();
        enderecoDTO.setLogradouro("Rua das Flores");
        enderecoDTO.setNumero("123");
        enderecoDTO.setBairro("Centro");
        enderecoDTO.setCidade("São Paulo");
        enderecoDTO.setEstado("SP");
        enderecoDTO.setTipoEndereco(TipoEndereco.RESIDENCIAL);

        TelefoneDTO telefoneDTO = new TelefoneDTO();
        telefoneDTO.setCodigoPais("55");
        telefoneDTO.setDdd("11");
        telefoneDTO.setNumero("999999999");
        telefoneDTO.setTipoTelefone(TipoTelefone.CELULAR);

        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setEmail("joao@email.com");
        emailDTO.setTipoEmail(TipoEmail.PESSOAL);

        dto.setEnderecos(List.of(enderecoDTO));
        dto.setTelefones(List.of(telefoneDTO));
        dto.setEmails(List.of(emailDTO));
        return dto;
    }

    private Contato criarContato() {
        Contato c = new Contato();
        c.setId(1L);
        c.setNome("João Silva");
        return c;
    }
}
