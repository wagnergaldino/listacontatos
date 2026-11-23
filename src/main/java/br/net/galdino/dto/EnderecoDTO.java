package br.net.galdino.dto;

import br.net.galdino.enums.TipoEndereco;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class EnderecoDTO {

    private Long id;

    @NotBlank(message = "O logradouro é obrigatório")
    @Size(max = 200)
    private String logradouro;

    @NotBlank(message = "O número é obrigatório")
    @Size(max = 10)
    private String numero;

    @Size(max = 100)
    private String complemento;

    @NotBlank(message = "O bairro é obrigatório")
    @Size(max = 100)
    private String bairro;

    @NotBlank(message = "A cidade é obrigatória")
    @Size(max = 100)
    private String cidade;

    @NotBlank(message = "O estado é obrigatório")
    @Size(max = 2)
    private String estado;

    @NotNull(message = "O tipo de endereço é obrigatório")
    private TipoEndereco tipoEndereco;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public TipoEndereco getTipoEndereco() {
        return tipoEndereco;
    }

    public void setTipoEndereco(TipoEndereco tipoEndereco) {
        this.tipoEndereco = tipoEndereco;
    }
}
