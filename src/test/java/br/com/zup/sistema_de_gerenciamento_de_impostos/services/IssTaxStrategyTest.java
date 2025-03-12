package br.com.zup.sistema_de_gerenciamento_de_impostos.strategies;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IssTaxStrategyTest {

    @Test
    @DisplayName("Deve calcular corretamente o imposto ISS (5%) para um valor base")
    void testIssTax() {
        TaxCalculationStrategy iss = new IssTaxStrategy();
        double result = iss.calculateTax(1000);
        assertEquals(50, result); // 5% de 1000
    }
}