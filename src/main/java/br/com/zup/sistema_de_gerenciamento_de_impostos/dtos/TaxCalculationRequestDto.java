package br.com.zup.sistema_de_gerenciamento_de_impostos.dto;

public record TaxCalculationRequestDto(
        Long tipoImpostoId,
        Double valorBase
) {}