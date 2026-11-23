package br.net.galdino.service;

import br.net.galdino.dto.TelefoneDTO;
import br.net.galdino.enums.TipoTelefone;
import br.net.galdino.model.Contato;
import br.net.galdino.model.Telefone;
import br.net.galdino.repository.ContatoRepository;
import br.net.galdino.repository.TelefoneRepository;
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
class TelefoneServiceTest {

    @Mock
    private TelefoneRepository telefoneRepository;

    @Mock
    private ContatoRepository contatoRepository;

    @InjectMocks
    private TelefoneService telefoneService;

    private TelefoneDTO telefoneDTO;
    private Telefone telefone;
    private Contato contato;

    @BeforeEach
    void setUp() {
        contato = new Contato();
        contato.setId(1L);
        contato.setNome("João Silva");

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
    void listarTodos_deveriaRetornarListaDeTelefones() {
        List<Telefone> telefones = Arrays.asList(telefone, criarTelefoneComNumero("888888888"));
        when(telefoneRepository.findAll()).thenReturn(telefones);

        List<Telefone> resultado = telefoneService.listarTodos();

        assertEquals(2, resultado.size());
        verify(telefoneRepository).findAll();
    }

    @Test
    void listarTodos_deveriaRetornarListaVaziaQuandoNaoExistirTelefones() {
        when(telefoneRepository.findAll()).thenReturn(List.of());

        List<Telefone> resultado = telefoneService.listarTodos();

        assertTrue(resultado.isEmpty());
    }

    @Test
    void listarPorContato_deveriaRetornarTelefonesDoContato() {
        when(telefoneRepository.findByContatoId(1L)).thenReturn(List.of(telefone));

        List<Telefone> resultado = telefoneService.listarPorContato(1L);

        assertEquals(1, resultado.size());
    }

    @Test
    void buscarPorId_deveriaRetornarTelefoneQuandoExistir() {
        when(telefoneRepository.findById(1L)).thenReturn(Optional.of(telefone));

        Telefone resultado = telefoneService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals("999999999", resultado.getNumero());
    }

    @Test
    void buscarPorId_deveriaLancarExcecaoQuandoNaoExistir() {
        when(telefoneRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> telefoneService.buscarPorId(999L));
    }

    @Test
    void salvar_deveriaCriarTelefoneParaContatoExistente() {
        when(contatoRepository.findById(1L)).thenReturn(Optional.of(contato));
        when(contatoRepository.save(any(Contato.class))).thenReturn(contato);

        Telefone resultado = telefoneService.salvar(1L, telefoneDTO);

        assertNotNull(resultado);
        assertEquals("999999999", resultado.getNumero());
        verify(contatoRepository).save(any(Contato.class));
    }

    @Test
    void salvar_deveriaLancarExcecaoQuandoContatoNaoExistir() {
        when(contatoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> telefoneService.salvar(999L, telefoneDTO));
    }

    @Test
    void atualizar_deveriaAtualizarTelefoneExistente() {
        when(telefoneRepository.findById(1L)).thenReturn(Optional.of(telefone));
        when(telefoneRepository.save(any(Telefone.class))).thenReturn(telefone);

        TelefoneDTO updateDTO = new TelefoneDTO();
        updateDTO.setCodigoPais("55");
        updateDTO.setDdd("21");
        updateDTO.setNumero("777777777");
        updateDTO.setTipoTelefone(TipoTelefone.FIXO);

        Telefone resultado = telefoneService.atualizar(1L, updateDTO);

        assertNotNull(resultado);
    }

    @Test
    void atualizar_deveriaLancarExcecaoQuandoTelefoneNaoExistir() {
        when(telefoneRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> telefoneService.atualizar(999L, telefoneDTO));
    }

    @Test
    void excluir_deveriaRemoverTelefoneQuandoExistir() {
        when(telefoneRepository.existsById(1L)).thenReturn(true);

        telefoneService.excluir(1L);

        verify(telefoneRepository).deleteById(1L);
    }

    @Test
    void excluir_deveriaLancarExcecaoQuandoTelefoneNaoExistir() {
        when(telefoneRepository.existsById(999L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> telefoneService.excluir(999L));
    }

    @Test
    void toDto_deveriaConverterTelefoneParaDTO() {
        TelefoneDTO dto = telefoneService.toDto(telefone);

        assertNotNull(dto);
        assertEquals(telefone.getId(), dto.getId());
        assertEquals(telefone.getCodigoPais(), dto.getCodigoPais());
        assertEquals(telefone.getDdd(), dto.getDdd());
        assertEquals(telefone.getNumero(), dto.getNumero());
        assertEquals(telefone.getTipoTelefone(), dto.getTipoTelefone());
    }

    private Telefone criarTelefoneComNumero(String numero) {
        Telefone t = new Telefone();
        t.setId(2L);
        t.setCodigoPais("55");
        t.setDdd("21");
        t.setNumero(numero);
        t.setTipoTelefone(TipoTelefone.FIXO);
        return t;
    }
}
