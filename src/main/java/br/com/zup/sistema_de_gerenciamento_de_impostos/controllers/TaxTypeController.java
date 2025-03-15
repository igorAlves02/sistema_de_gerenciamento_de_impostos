package br.com.zup.sistema_de_gerenciamento_de_impostos.controllers;

import br.com.zup.sistema_de_gerenciamento_de_impostos.models.TaxType;
import br.com.zup.sistema_de_gerenciamento_de_impostos.services.TaxTypeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tipos")
public class TaxTypeController {

    @Autowired
    private TaxTypeService taxTypeService;

    @GetMapping
    public ResponseEntity<List<TaxType>> findAll() {
        List<TaxType> taxTypes = taxTypeService.findAll();
        return ResponseEntity.ok(taxTypes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaxType> findById(@PathVariable Long id) {
        Optional<TaxType> taxType = taxTypeService.findById(id);
        return taxType.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<TaxType> save(@Valid @RequestBody TaxType taxType) {
        TaxType savedTaxType = taxTypeService.save(taxType);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTaxType);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        taxTypeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}