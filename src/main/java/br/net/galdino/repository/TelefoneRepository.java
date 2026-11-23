package br.net.galdino.repository;

import br.net.galdino.model.Telefone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TelefoneRepository extends JpaRepository<Telefone, Long> {

    List<Telefone> findByContatoId(Long contatoId);

    void deleteByContatoId(Long contatoId);
}
