package br.com.zup.sistema_de_gerenciamento_de_impostos.controllers;

import br.com.zup.sistema_de_gerenciamento_de_impostos.models.TaxType;
import br.com.zup.sistema_de_gerenciamento_de_impostos.services.TaxTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tipos")
public class TaxTypeController {
    
    @Autowired
    private TaxTypeService taxTypeService;
    
    @GetMapping
    public ResponseEntity<List<TaxType>> findAll() {
        return null;
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TaxType> findById(@PathVariable Long id) {
        return null;
    }
    
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<TaxType> save(@RequestBody TaxType taxType) {
        return null;
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return null;
    }
}