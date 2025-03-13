package br.com.zup.sistema_de_gerenciamento_de_impostos.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "tax_types")
public class TaxType {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    private String description;
    
    private Double rate;
}