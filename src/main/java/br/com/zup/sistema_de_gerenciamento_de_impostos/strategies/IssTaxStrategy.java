package br.com.zup.sistema_de_gerenciamento_de_impostos.strategies;

import org.springframework.stereotype.Component;

@Component
public class IssTaxStrategy implements TaxCalculationStrategy {
    @Override
    public double calculateTax(double value) {
        return 0.05 * value; // 5% para ISS
    }
}