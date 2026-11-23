package br.net.galdino.service;

import br.net.galdino.dto.EmailDTO;
import br.net.galdino.enums.TipoEmail;
import br.net.galdino.model.Contato;
import br.net.galdino.model.Email;
import br.net.galdino.repository.ContatoRepository;
import br.net.galdino.repository.EmailRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private EmailRepository emailRepository;

    @Mock
    private ContatoRepository contatoRepository;

    @InjectMocks
    private EmailService emailService;

    private EmailDTO emailDTO;
    private Email email;
    private Contato contato;

    @BeforeEach
    void setUp() {
        contato = new Contato();
        contato.setId(1L);
        contato.setNome("João Silva");

        emailDTO = new EmailDTO();
        emailDTO.setEmail("joao@email.com");
        emailDTO.setTipoEmail(TipoEmail.PESSOAL);

        email = new Email();
        email.setId(1L);
        email.setEmail("joao@email.com");
        email.setTipoEmail(TipoEmail.PESSOAL);
    }

    @Test
    void listarTodos_deveriaRetornarListaDeEmails() {
        List<Email> emails = Arrays.asList(email, criarEmailComEndereco("joao@trabalho.com"));
        when(emailRepository.findAll()).thenReturn(emails);

        List<Email> resultado = emailService.listarTodos();

        assertEquals(2, resultado.size());
        verify(emailRepository).findAll();
    }

    @Test
    void listarTodos_deveriaRetornarListaVaziaQuandoNaoExistirEmails() {
        when(emailRepository.findAll()).thenReturn(List.of());

        List<Email> resultado = emailService.listarTodos();

        assertTrue(resultado.isEmpty());
    }

    @Test
    void listarPorContato_deveriaRetornarEmailsDoContato() {
        when(emailRepository.findByContatoId(1L)).thenReturn(List.of(email));

        List<Email> resultado = emailService.listarPorContato(1L);

        assertEquals(1, resultado.size());
    }

    @Test
    void buscarPorId_deveriaRetornarEmailQuandoExistir() {
        when(emailRepository.findById(1L)).thenReturn(Optional.of(email));

        Email resultado = emailService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals("joao@email.com", resultado.getEmail());
    }

    @Test
    void buscarPorId_deveriaLancarExcecaoQuandoNaoExistir() {
        when(emailRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> emailService.buscarPorId(999L));
    }

    @Test
    void salvar_deveriaCriarEmailParaContatoExistente() {
        when(contatoRepository.findById(1L)).thenReturn(Optional.of(contato));
        when(contatoRepository.save(any(Contato.class))).thenReturn(contato);

        Email resultado = emailService.salvar(1L, emailDTO);

        assertNotNull(resultado);
        assertEquals("joao@email.com", resultado.getEmail());
        verify(contatoRepository).save(any(Contato.class));
    }

    @Test
    void salvar_deveriaLancarExcecaoQuandoContatoNaoExistir() {
        when(contatoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> emailService.salvar(999L, emailDTO));
    }

    @Test
    void atualizar_deveriaAtualizarEmailExistente() {
        when(emailRepository.findById(1L)).thenReturn(Optional.of(email));
        when(emailRepository.save(any(Email.class))).thenReturn(email);

        EmailDTO updateDTO = new EmailDTO();
        updateDTO.setEmail("novo@email.com");
        updateDTO.setTipoEmail(TipoEmail.TRABALHO);

        Email resultado = emailService.atualizar(1L, updateDTO);

        assertNotNull(resultado);
    }

    @Test
    void atualizar_deveriaLancarExcecaoQuandoEmailNaoExistir() {
        when(emailRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> emailService.atualizar(999L, emailDTO));
    }

    @Test
    void excluir_deveriaRemoverEmailQuandoExistir() {
        when(emailRepository.existsById(1L)).thenReturn(true);

        emailService.excluir(1L);

        verify(emailRepository).deleteById(1L);
    }

    @Test
    void excluir_deveriaLancarExcecaoQuandoEmailNaoExistir() {
        when(emailRepository.existsById(999L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> emailService.excluir(999L));
    }

    @Test
    void toDto_deveriaConverterEmailParaDTO() {
        EmailDTO dto = emailService.toDto(email);

        assertNotNull(dto);
        assertEquals(email.getId(), dto.getId());
        assertEquals(email.getEmail(), dto.getEmail());
        assertEquals(email.getTipoEmail(), dto.getTipoEmail());
    }

    private Email criarEmailComEndereco(String enderecoEmail) {
        Email e = new Email();
        e.setId(2L);
        e.setEmail(enderecoEmail);
        e.setTipoEmail(TipoEmail.TRABALHO);
        return e;
    }
}
