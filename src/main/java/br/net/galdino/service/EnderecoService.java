package br.net.galdino.service;

import br.net.galdino.dto.EnderecoDTO;
import br.net.galdino.model.Contato;
import br.net.galdino.model.Endereco;
import br.net.galdino.repository.ContatoRepository;
import br.net.galdino.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private ContatoRepository contatoRepository;

    public List<Endereco> listarTodos() {
        return enderecoRepository.findAll();
    }

    public List<Endereco> listarPorContato(Long contatoId) {
        return enderecoRepository.findByContatoId(contatoId);
    }

    public Endereco buscarPorId(Long id) {
        return enderecoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado com id: " + id));
    }

    public Endereco salvar(Long contatoId, EnderecoDTO dto) {
        Contato contato = contatoRepository.findById(contatoId)
                .orElseThrow(() -> new RuntimeException("Contato não encontrado com id: " + contatoId));

        Endereco endereco = new Endereco();
        endereco.setLogradouro(dto.getLogradouro());
        endereco.setNumero(dto.getNumero());
        endereco.setComplemento(dto.getComplemento());
        endereco.setBairro(dto.getBairro());
        endereco.setCidade(dto.getCidade());
        endereco.setEstado(dto.getEstado());
        endereco.setTipoEndereco(dto.getTipoEndereco());
        contato.addEndereco(endereco);

        contatoRepository.save(contato);
        return endereco;
    }

    public Endereco atualizar(Long id, EnderecoDTO dto) {
        Endereco endereco = enderecoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado com id: " + id));

        endereco.setLogradouro(dto.getLogradouro());
        endereco.setNumero(dto.getNumero());
        endereco.setComplemento(dto.getComplemento());
        endereco.setBairro(dto.getBairro());
        endereco.setCidade(dto.getCidade());
        endereco.setEstado(dto.getEstado());
        endereco.setTipoEndereco(dto.getTipoEndereco());

        return enderecoRepository.save(endereco);
    }

    public void excluir(Long id) {
        if (!enderecoRepository.existsById(id)) {
            throw new RuntimeException("Endereço não encontrado com id: " + id);
        }
        enderecoRepository.deleteById(id);
    }

    public EnderecoDTO toDto(Endereco endereco) {
        EnderecoDTO dto = new EnderecoDTO();
        dto.setId(endereco.getId());
        dto.setLogradouro(endereco.getLogradouro());
        dto.setNumero(endereco.getNumero());
        dto.setComplemento(endereco.getComplemento());
        dto.setBairro(endereco.getBairro());
        dto.setCidade(endereco.getCidade());
        dto.setEstado(endereco.getEstado());
        dto.setTipoEndereco(endereco.getTipoEndereco());
        return dto;
    }
}
