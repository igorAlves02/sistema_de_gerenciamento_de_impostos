package br.com.zup.sistema_de_gerenciamento_de_impostos.strategies;

import org.springframework.stereotype.Component;

@Component
public class PisTaxStrategy implements TaxCalculationStrategy {
    @Override
    public double calculateTax(double value) {
        return 0; // Implementação mínima que fará o teste falhar
    }
}