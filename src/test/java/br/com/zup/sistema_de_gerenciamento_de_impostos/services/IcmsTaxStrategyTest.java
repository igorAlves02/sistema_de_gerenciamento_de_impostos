package br.com.zup.sistema_de_gerenciamento_de_impostos.strategies;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IcmsTaxStrategyTest {

    @Test
    @DisplayName("Deve calcular corretamente o imposto ICMS (18%) para um valor base")
    void testIcmsTax() {
        TaxCalculationStrategy icms = new IcmsTaxStrategy();
        double result = icms.calculateTax(1000);
        assertEquals(180, result); // 18% de 1000
    }
}