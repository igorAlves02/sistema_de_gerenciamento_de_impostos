package br.com.zup.sistema_de_gerenciamento_de_impostos.dto;

public record TaxCalculationResponseDto(
        String tipoImposto,
        Double valorBase,
        Double aliquota,
        Double valorImposto
) {}