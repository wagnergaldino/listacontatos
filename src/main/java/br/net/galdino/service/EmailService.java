package br.net.galdino.service;

import br.net.galdino.dto.EmailDTO;
import br.net.galdino.model.Contato;
import br.net.galdino.model.Email;
import br.net.galdino.repository.ContatoRepository;
import br.net.galdino.repository.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private ContatoRepository contatoRepository;

    public List<Email> listarTodos() {
        return emailRepository.findAll();
    }

    public List<Email> listarPorContato(Long contatoId) {
        return emailRepository.findByContatoId(contatoId);
    }

    public Email buscarPorId(Long id) {
        return emailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Email não encontrado com id: " + id));
    }

    public Email salvar(Long contatoId, EmailDTO dto) {
        Contato contato = contatoRepository.findById(contatoId)
                .orElseThrow(() -> new RuntimeException("Contato não encontrado com id: " + contatoId));

        Email email = new Email();
        email.setEmail(dto.getEmail());
        email.setTipoEmail(dto.getTipoEmail());
        contato.addEmail(email);

        contatoRepository.save(contato);
        return email;
    }

    public Email atualizar(Long id, EmailDTO dto) {
        Email email = emailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Email não encontrado com id: " + id));

        email.setEmail(dto.getEmail());
        email.setTipoEmail(dto.getTipoEmail());

        return emailRepository.save(email);
    }

    public void excluir(Long id) {
        if (!emailRepository.existsById(id)) {
            throw new RuntimeException("Email não encontrado com id: " + id);
        }
        emailRepository.deleteById(id);
    }

    public EmailDTO toDto(Email email) {
        EmailDTO dto = new EmailDTO();
        dto.setId(email.getId());
        dto.setEmail(email.getEmail());
        dto.setTipoEmail(email.getTipoEmail());
        return dto;
    }
}
