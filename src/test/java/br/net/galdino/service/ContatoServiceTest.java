package br.net.galdino.service;

import br.net.galdino.dto.ContatoDTO;
import br.net.galdino.dto.EnderecoDTO;
import br.net.galdino.dto.EmailDTO;
import br.net.galdino.dto.TelefoneDTO;
import br.net.galdino.enums.TipoEmail;
import br.net.galdino.enums.TipoEndereco;
import br.net.galdino.enums.TipoTelefone;
import br.net.galdino.model.Contato;
import br.net.galdino.repository.ContatoRepository;
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
class ContatoServiceTest {

    @Mock
    private ContatoRepository contatoRepository;

    @InjectMocks
    private ContatoService contatoService;

    private ContatoDTO contatoDTO;
    private Contato contato;

    @BeforeEach
    void setUp() {
        contatoDTO = criarContatoDTO();
        contato = criarContato();
    }

    @Test
    void listarTodos_deveriaRetornarListaDeContatos() {
        List<Contato> contatos = Arrays.asList(criarContato(), criarContatoComNome("Outro"));
        when(contatoRepository.findAll()).thenReturn(contatos);

        List<Contato> resultado = contatoService.listarTodos();

        assertEquals(2, resultado.size());
        verify(contatoRepository).findAll();
    }

    @Test
    void listarTodos_deveriaRetornarListaVaziaQuandoNaoExistirContatos() {
        when(contatoRepository.findAll()).thenReturn(List.of());

        List<Contato> resultado = contatoService.listarTodos();

        assertTrue(resultado.isEmpty());
    }

    @Test
    void buscarPorId_deveriaRetornarContatoQuandoExistir() {
        when(contatoRepository.findById(1L)).thenReturn(Optional.of(contato));

        Optional<Contato> resultado = contatoService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("João Silva", resultado.get().getNome());
    }

    @Test
    void buscarPorId_deveriaRetornarVazioQuandoNaoExistir() {
        when(contatoRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Contato> resultado = contatoService.buscarPorId(1L);

        assertFalse(resultado.isPresent());
    }

    @Test
    void buscarPorNome_deveriaRetornarContatosQueCorrespondem() {
        List<Contato> contatos = Arrays.asList(criarContato(), criarContatoComNome("João Souza"));
        when(contatoRepository.findByNomeContainingIgnoreCase("João")).thenReturn(contatos);

        List<Contato> resultado = contatoService.buscarPorNome("João");

        assertEquals(2, resultado.size());
    }

    @Test
    void salvar_deveriaCriarContatoComEnderecosTelefonesEEmails() {
        when(contatoRepository.save(any(Contato.class))).thenReturn(contato);

        Contato resultado = contatoService.salvar(contatoDTO);

        assertNotNull(resultado);
        assertEquals("João Silva", resultado.getNome());
        verify(contatoRepository).save(any(Contato.class));
    }

    @Test
    void salvar_deveriaCriarContatoSemListasOpcionais() {
        ContatoDTO dtoSimples = new ContatoDTO();
        dtoSimples.setNome("Maria");

        Contato salvo = new Contato();
        salvo.setNome("Maria");
        when(contatoRepository.save(any(Contato.class))).thenReturn(salvo);

        Contato resultado = contatoService.salvar(dtoSimples);

        assertNotNull(resultado);
        assertEquals("Maria", resultado.getNome());
    }

    @Test
    void atualizar_deveriaAtualizarContatoExistente() {
        when(contatoRepository.findById(1L)).thenReturn(Optional.of(contato));
        when(contatoRepository.save(any(Contato.class))).thenReturn(contato);

        Contato resultado = contatoService.atualizar(1L, contatoDTO);

        assertNotNull(resultado);
        assertEquals("João Silva", resultado.getNome());
    }

    @Test
    void atualizar_deveriaLancarExcecaoQuandoContatoNaoExistir() {
        when(contatoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> contatoService.atualizar(999L, contatoDTO));
    }

    @Test
    void excluir_deveriaRemoverContatoQuandoExistir() {
        when(contatoRepository.existsById(1L)).thenReturn(true);

        contatoService.excluir(1L);

        verify(contatoRepository).deleteById(1L);
    }

    @Test
    void excluir_deveriaLancarExcecaoQuandoContatoNaoExistir() {
        when(contatoRepository.existsById(999L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> contatoService.excluir(999L));
    }

    @Test
    void toDto_deveriaConverterContatoParaDTO() {
        ContatoDTO dto = contatoService.toDto(contato);

        assertNotNull(dto);
        assertEquals(contato.getId(), dto.getId());
        assertEquals(contato.getNome(), dto.getNome());
        assertNotNull(dto.getEnderecos());
        assertNotNull(dto.getTelefones());
        assertNotNull(dto.getEmails());
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

    private Contato criarContatoComNome(String nome) {
        Contato c = new Contato();
        c.setId(2L);
        c.setNome(nome);
        return c;
    }
}
