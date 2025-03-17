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
}