package br.com.zup.sistema_de_gerenciamento_de_impostos.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    public ResourceNotFoundException(String resource, String field, Object value) {
        super(String.format("%s não encontrado com %s: %s", resource, field, value));
    }
}