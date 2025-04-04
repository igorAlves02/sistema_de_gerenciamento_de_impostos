package br.com.zup.sistema_de_gerenciamento_de_impostos.services;

import br.com.zup.sistema_de_gerenciamento_de_impostos.exceptions.DuplicateResourceException;
import br.com.zup.sistema_de_gerenciamento_de_impostos.exceptions.ResourceNotFoundException;
import br.com.zup.sistema_de_gerenciamento_de_impostos.models.TaxType;
import br.com.zup.sistema_de_gerenciamento_de_impostos.repositories.TaxTypeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaxTypeServiceTest {

    @Mock
    private TaxTypeRepository taxTypeRepository;

    @InjectMocks
    private TaxTypeService taxTypeService;

    @Test
    @DisplayName("Deve retornar todos os tipos de impostos")
    void shouldReturnAllTaxTypes() {
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
        
        when(taxTypeRepository.findAll()).thenReturn(taxTypes);

        // Act
        List<TaxType> result = taxTypeService.findAll();

        // Assert
        assertAll(
            () -> assertNotNull(result, "A lista não deve ser nula"),
            () -> assertEquals(2, result.size(), "Deve retornar 2 tipos de impostos"),
            () -> assertEquals("ICMS", result.get(0).getName(), "O primeiro tipo deve ser ICMS"),
            () -> assertEquals("ISS", result.get(1).getName(), "O segundo tipo deve ser ISS")
        );
        
        verify(taxTypeRepository).findAll();
    }

    @Test
    @DisplayName("Deve retornar um tipo de imposto quando buscar por ID existente")
    void shouldReturnTaxTypeWhenIdExists() {
        // Arrange
        Long id = 1L;
        TaxType taxType = new TaxType();
        taxType.setId(id);
        taxType.setName("ICMS");
        taxType.setDescription("Imposto sobre Circulação de Mercadorias e Serviços");
        taxType.setRate(18.0);
        
        when(taxTypeRepository.findById(id)).thenReturn(Optional.of(taxType));

        // Act
        Optional<TaxType> result = taxTypeService.findById(id);

        // Assert
        assertAll(
            () -> assertTrue(result.isPresent(), "Deve retornar um Optional com valor"),
            () -> assertEquals("ICMS", result.get().getName(), "O nome deve ser ICMS"),
            () -> assertEquals(18.0, result.get().getRate(), "A alíquota deve ser 18.0")
        );
        
        verify(taxTypeRepository).findById(id);
    }

    @Test
    @DisplayName("Deve retornar Optional vazio quando buscar por ID inexistente")
    void shouldReturnEmptyOptionalWhenIdDoesNotExist() {
        // Arrange
        Long id = 99L;
        when(taxTypeRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<TaxType> result = taxTypeService.findById(id);

        // Assert
        assertFalse(result.isPresent(), "Deve retornar um Optional vazio");
        
        verify(taxTypeRepository).findById(id);
    }

    @Test
    @DisplayName("Deve salvar um novo tipo de imposto")
    void shouldSaveNewTaxType() {
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
        
        when(taxTypeRepository.findByName(taxTypeToSave.getName())).thenReturn(Optional.empty());
        when(taxTypeRepository.save(taxTypeToSave)).thenReturn(savedTaxType);

        // Act
        TaxType result = taxTypeService.save(taxTypeToSave);

        // Assert
        assertAll(
            () -> assertNotNull(result, "O resultado não deve ser nulo"),
            () -> assertEquals(3L, result.getId(), "O ID deve ser 3"),
            () -> assertEquals("IPI", result.getName(), "O nome deve ser IPI"),
            () -> assertEquals(12.0, result.getRate(), "A alíquota deve ser 12.0")
        );
        
        verify(taxTypeRepository).findByName(taxTypeToSave.getName());
        verify(taxTypeRepository).save(taxTypeToSave);
    }

    @Test
    @DisplayName("Deve lançar DuplicateResourceException quando tentar salvar tipo de imposto com nome duplicado")
    void shouldThrowDuplicateResourceExceptionWhenSavingDuplicateTaxTypeName() {
        // Arrange
        TaxType existingTaxType = new TaxType();
        existingTaxType.setId(1L);
        existingTaxType.setName("ICMS");
        existingTaxType.setDescription("Imposto Existente");
        existingTaxType.setRate(18.0);

        TaxType newTaxType = new TaxType();
        newTaxType.setName("ICMS");
        newTaxType.setDescription("Novo Imposto com Mesmo Nome");
        newTaxType.setRate(20.0);
        
        when(taxTypeRepository.findByName("ICMS")).thenReturn(Optional.of(existingTaxType));

        // Act & Assert
        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class, () -> {
            taxTypeService.save(newTaxType);
        });
        
        assertTrue(exception.getMessage().contains("já existe"));
        
        verify(taxTypeRepository).findByName("ICMS");
        verify(taxTypeRepository, never()).save(any(TaxType.class));
    }

    @Test
    @DisplayName("Deve excluir um tipo de imposto existente")
    void shouldDeleteExistingTaxType() {
        // Arrange
        Long id = 1L;
        when(taxTypeRepository.existsById(id)).thenReturn(true);
        doNothing().when(taxTypeRepository).deleteById(id);

        // Act & Assert
        assertDoesNotThrow(() -> {
            taxTypeService.delete(id);
        });
        
        verify(taxTypeRepository).existsById(id);
        verify(taxTypeRepository).deleteById(id);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao tentar excluir tipo de imposto inexistente")
    void shouldThrowResourceNotFoundExceptionWhenDeletingNonExistentTaxType() {
        // Arrange
        Long id = 99L;
        when(taxTypeRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            taxTypeService.delete(id);
        });
        
        assertTrue(exception.getMessage().contains("não encontrado"));
        
        verify(taxTypeRepository).existsById(id);
        verify(taxTypeRepository, never()).deleteById(anyLong());
    }
    
    @Test
    @DisplayName("findByIdOrThrow deve retornar o tipo de imposto quando existir")
    void findByIdOrThrowShouldReturnTaxTypeWhenExists() {
        // Arrange
        Long id = 1L;
        TaxType taxType = new TaxType();
        taxType.setId(id);
        taxType.setName("ICMS");
        
        when(taxTypeRepository.findById(id)).thenReturn(Optional.of(taxType));
        
        // Act
        TaxType result = taxTypeService.findByIdOrThrow(id);
        
        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("ICMS", result.getName());
    }

    @Test
    @DisplayName("findByIdOrThrow deve lançar ResourceNotFoundException quando não existir")
    void findByIdOrThrowShouldThrowResourceNotFoundExceptionWhenNotExists() {
        // Arrange
        Long id = 999L;
        when(taxTypeRepository.findById(id)).thenReturn(Optional.empty());
        
        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            taxTypeService.findByIdOrThrow(id);
        });
        
        assertTrue(exception.getMessage().contains("não encontrado"));
    }
}