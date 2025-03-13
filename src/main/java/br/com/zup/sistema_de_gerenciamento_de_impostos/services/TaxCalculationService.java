package br.com.zup.sistema_de_gerenciamento_de_impostos.services;

import br.com.zup.sistema_de_gerenciamento_de_impostos.dto.TaxCalculationRequestDto;
import br.com.zup.sistema_de_gerenciamento_de_impostos.dto.TaxCalculationResponseDto;
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
        TaxType taxType = taxTypeRepository.findById(requestDto.tipoImpostoId())
                .orElseThrow(() -> new RuntimeException("Tipo de imposto não encontrado."));

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