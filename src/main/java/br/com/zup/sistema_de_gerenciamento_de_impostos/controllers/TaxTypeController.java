package br.com.zup.sistema_de_gerenciamento_de_impostos.controllers;

import br.com.zup.sistema_de_gerenciamento_de_impostos.models.TaxType;
import br.com.zup.sistema_de_gerenciamento_de_impostos.services.TaxTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tipos")
@Tag(name = "Tipos de Impostos", description = "Endpoints para gerenciamento de tipos de impostos")
public class TaxTypeController {
    @Autowired
    private TaxTypeService taxTypeService;
    
    @GetMapping
    @Operation(
        summary = "Lista todos os tipos de impostos",
        description = "Retorna uma lista de todos os tipos de impostos cadastrados",
        responses = {
            @ApiResponse(responseCode = "200", description = "Operação bem-sucedida")
        }
    )
    public ResponseEntity<List<TaxType>> findAll() {
        List<TaxType> taxTypes = taxTypeService.findAll();
        return ResponseEntity.ok(taxTypes);
    }
    
    @GetMapping("/{id}")
    @Operation(
        summary = "Obtém um tipo de imposto pelo ID",
        description = "Retorna os detalhes de um tipo de imposto específico",
        responses = {
            @ApiResponse(responseCode = "200", description = "Operação bem-sucedida"),
            @ApiResponse(responseCode = "404", description = "Tipo de imposto não encontrado", content = @Content)
        }
    )
    public ResponseEntity<TaxType> findById(@PathVariable Long id) {
        // Usando o método auxiliar que lança exceção
        TaxType taxType = taxTypeService.findByIdOrThrow(id);
        return ResponseEntity.ok(taxType);
    }
    
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(
        summary = "Cadastra um novo tipo de imposto",
        description = "Cadastra um novo tipo de imposto. Requer permissão de ADMIN.",
        security = @SecurityRequirement(name = "bearerAuth"),
        responses = {
            @ApiResponse(responseCode = "201", description = "Tipo de imposto criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Tipo de imposto já existe", content = @Content)
        }
    )
    public ResponseEntity<TaxType> save(@Valid @RequestBody TaxType taxType) {
        TaxType savedTaxType = taxTypeService.save(taxType);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTaxType);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(
        summary = "Exclui um tipo de imposto",
        description = "Exclui um tipo de imposto pelo ID. Requer permissão de ADMIN.",
        security = @SecurityRequirement(name = "bearerAuth"),
        responses = {
            @ApiResponse(responseCode = "204", description = "Tipo de imposto excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Tipo de imposto não encontrado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
        }
    )
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        taxTypeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}