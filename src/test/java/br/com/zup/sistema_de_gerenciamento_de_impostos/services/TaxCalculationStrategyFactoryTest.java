package br.com.zup.sistema_de_gerenciamento_de_impostos.strategies;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TaxCalculationStrategyFactoryTest {

    @Test
    @DisplayName("Deve retornar a estratégia ICMS quando o nome do imposto for ICMS")
    void shouldReturnIcmsStrategyForIcmsName() {
        TaxCalculationStrategyFactory factory = new TaxCalculationStrategyFactory();
        TaxCalculationStrategy strategy = factory.getStrategy("ICMS");

        // Verificar se a estratégia é do tipo IcmsTaxStrategy
        assertTrue(strategy instanceof IcmsTaxStrategy);

        // Verificar se calcula corretamente
        assertEquals(180, strategy.calculateTax(1000));
    }

    @Test
    @DisplayName("Deve retornar a estratégia ISS quando o nome do imposto for ISS")
    void shouldReturnIssStrategyForIssName() {
        TaxCalculationStrategyFactory factory = new TaxCalculationStrategyFactory();
        TaxCalculationStrategy strategy = factory.getStrategy("ISS");

        // Verificar se a estratégia é do tipo IssTaxStrategy
        assertTrue(strategy instanceof IssTaxStrategy);

        // Verificar se calcula corretamente
        assertEquals(50, strategy.calculateTax(1000));
    }

    @Test
    @DisplayName("Deve retornar a estratégia padrão (10%) quando o tipo de imposto for desconhecido")
    void shouldReturnDefaultStrategyForUnknownTaxType() {
        TaxCalculationStrategyFactory factory = new TaxCalculationStrategyFactory();
        TaxCalculationStrategy strategy = factory.getStrategy("UNKNOWN");

        // Verificar se calcula usando a estratégia padrão (10%)
        assertEquals(100, strategy.calculateTax(1000));
    }
}