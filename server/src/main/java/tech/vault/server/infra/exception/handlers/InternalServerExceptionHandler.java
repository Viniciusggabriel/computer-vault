package tech.vault.server.infra.exception.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import tech.vault.server.infra.exception.message.OperationStatus;

@ControllerAdvice
public class InternalServerExceptionHandler {
    protected ResponseEntity<OperationStatus> internalServerError() {
        OperationStatus operationStatus = new OperationStatus(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Ocorreu um erro interno no servidor.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(operationStatus);
    }
}
