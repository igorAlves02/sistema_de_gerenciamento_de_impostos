package br.com.zup.sistema_de_gerenciamento_de_impostos.strategies;

import org.springframework.stereotype.Component;

@Component
public class TaxCalculationStrategyFactory {
    
    public TaxCalculationStrategy getStrategy(String taxTypeName) {
        switch (taxTypeName.toUpperCase()) {
            case "ICMS":
                return new IcmsTaxStrategy();
            case "ISS":
                return new IssTaxStrategy();
            default:
                // Estratégia padrão para novos impostos
                return value -> value * 0.1; // 10% por padrão
        }
    }
}