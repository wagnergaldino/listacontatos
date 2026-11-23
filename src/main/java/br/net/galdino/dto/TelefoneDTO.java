package br.net.galdino.dto;

import br.net.galdino.enums.TipoTelefone;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TelefoneDTO {

    private Long id;

    @NotBlank(message = "O código do país é obrigatório")
    @Size(max = 5)
    private String codigoPais;

    @NotBlank(message = "O DDD é obrigatório")
    @Size(max = 2)
    private String ddd;

    @NotBlank(message = "O número é obrigatório")
    @Size(max = 15)
    private String numero;

    @NotNull(message = "O tipo de telefone é obrigatório")
    private TipoTelefone tipoTelefone;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoPais() {
        return codigoPais;
    }

    public void setCodigoPais(String codigoPais) {
        this.codigoPais = codigoPais;
    }

    public String getDdd() {
        return ddd;
    }

    public void setDdd(String ddd) {
        this.ddd = ddd;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public TipoTelefone getTipoTelefone() {
        return tipoTelefone;
    }

    public void setTipoTelefone(TipoTelefone tipoTelefone) {
        this.tipoTelefone = tipoTelefone;
    }
}
