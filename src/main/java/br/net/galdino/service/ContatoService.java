package br.net.galdino.service;

import br.net.galdino.dto.ContatoDTO;
import br.net.galdino.dto.EnderecoDTO;
import br.net.galdino.dto.EmailDTO;
import br.net.galdino.dto.TelefoneDTO;
import br.net.galdino.model.Contato;
import br.net.galdino.model.Endereco;
import br.net.galdino.model.Email;
import br.net.galdino.model.Telefone;
import br.net.galdino.repository.ContatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ContatoService {

    @Autowired
    private ContatoRepository contatoRepository;

    public List<Contato> listarTodos() {
        return contatoRepository.findAll();
    }

    public Optional<Contato> buscarPorId(Long id) {
        return contatoRepository.findById(id);
    }

    public List<Contato> buscarPorNome(String nome) {
        return contatoRepository.findByNomeContainingIgnoreCase(nome);
    }

    public Contato salvar(ContatoDTO dto) {
        Contato contato = new Contato();
        contato.setNome(dto.getNome());

        if (dto.getEnderecos() != null) {
            for (EnderecoDTO endDto : dto.getEnderecos()) {
                Endereco endereco = new Endereco();
                endereco.setLogradouro(endDto.getLogradouro());
                endereco.setNumero(endDto.getNumero());
                endereco.setComplemento(endDto.getComplemento());
                endereco.setBairro(endDto.getBairro());
                endereco.setCidade(endDto.getCidade());
                endereco.setEstado(endDto.getEstado());
                endereco.setTipoEndereco(endDto.getTipoEndereco());
                contato.addEndereco(endereco);
            }
        }

        if (dto.getTelefones() != null) {
            for (TelefoneDTO telDto : dto.getTelefones()) {
                Telefone telefone = new Telefone();
                telefone.setCodigoPais(telDto.getCodigoPais());
                telefone.setDdd(telDto.getDdd());
                telefone.setNumero(telDto.getNumero());
                telefone.setTipoTelefone(telDto.getTipoTelefone());
                contato.addTelefone(telefone);
            }
        }

        if (dto.getEmails() != null) {
            for (EmailDTO emailDto : dto.getEmails()) {
                Email email = new Email();
                email.setEmail(emailDto.getEmail());
                email.setTipoEmail(emailDto.getTipoEmail());
                contato.addEmail(email);
            }
        }

        return contatoRepository.save(contato);
    }

    public Contato atualizar(Long id, ContatoDTO dto) {
        Contato contato = contatoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contato não encontrado com id: " + id));

        contato.setNome(dto.getNome());

        // Atualiza endereços
        contato.getEnderecos().clear();
        if (dto.getEnderecos() != null) {
            for (EnderecoDTO endDto : dto.getEnderecos()) {
                Endereco endereco = new Endereco();
                endereco.setLogradouro(endDto.getLogradouro());
                endereco.setNumero(endDto.getNumero());
                endereco.setComplemento(endDto.getComplemento());
                endereco.setBairro(endDto.getBairro());
                endereco.setCidade(endDto.getCidade());
                endereco.setEstado(endDto.getEstado());
                endereco.setTipoEndereco(endDto.getTipoEndereco());
                contato.addEndereco(endereco);
            }
        }

        // Atualiza telefones
        contato.getTelefones().clear();
        if (dto.getTelefones() != null) {
            for (TelefoneDTO telDto : dto.getTelefones()) {
                Telefone telefone = new Telefone();
                telefone.setCodigoPais(telDto.getCodigoPais());
                telefone.setDdd(telDto.getDdd());
                telefone.setNumero(telDto.getNumero());
                telefone.setTipoTelefone(telDto.getTipoTelefone());
                contato.addTelefone(telefone);
            }
        }

        // Atualiza emails
        contato.getEmails().clear();
        if (dto.getEmails() != null) {
            for (EmailDTO emailDto : dto.getEmails()) {
                Email email = new Email();
                email.setEmail(emailDto.getEmail());
                email.setTipoEmail(emailDto.getTipoEmail());
                contato.addEmail(email);
            }
        }

        return contatoRepository.save(contato);
    }

    public void excluir(Long id) {
        if (!contatoRepository.existsById(id)) {
            throw new RuntimeException("Contato não encontrado com id: " + id);
        }
        contatoRepository.deleteById(id);
    }

    public ContatoDTO toDto(Contato contato) {
        ContatoDTO dto = new ContatoDTO();
        dto.setId(contato.getId());
        dto.setNome(contato.getNome());

        List<EnderecoDTO> enderecosDto = new ArrayList<>();
        for (Endereco end : contato.getEnderecos()) {
            EnderecoDTO endDto = new EnderecoDTO();
            endDto.setId(end.getId());
            endDto.setLogradouro(end.getLogradouro());
            endDto.setNumero(end.getNumero());
            endDto.setComplemento(end.getComplemento());
            endDto.setBairro(end.getBairro());
            endDto.setCidade(end.getCidade());
            endDto.setEstado(end.getEstado());
            endDto.setTipoEndereco(end.getTipoEndereco());
            enderecosDto.add(endDto);
        }
        dto.setEnderecos(enderecosDto);

        List<TelefoneDTO> telefonesDto = new ArrayList<>();
        for (Telefone tel : contato.getTelefones()) {
            TelefoneDTO telDto = new TelefoneDTO();
            telDto.setId(tel.getId());
            telDto.setCodigoPais(tel.getCodigoPais());
            telDto.setDdd(tel.getDdd());
            telDto.setNumero(tel.getNumero());
            telDto.setTipoTelefone(tel.getTipoTelefone());
            telefonesDto.add(telDto);
        }
        dto.setTelefones(telefonesDto);

        List<EmailDTO> emailsDto = new ArrayList<>();
        for (Email email : contato.getEmails()) {
            EmailDTO emailDto = new EmailDTO();
            emailDto.setId(email.getId());
            emailDto.setEmail(email.getEmail());
            emailDto.setTipoEmail(email.getTipoEmail());
            emailsDto.add(emailDto);
        }
        dto.setEmails(emailsDto);

        return dto;
    }
}
