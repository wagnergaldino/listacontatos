package br.net.galdino.dto;

import br.net.galdino.enums.TipoEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class EmailDTO {

    private Long id;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "O email deve ser um endereço válido")
    @Size(max = 150)
    private String email;

    @NotNull(message = "O tipo de email é obrigatório")
    private TipoEmail tipoEmail;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public TipoEmail getTipoEmail() {
        return tipoEmail;
    }

    public void setTipoEmail(TipoEmail tipoEmail) {
        this.tipoEmail = tipoEmail;
    }
}
