package br.com.zup.sistema_de_gerenciamento_de_impostos.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Entity
@Data
@Table(name = "tax_types")
public class TaxType {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "O nome do imposto é obrigatório")
    @Column(nullable = false)
    private String name;
    
    @NotBlank(message = "A descrição do imposto é obrigatória")
    @Column(nullable = false)
    private String description;
    
    @NotNull(message = "A alíquota é obrigatória")
    @Positive(message = "A alíquota deve ser um valor positivo")
    @Column(nullable = false)
    private Double rate;
    
    // Getters e Setters manuais
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Double getRate() {
        return rate;
    }
    
    public void setRate(Double rate) {
        this.rate = rate;
    }
}