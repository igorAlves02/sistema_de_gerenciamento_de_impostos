package br.com.zup.sistema_de_gerenciamento_de_impostos.strategies;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PisTaxStrategyTest {
    
    @Test
    @DisplayName("Deve calcular corretamente o imposto PIS (1.65%) para um valor base")
    void testPisTax() {
        TaxCalculationStrategy pis = new PisTaxStrategy();
        double result = pis.calculateTax(1000);
        assertEquals(16.5, result); // 1.65% de 1000
    }
}