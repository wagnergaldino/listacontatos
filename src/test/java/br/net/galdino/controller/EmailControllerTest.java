package br.net.galdino.controller;

import br.net.galdino.dto.EmailDTO;
import br.net.galdino.enums.TipoEmail;
import br.net.galdino.model.Email;
import br.net.galdino.service.EmailService;
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

@WebMvcTest(EmailController.class)
class EmailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmailService emailService;

    @Autowired
    private ObjectMapper objectMapper;

    private EmailDTO emailDTO;
    private Email email;

    @BeforeEach
    void setUp() {
        emailDTO = new EmailDTO();
        emailDTO.setEmail("joao@email.com");
        emailDTO.setTipoEmail(TipoEmail.PESSOAL);

        email = new Email();
        email.setId(1L);
        email.setEmail("joao@email.com");
        email.setTipoEmail(TipoEmail.PESSOAL);
    }

    @Test
    void listarTodos_deveriaRetornarListaDeEmails() throws Exception {
        when(emailService.listarTodos()).thenReturn(List.of(email));
        when(emailService.toDto(email)).thenReturn(emailDTO);

        mockMvc.perform(get("/api/emails"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("joao@email.com"));
    }

    @Test
    void listarTodos_deveriaRetornarListaVaziaQuandoNaoExistirEmails() throws Exception {
        when(emailService.listarTodos()).thenReturn(List.of());

        mockMvc.perform(get("/api/emails"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void buscarPorId_deveriaRetornarEmailQuandoExistir() throws Exception {
        when(emailService.buscarPorId(1L)).thenReturn(email);
        when(emailService.toDto(email)).thenReturn(emailDTO);

        mockMvc.perform(get("/api/emails/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("joao@email.com"));
    }

    @Test
    void buscarPorId_deveriaRetornarNotFoundQuandoNaoExistir() throws Exception {
        when(emailService.buscarPorId(999L)).thenThrow(new RuntimeException("Email não encontrado"));

        mockMvc.perform(get("/api/emails/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void listarPorContato_deveriaRetornarEmailsDoContato() throws Exception {
        when(emailService.listarPorContato(1L)).thenReturn(List.of(email));
        when(emailService.toDto(email)).thenReturn(emailDTO);

        mockMvc.perform(get("/api/emails/contato/{contatoId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("joao@email.com"));
    }

    @Test
    void salvar_deveriaCriarEmailERetornarStatus201() throws Exception {
        when(emailService.salvar(eq(1L), any(EmailDTO.class))).thenReturn(email);
        when(emailService.toDto(email)).thenReturn(emailDTO);

        mockMvc.perform(post("/api/emails/contato/{contatoId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("joao@email.com"));
    }

    @Test
    void salvar_deveriaRetornarBadRequestQuandoEmailForInvalido() throws Exception {
        EmailDTO dtoInvalido = new EmailDTO();
        dtoInvalido.setEmail("");
        dtoInvalido.setTipoEmail(TipoEmail.PESSOAL);

        mockMvc.perform(post("/api/emails/contato/{contatoId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoInvalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void atualizar_deveriaAtualizarEmailExistente() throws Exception {
        when(emailService.atualizar(eq(1L), any(EmailDTO.class))).thenReturn(email);
        when(emailService.toDto(email)).thenReturn(emailDTO);

        mockMvc.perform(put("/api/emails/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("joao@email.com"));
    }

    @Test
    void atualizar_deveriaRetornarNotFoundQuandoEmailNaoExistir() throws Exception {
        when(emailService.atualizar(eq(999L), any(EmailDTO.class)))
                .thenThrow(new RuntimeException("Email não encontrado"));

        mockMvc.perform(put("/api/emails/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void excluir_deveriaRemoverEmailERetornarStatus204() throws Exception {
        doNothing().when(emailService).excluir(1L);

        mockMvc.perform(delete("/api/emails/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void excluir_deveriaRetornarNotFoundQuandoEmailNaoExistir() throws Exception {
        doThrow(new RuntimeException("Email não encontrado"))
                .when(emailService).excluir(999L);

        mockMvc.perform(delete("/api/emails/{id}", 999L))
                .andExpect(status().isNotFound());
    }
}
