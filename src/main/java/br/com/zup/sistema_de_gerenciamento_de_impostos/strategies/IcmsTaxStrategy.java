package br.com.zup.sistema_de_gerenciamento_de_impostos.strategies;

import org.springframework.stereotype.Component;

@Component
public class IcmsTaxStrategy implements TaxCalculationStrategy {
    @Override
    public double calculateTax(double value) {
        return 0.18 * value; // 18% para ICMS
    }
}