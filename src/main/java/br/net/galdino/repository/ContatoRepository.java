package br.net.galdino.repository;

import br.net.galdino.model.Contato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContatoRepository extends JpaRepository<Contato, Long> {

    List<Contato> findByNomeContainingIgnoreCase(String nome);

    Optional<Contato> findByNome(String nome);
}
