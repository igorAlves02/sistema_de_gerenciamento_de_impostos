package br.com.zup.sistema_de_gerenciamento_de_impostos.exceptions;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}