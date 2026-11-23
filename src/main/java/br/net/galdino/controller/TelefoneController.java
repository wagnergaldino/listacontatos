package br.net.galdino.controller;

import br.net.galdino.dto.TelefoneDTO;
import br.net.galdino.model.Telefone;
import br.net.galdino.service.TelefoneService;
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
@RequestMapping("/api/telefones")
@Tag(name = "Telefones", description = "API de gerenciamento de telefones")
public class TelefoneController {

    @Autowired
    private TelefoneService telefoneService;

    @GetMapping
    @Operation(summary = "Listar todos os telefones")
    public ResponseEntity<List<TelefoneDTO>> listarTodos() {
        List<TelefoneDTO> telefones = telefoneService.listarTodos().stream()
                .map(telefoneService::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(telefones);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar telefone por ID")
    public ResponseEntity<TelefoneDTO> buscarPorId(@PathVariable Long id) {
        try {
            Telefone telefone = telefoneService.buscarPorId(id);
            return ResponseEntity.ok(telefoneService.toDto(telefone));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/contato/{contatoId}")
    @Operation(summary = "Listar telefones por contato")
    public ResponseEntity<List<TelefoneDTO>> listarPorContato(@PathVariable Long contatoId) {
        List<TelefoneDTO> telefones = telefoneService.listarPorContato(contatoId).stream()
                .map(telefoneService::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(telefones);
    }

    @PostMapping("/contato/{contatoId}")
    @Operation(summary = "Criar um novo telefone para um contato")
    public ResponseEntity<TelefoneDTO> salvar(@PathVariable Long contatoId, @Valid @RequestBody TelefoneDTO dto) {
        Telefone telefone = telefoneService.salvar(contatoId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(telefoneService.toDto(telefone));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um telefone existente")
    public ResponseEntity<TelefoneDTO> atualizar(@PathVariable Long id, @Valid @RequestBody TelefoneDTO dto) {
        Telefone telefone = telefoneService.atualizar(id, dto);
        return ResponseEntity.ok(telefoneService.toDto(telefone));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir um telefone")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        telefoneService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
