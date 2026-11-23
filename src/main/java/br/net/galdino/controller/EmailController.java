package br.net.galdino.controller;

import br.net.galdino.dto.EmailDTO;
import br.net.galdino.model.Email;
import br.net.galdino.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/emails")
@Tag(name = "Emails", description = "API de gerenciamento de emails")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping
    @Operation(summary = "Listar todos os emails")
    public ResponseEntity<List<EmailDTO>> listarTodos() {
        List<EmailDTO> emails = emailService.listarTodos().stream()
                .map(emailService::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(emails);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar email por ID")
    public ResponseEntity<EmailDTO> buscarPorId(@PathVariable Long id) {
        try {
            Email email = emailService.buscarPorId(id);
            return ResponseEntity.ok(emailService.toDto(email));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/contato/{contatoId}")
    @Operation(summary = "Listar emails por contato")
    public ResponseEntity<List<EmailDTO>> listarPorContato(@PathVariable Long contatoId) {
        List<EmailDTO> emails = emailService.listarPorContato(contatoId).stream()
                .map(emailService::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(emails);
    }

    @PostMapping("/contato/{contatoId}")
    @Operation(summary = "Criar um novo email para um contato")
    public ResponseEntity<EmailDTO> salvar(@PathVariable Long contatoId, @Valid @RequestBody EmailDTO dto) {
        Email email = emailService.salvar(contatoId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(emailService.toDto(email));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um email existente")
    public ResponseEntity<EmailDTO> atualizar(@PathVariable Long id, @Valid @RequestBody EmailDTO dto) {
        Email email = emailService.atualizar(id, dto);
        return ResponseEntity.ok(emailService.toDto(email));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir um email")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        emailService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
