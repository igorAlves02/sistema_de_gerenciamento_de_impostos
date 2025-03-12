package br.com.zup.sistema_de_gerenciamento_de_impostos.services;

import br.com.zup.sistema_de_gerenciamento_de_impostos.dto.TaxCalculationRequestDto;
import br.com.zup.sistema_de_gerenciamento_de_impostos.dto.TaxCalculationResponseDto;
import br.com.zup.sistema_de_gerenciamento_de_impostos.models.TaxType;
import br.com.zup.sistema_de_gerenciamento_de_impostos.repositories.TaxTypeRepository;
import br.com.zup.sistema_de_gerenciamento_de_impostos.strategies.IcmsTaxStrategy;
import br.com.zup.sistema_de_gerenciamento_de_impostos.strategies.TaxCalculationStrategy;
import br.com.zup.sistema_de_gerenciamento_de_impostos.strategies.TaxCalculationStrategyFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaxCalculationServiceTest {

    @Mock
    private TaxTypeRepository taxTypeRepository;

    @Mock
    private TaxCalculationStrategyFactory strategyFactory;

    @InjectMocks
    private TaxCalculationService taxCalculationService;

    @Test
    @DisplayName("Deve calcular o imposto corretamente usando a estratégia apropriada para o tipo de imposto")
    void shouldCalculateTaxUsingCorrectStrategy() {
        // Arrange
        Long taxTypeId = 1L;
        Double baseValue = 1000.0;
        Double rate = 18.0;
        Double expectedTaxValue = 180.0;

        TaxType taxType = new TaxType();
        taxType.setId(taxTypeId);
        taxType.setName("ICMS");
        taxType.setDescription("Imposto sobre Circulação de Mercadorias e Serviços");
        taxType.setRate(rate);

        TaxCalculationRequestDto requestDto = new TaxCalculationRequestDto(taxTypeId, baseValue);

        IcmsTaxStrategy icmsStrategy = new IcmsTaxStrategy();

        when(taxTypeRepository.findById(taxTypeId)).thenReturn(Optional.of(taxType));
        when(strategyFactory.getStrategy("ICMS")).thenReturn(icmsStrategy);

        // Act
        TaxCalculationResponseDto result = taxCalculationService.calculate(requestDto);

        // Assert
        assertEquals("ICMS", result.tipoImposto());
        assertEquals(baseValue, result.valorBase());
        assertEquals(rate, result.aliquota());
        assertEquals(expectedTaxValue, result.valorImposto());

        // Verificar se a fábrica foi chamada com o nome do imposto correto
        verify(strategyFactory).getStrategy("ICMS");
    }
}