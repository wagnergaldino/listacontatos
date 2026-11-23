package br.net.galdino.controller;

import br.net.galdino.dto.EnderecoDTO;
import br.net.galdino.model.Endereco;
import br.net.galdino.service.EnderecoService;
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
@RequestMapping("/api/enderecos")
@Tag(name = "Endereços", description = "API de gerenciamento de endereços")
public class EnderecoController {

    @Autowired
    private EnderecoService enderecoService;

    @GetMapping
    @Operation(summary = "Listar todos os endereços")
    public ResponseEntity<List<EnderecoDTO>> listarTodos() {
        List<EnderecoDTO> enderecos = enderecoService.listarTodos().stream()
                .map(enderecoService::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(enderecos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar endereço por ID")
    public ResponseEntity<EnderecoDTO> buscarPorId(@PathVariable Long id) {
        try {
            Endereco endereco = enderecoService.buscarPorId(id);
            return ResponseEntity.ok(enderecoService.toDto(endereco));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/contato/{contatoId}")
    @Operation(summary = "Listar endereços por contato")
    public ResponseEntity<List<EnderecoDTO>> listarPorContato(@PathVariable Long contatoId) {
        List<EnderecoDTO> enderecos = enderecoService.listarPorContato(contatoId).stream()
                .map(enderecoService::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(enderecos);
    }

    @PostMapping("/contato/{contatoId}")
    @Operation(summary = "Criar um novo endereço para um contato")
    public ResponseEntity<EnderecoDTO> salvar(@PathVariable Long contatoId, @Valid @RequestBody EnderecoDTO dto) {
        Endereco endereco = enderecoService.salvar(contatoId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(enderecoService.toDto(endereco));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um endereço existente")
    public ResponseEntity<EnderecoDTO> atualizar(@PathVariable Long id, @Valid @RequestBody EnderecoDTO dto) {
        Endereco endereco = enderecoService.atualizar(id, dto);
        return ResponseEntity.ok(enderecoService.toDto(endereco));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir um endereço")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        enderecoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
