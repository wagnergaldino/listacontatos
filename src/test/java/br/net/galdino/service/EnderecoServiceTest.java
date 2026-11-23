package br.net.galdino.service;

import br.net.galdino.dto.EnderecoDTO;
import br.net.galdino.enums.TipoEndereco;
import br.net.galdino.model.Contato;
import br.net.galdino.model.Endereco;
import br.net.galdino.repository.ContatoRepository;
import br.net.galdino.repository.EnderecoRepository;
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
class EnderecoServiceTest {

    @Mock
    private EnderecoRepository enderecoRepository;

    @Mock
    private ContatoRepository contatoRepository;

    @InjectMocks
    private EnderecoService enderecoService;

    private EnderecoDTO enderecoDTO;
    private Endereco endereco;
    private Contato contato;

    @BeforeEach
    void setUp() {
        contato = new Contato();
        contato.setId(1L);
        contato.setNome("João Silva");

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
    void listarTodos_deveriaRetornarListaDeEnderecos() {
        List<Endereco> enderecos = Arrays.asList(endereco, criarEnderecoComLogradouro("Av. Brasil"));
        when(enderecoRepository.findAll()).thenReturn(enderecos);

        List<Endereco> resultado = enderecoService.listarTodos();

        assertEquals(2, resultado.size());
        verify(enderecoRepository).findAll();
    }

    @Test
    void listarTodos_deveriaRetornarListaVaziaQuandoNaoExistirEnderecos() {
        when(enderecoRepository.findAll()).thenReturn(List.of());

        List<Endereco> resultado = enderecoService.listarTodos();

        assertTrue(resultado.isEmpty());
    }

    @Test
    void listarPorContato_deveriaRetornarEnderecosDoContato() {
        when(enderecoRepository.findByContatoId(1L)).thenReturn(List.of(endereco));

        List<Endereco> resultado = enderecoService.listarPorContato(1L);

        assertEquals(1, resultado.size());
    }

    @Test
    void buscarPorId_deveriaRetornarEnderecoQuandoExistir() {
        when(enderecoRepository.findById(1L)).thenReturn(Optional.of(endereco));

        Endereco resultado = enderecoService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals("Rua das Flores", resultado.getLogradouro());
    }

    @Test
    void buscarPorId_deveriaLancarExcecaoQuandoNaoExistir() {
        when(enderecoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> enderecoService.buscarPorId(999L));
    }

    @Test
    void salvar_deveriaCriarEnderecoParaContatoExistente() {
        when(contatoRepository.findById(1L)).thenReturn(Optional.of(contato));
        when(contatoRepository.save(any(Contato.class))).thenReturn(contato);

        Endereco resultado = enderecoService.salvar(1L, enderecoDTO);

        assertNotNull(resultado);
        assertEquals("Rua das Flores", resultado.getLogradouro());
        verify(contatoRepository).save(any(Contato.class));
    }

    @Test
    void salvar_deveriaLancarExcecaoQuandoContatoNaoExistir() {
        when(contatoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> enderecoService.salvar(999L, enderecoDTO));
    }

    @Test
    void atualizar_deveriaAtualizarEnderecoExistente() {
        when(enderecoRepository.findById(1L)).thenReturn(Optional.of(endereco));
        when(enderecoRepository.save(any(Endereco.class))).thenReturn(endereco);

        EnderecoDTO updateDTO = new EnderecoDTO();
        updateDTO.setLogradouro("Av. Brasil");
        updateDTO.setNumero("456");
        updateDTO.setBairro("Jardins");
        updateDTO.setCidade("São Paulo");
        updateDTO.setEstado("SP");
        updateDTO.setTipoEndereco(TipoEndereco.COMERCIAL);

        Endereco resultado = enderecoService.atualizar(1L, updateDTO);

        assertNotNull(resultado);
    }

    @Test
    void atualizar_deveriaLancarExcecaoQuandoEnderecoNaoExistir() {
        when(enderecoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> enderecoService.atualizar(999L, enderecoDTO));
    }

    @Test
    void excluir_deveriaRemoverEnderecoQuandoExistir() {
        when(enderecoRepository.existsById(1L)).thenReturn(true);

        enderecoService.excluir(1L);

        verify(enderecoRepository).deleteById(1L);
    }

    @Test
    void excluir_deveriaLancarExcecaoQuandoEnderecoNaoExistir() {
        when(enderecoRepository.existsById(999L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> enderecoService.excluir(999L));
    }

    @Test
    void toDto_deveriaConverterEnderecoParaDTO() {
        EnderecoDTO dto = enderecoService.toDto(endereco);

        assertNotNull(dto);
        assertEquals(endereco.getId(), dto.getId());
        assertEquals(endereco.getLogradouro(), dto.getLogradouro());
        assertEquals(endereco.getNumero(), dto.getNumero());
        assertEquals(endereco.getBairro(), dto.getBairro());
        assertEquals(endereco.getCidade(), dto.getCidade());
        assertEquals(endereco.getEstado(), dto.getEstado());
        assertEquals(endereco.getTipoEndereco(), dto.getTipoEndereco());
    }

    private Endereco criarEnderecoComLogradouro(String logradouro) {
        Endereco e = new Endereco();
        e.setId(2L);
        e.setLogradouro(logradouro);
        e.setNumero("456");
        e.setBairro("Jardins");
        e.setCidade("São Paulo");
        e.setEstado("SP");
        e.setTipoEndereco(TipoEndereco.COMERCIAL);
        return e;
    }
}
