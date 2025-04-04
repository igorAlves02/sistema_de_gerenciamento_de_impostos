package br.com.zup.sistema_de_gerenciamento_de_impostos.exceptions;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}