package br.net.galdino.repository;

import br.net.galdino.model.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {

    List<Email> findByContatoId(Long contatoId);

    void deleteByContatoId(Long contatoId);
}
