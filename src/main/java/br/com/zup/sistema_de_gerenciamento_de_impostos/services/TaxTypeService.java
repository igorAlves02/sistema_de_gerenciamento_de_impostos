package br.com.zup.sistema_de_gerenciamento_de_impostos.services;

import br.com.zup.sistema_de_gerenciamento_de_impostos.exceptions.DuplicateResourceException;
import br.com.zup.sistema_de_gerenciamento_de_impostos.exceptions.ResourceNotFoundException;
import br.com.zup.sistema_de_gerenciamento_de_impostos.models.TaxType;
import br.com.zup.sistema_de_gerenciamento_de_impostos.repositories.TaxTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TaxTypeService {
    @Autowired
    private TaxTypeRepository taxTypeRepository;
    
    public List<TaxType> findAll() {
        return taxTypeRepository.findAll();
    }
    
    public Optional<TaxType> findById(Long id) {
        return taxTypeRepository.findById(id);
    }
    
    @Transactional
    public TaxType save(TaxType taxType) {
        if (taxTypeRepository.findByName(taxType.getName()).isPresent()) {
            throw new DuplicateResourceException("TipoImposto", "nome", taxType.getName());
        }
        return taxTypeRepository.save(taxType);
    }
    
    @Transactional
    public void delete(Long id) {
        if (!taxTypeRepository.existsById(id)) {
            throw new ResourceNotFoundException("TipoImposto", "id", id);
        }
        taxTypeRepository.deleteById(id);
    }
    
    // Método auxiliar que pode ser útil
    public TaxType findByIdOrThrow(Long id) {
        return taxTypeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("TipoImposto", "id", id));
    }
}