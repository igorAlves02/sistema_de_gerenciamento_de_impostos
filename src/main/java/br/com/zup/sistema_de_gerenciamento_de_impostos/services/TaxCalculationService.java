package br.com.zup.sistema_de_gerenciamento_de_impostos.services;

import br.com.zup.sistema_de_gerenciamento_de_impostos.dto.TaxCalculationRequestDto;
import br.com.zup.sistema_de_gerenciamento_de_impostos.dto.TaxCalculationResponseDto;
import br.com.zup.sistema_de_gerenciamento_de_impostos.exceptions.BadRequestException;
import br.com.zup.sistema_de_gerenciamento_de_impostos.exceptions.ResourceNotFoundException;
import br.com.zup.sistema_de_gerenciamento_de_impostos.models.TaxType;
import br.com.zup.sistema_de_gerenciamento_de_impostos.repositories.TaxTypeRepository;
import br.com.zup.sistema_de_gerenciamento_de_impostos.strategies.TaxCalculationStrategy;
import br.com.zup.sistema_de_gerenciamento_de_impostos.strategies.TaxCalculationStrategyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaxCalculationService {
    @Autowired
    private TaxTypeRepository taxTypeRepository;
    
    @Autowired
    private TaxCalculationStrategyFactory strategyFactory;
    
    public TaxCalculationResponseDto calculate(TaxCalculationRequestDto requestDto) {
        // Validação do valor base
        if (requestDto.valorBase() <= 0) {
            throw new BadRequestException("Valor base deve ser maior que zero.");
        }
        
        TaxType taxType = taxTypeRepository.findById(requestDto.tipoImpostoId())
                .orElseThrow(() -> new ResourceNotFoundException("TipoImposto", "id", requestDto.tipoImpostoId()));
        
        TaxCalculationStrategy strategy = strategyFactory.getStrategy(taxType.getName());
        double valorImposto = strategy.calculateTax(requestDto.valorBase());
        
        return new TaxCalculationResponseDto(
                taxType.getName(),
                requestDto.valorBase(),
                taxType.getRate(),
                valorImposto
        );
    }
}