package br.com.zup.sistema_de_gerenciamento_de_impostos.exceptions;

public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
    
    public DuplicateResourceException(String resource, String field, Object value) {
        super(String.format("%s com %s '%s' já existe", resource, field, value));
    }
}