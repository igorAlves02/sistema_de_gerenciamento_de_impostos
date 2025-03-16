package br.com.zup.sistema_de_gerenciamento_de_impostos.controllers;

import br.com.zup.sistema_de_gerenciamento_de_impostos.models.TaxType;
import br.com.zup.sistema_de_gerenciamento_de_impostos.services.TaxTypeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaxTypeControllerTest {

    @Mock
    private TaxTypeService taxTypeService;

    @InjectMocks
    private TaxTypeController taxTypeController;

    @Test
    @DisplayName("Deve retornar lista de tipos de impostos com status 200")
    void shouldReturnTaxTypeList() {
        // Arrange
        TaxType icms = new TaxType();
        icms.setId(1L);
        icms.setName("ICMS");
        icms.setDescription("Imposto sobre Circulação de Mercadorias e Serviços");
        icms.setRate(18.0);

        TaxType iss = new TaxType();
        iss.setId(2L);
        iss.setName("ISS");
        iss.setDescription("Imposto sobre Serviços");
        iss.setRate(5.0);

        List<TaxType> taxTypes = Arrays.asList(icms, iss);
        
        when(taxTypeService.findAll()).thenReturn(taxTypes);

        // Act
        ResponseEntity<List<TaxType>> response = taxTypeController.findAll();

        // Assert
        assertAll(
            () -> assertNotNull(response, "A resposta não deve ser nula"),
            () -> assertNotNull(response.getBody(), "O corpo da resposta não deve ser nulo"),
            () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
            () -> assertEquals(2, response.getBody().size()),
            () -> assertEquals("ICMS", response.getBody().get(0).getName()),
            () -> assertEquals("ISS", response.getBody().get(1).getName())
        );
    }
    
    @Test
    @DisplayName("Deve retornar lista vazia quando não existem tipos de impostos")
    void shouldReturnEmptyListWhenNoTaxTypesExist() {
        // Arrange
        List<TaxType> emptyList = new ArrayList<>();
        when(taxTypeService.findAll()).thenReturn(emptyList);

        // Act
        ResponseEntity<List<TaxType>> response = taxTypeController.findAll();

        // Assert
        assertAll(
            () -> assertNotNull(response, "A resposta não deve ser nula"),
            () -> assertNotNull(response.getBody(), "O corpo da resposta não deve ser nulo"),
            () -> assertEquals(HttpStatus.OK, response.getStatusCode(), "O status deve ser 200 OK"),
            () -> assertTrue(response.getBody().isEmpty(), "A lista deve estar vazia")
        );
        
        verify(taxTypeService).findAll();
    }

    @Test
    @DisplayName("Deve retornar tipo de imposto por ID com status 200")
    void shouldReturnTaxTypeById() {
        // Arrange
        Long id = 1L;
        TaxType taxType = new TaxType();
        taxType.setId(id);
        taxType.setName("ICMS");
        taxType.setDescription("Imposto sobre Circulação de Mercadorias e Serviços");
        taxType.setRate(18.0);
        
        when(taxTypeService.findById(id)).thenReturn(Optional.of(taxType));

        // Act
        ResponseEntity<TaxType> response = taxTypeController.findById(id);

        // Assert
        assertAll(
            () -> assertNotNull(response, "A resposta não deve ser nula"),
            () -> assertNotNull(response.getBody(), "O corpo da resposta não deve ser nulo"),
            () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
            () -> assertEquals(id, response.getBody().getId()),
            () -> assertEquals("ICMS", response.getBody().getName())
        );
    }

    @Test
    @DisplayName("Deve retornar status 404 quando tipo de imposto não é encontrado")
    void shouldReturnNotFoundWhenTaxTypeDoesNotExist() {
        // Arrange
        Long id = 999L;
        when(taxTypeService.findById(id)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<TaxType> response = taxTypeController.findById(id);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() == null, "Corpo da resposta deve ser nulo para 404");
    }

    @Test
    @DisplayName("Deve salvar tipo de imposto e retornar status 201")
    void shouldSaveTaxType() {
        // Arrange
        TaxType taxTypeToSave = new TaxType();
        taxTypeToSave.setName("IPI");
        taxTypeToSave.setDescription("Imposto sobre Produtos Industrializados");
        taxTypeToSave.setRate(12.0);

        TaxType savedTaxType = new TaxType();
        savedTaxType.setId(3L);
        savedTaxType.setName("IPI");
        savedTaxType.setDescription("Imposto sobre Produtos Industrializados");
        savedTaxType.setRate(12.0);
        
        when(taxTypeService.save(any(TaxType.class))).thenReturn(savedTaxType);

        // Act
        ResponseEntity<TaxType> response = taxTypeController.save(taxTypeToSave);

        // Assert
        assertAll(
            () -> assertNotNull(response, "A resposta não deve ser nula"),
            () -> assertNotNull(response.getBody(), "O corpo da resposta não deve ser nulo"),
            () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
            () -> assertEquals(3L, response.getBody().getId()),
            () -> assertEquals("IPI", response.getBody().getName())
        );
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando tentar salvar tipo de imposto com nome duplicado")
    void shouldHandleExceptionWhenSavingDuplicateTaxType() {
        // Arrange
        TaxType taxType = new TaxType();
        taxType.setName("ICMS");
        taxType.setDescription("Imposto duplicado");
        taxType.setRate(18.0);
        
        when(taxTypeService.save(any(TaxType.class))).thenThrow(new RuntimeException("Tipo de imposto com este nome já existe."));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            taxTypeController.save(taxType);
        });
        
        verify(taxTypeService).save(any(TaxType.class));
    }

    @Test
    @DisplayName("Deve excluir tipo de imposto e retornar status 204")
    void shouldDeleteTaxType() {
        // Arrange
        Long id = 1L;
        doNothing().when(taxTypeService).delete(id);

        // Act
        ResponseEntity<Void> response = taxTypeController.delete(id);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(taxTypeService, times(1)).delete(id);
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando tentar excluir tipo de imposto inexistente")
    void shouldHandleExceptionWhenDeletingNonExistentTaxType() {
        // Arrange
        Long id = 999L;
        doThrow(new RuntimeException("Tipo de imposto não encontrado.")).when(taxTypeService).delete(id);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            taxTypeController.delete(id);
        });
        
        verify(taxTypeService).delete(id);
    }
}