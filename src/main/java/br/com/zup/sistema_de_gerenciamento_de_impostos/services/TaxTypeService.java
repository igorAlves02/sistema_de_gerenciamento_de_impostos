package br.com.zup.sistema_de_gerenciamento_de_impostos.services;

import br.com.zup.sistema_de_gerenciamento_de_impostos.models.TaxType;
import br.com.zup.sistema_de_gerenciamento_de_impostos.repositories.TaxTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public TaxType save(TaxType taxType) {
        if (taxTypeRepository.findByName(taxType.getName()).isPresent()) {
            throw new RuntimeException("Tipo de imposto com este nome já existe.");
        }
        return taxTypeRepository.save(taxType);
    }

    public void delete(Long id) {
        if (!taxTypeRepository.existsById(id)) {
            throw new RuntimeException("Tipo de imposto não encontrado.");
        }
        taxTypeRepository.deleteById(id);
    }
}