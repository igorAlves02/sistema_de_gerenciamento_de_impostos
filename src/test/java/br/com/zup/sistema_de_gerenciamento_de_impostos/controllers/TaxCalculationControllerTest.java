package br.com.zup.sistema_de_gerenciamento_de_impostos.controllers;

import br.com.zup.sistema_de_gerenciamento_de_impostos.dto.TaxCalculationRequestDto;
import br.com.zup.sistema_de_gerenciamento_de_impostos.dto.TaxCalculationResponseDto;
import br.com.zup.sistema_de_gerenciamento_de_impostos.exceptions.BadRequestException;
import br.com.zup.sistema_de_gerenciamento_de_impostos.exceptions.ResourceNotFoundException;
import br.com.zup.sistema_de_gerenciamento_de_impostos.services.TaxCalculationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaxCalculationControllerTest {
    @Mock
    private TaxCalculationService taxCalculationService;
    
    @InjectMocks
    private TaxCalculationController taxCalculationController;
    
    @Test
    @DisplayName("Deve calcular imposto e retornar resultado com status 200")
    void shouldCalculateTaxAndReturnResult() {
        // Arrange
        TaxCalculationRequestDto requestDto = new TaxCalculationRequestDto(1L, 1000.0);
        TaxCalculationResponseDto responseDto = new TaxCalculationResponseDto(
                "ICMS",
                1000.0,
                18.0,
                180.0
        );
        when(taxCalculationService.calculate(any(TaxCalculationRequestDto.class))).thenReturn(responseDto);
        
        // Act
        ResponseEntity<TaxCalculationResponseDto> response = taxCalculationController.calculate(requestDto);
        
        // Assert
        assertAll(
                () -> assertNotNull(response, "A resposta não deve ser nula"),
                () -> assertNotNull(response.getBody(), "O corpo da resposta não deve ser nulo"),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode(), "Status HTTP deve ser 200 OK"),
                () -> assertEquals("ICMS", response.getBody().tipoImposto(), "Tipo de imposto deve ser ICMS"),
                () -> assertEquals(1000.0, response.getBody().valorBase(), "Valor base deve ser 1000.0"),
                () -> assertEquals(18.0, response.getBody().aliquota(), "Alíquota deve ser 18.0%"),
                () -> assertEquals(180.0, response.getBody().valorImposto(), "Valor do imposto deve ser 180.0")
        );
    }
    
    @Test
    @DisplayName("Deve lançar BadRequestException quando o valor base for menor ou igual a zero")
    void shouldThrowBadRequestExceptionWhenBaseValueIsZeroOrNegative() {
        // Arrange
        TaxCalculationRequestDto requestDto = new TaxCalculationRequestDto(1L, 0.0);
        when(taxCalculationService.calculate(any(TaxCalculationRequestDto.class)))
                .thenThrow(new BadRequestException("Valor base deve ser maior que zero."));
        
        // Act & Assert
        assertThrows(BadRequestException.class, () -> {
            taxCalculationController.calculate(requestDto);
        }, "Deve lançar BadRequestException quando o valor base for inválido");
    }
    
    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando o tipo de imposto não existir")
    void shouldThrowResourceNotFoundExceptionWhenTaxTypeDoesNotExist() {
        // Arrange
        Long taxTypeId = 999L;
        TaxCalculationRequestDto requestDto = new TaxCalculationRequestDto(taxTypeId, 1000.0);
        when(taxCalculationService.calculate(any(TaxCalculationRequestDto.class)))
                .thenThrow(new ResourceNotFoundException("TipoImposto", "id", taxTypeId));
        
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            taxCalculationController.calculate(requestDto);
        }, "Deve lançar ResourceNotFoundException quando o tipo de imposto não existir");
    }
}