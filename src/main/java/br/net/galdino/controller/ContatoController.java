package br.net.galdino.controller;

import br.net.galdino.dto.ContatoDTO;
import br.net.galdino.model.Contato;
import br.net.galdino.service.ContatoService;
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
@RequestMapping("/api/contatos")
@Tag(name = "Contatos", description = "API de gerenciamento de contatos")
public class ContatoController {

    @Autowired
    private ContatoService contatoService;

    @GetMapping
    @Operation(summary = "Listar todos os contatos")
    public ResponseEntity<List<ContatoDTO>> listarTodos() {
        List<ContatoDTO> contatos = contatoService.listarTodos().stream()
                .map(contatoService::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(contatos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar contato por ID")
    public ResponseEntity<ContatoDTO> buscarPorId(@PathVariable Long id) {
        return contatoService.buscarPorId(id)
                .map(contato -> ResponseEntity.ok(contatoService.toDto(contato)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/pesquisa")
    @Operation(summary = "Buscar contatos por nome")
    public ResponseEntity<List<ContatoDTO>> buscarPorNome(@RequestParam String nome) {
        List<ContatoDTO> contatos = contatoService.buscarPorNome(nome).stream()
                .map(contatoService::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(contatos);
    }

    @PostMapping
    @Operation(summary = "Criar um novo contato")
    public ResponseEntity<ContatoDTO> salvar(@Valid @RequestBody ContatoDTO dto) {
        Contato contato = contatoService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(contatoService.toDto(contato));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um contato existente")
    public ResponseEntity<ContatoDTO> atualizar(@PathVariable Long id, @Valid @RequestBody ContatoDTO dto) {
        Contato contato = contatoService.atualizar(id, dto);
        return ResponseEntity.ok(contatoService.toDto(contato));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir um contato")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        contatoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
