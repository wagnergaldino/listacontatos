package br.net.galdino.service;

import br.net.galdino.dto.TelefoneDTO;
import br.net.galdino.model.Contato;
import br.net.galdino.model.Telefone;
import br.net.galdino.repository.ContatoRepository;
import br.net.galdino.repository.TelefoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TelefoneService {

    @Autowired
    private TelefoneRepository telefoneRepository;

    @Autowired
    private ContatoRepository contatoRepository;

    public List<Telefone> listarTodos() {
        return telefoneRepository.findAll();
    }

    public List<Telefone> listarPorContato(Long contatoId) {
        return telefoneRepository.findByContatoId(contatoId);
    }

    public Telefone buscarPorId(Long id) {
        return telefoneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Telefone não encontrado com id: " + id));
    }

    public Telefone salvar(Long contatoId, TelefoneDTO dto) {
        Contato contato = contatoRepository.findById(contatoId)
                .orElseThrow(() -> new RuntimeException("Contato não encontrado com id: " + contatoId));

        Telefone telefone = new Telefone();
        telefone.setCodigoPais(dto.getCodigoPais());
        telefone.setDdd(dto.getDdd());
        telefone.setNumero(dto.getNumero());
        telefone.setTipoTelefone(dto.getTipoTelefone());
        contato.addTelefone(telefone);

        contatoRepository.save(contato);
        return telefone;
    }

    public Telefone atualizar(Long id, TelefoneDTO dto) {
        Telefone telefone = telefoneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Telefone não encontrado com id: " + id));

        telefone.setCodigoPais(dto.getCodigoPais());
        telefone.setDdd(dto.getDdd());
        telefone.setNumero(dto.getNumero());
        telefone.setTipoTelefone(dto.getTipoTelefone());

        return telefoneRepository.save(telefone);
    }

    public void excluir(Long id) {
        if (!telefoneRepository.existsById(id)) {
            throw new RuntimeException("Telefone não encontrado com id: " + id);
        }
        telefoneRepository.deleteById(id);
    }

    public TelefoneDTO toDto(Telefone telefone) {
        TelefoneDTO dto = new TelefoneDTO();
        dto.setId(telefone.getId());
        dto.setCodigoPais(telefone.getCodigoPais());
        dto.setDdd(telefone.getDdd());
        dto.setNumero(telefone.getNumero());
        dto.setTipoTelefone(telefone.getTipoTelefone());
        return dto;
    }
}
