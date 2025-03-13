package br.com.zup.sistema_de_gerenciamento_de_impostos.repositories;

import br.com.zup.sistema_de_gerenciamento_de_impostos.models.TaxType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaxTypeRepository extends JpaRepository<TaxType, Long> {
    Optional<TaxType> findByName(String name);
}