//package it.unifi.swam.cleanlabel.exception;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.FieldError;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.Map;
//
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
//
//    // ── 404 ───────────────────────────────────────────────────────────────────
//
//    @ExceptionHandler(ResourceNotFoundException.class)
//    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
//    }
//
//    // ── 400 — Bean Validation failures ────────────────────────────────────────
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
//        Map<String, String> fieldErrors = new HashMap<>();
//        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
//            fieldErrors.put(fe.getField(), fe.getDefaultMessage());
//        }
//        ErrorResponse body = new ErrorResponse(
//                HttpStatus.BAD_REQUEST.value(),
//                "Validation failed",
//                fieldErrors
//        );
//        return ResponseEntity.badRequest().body(body);
//    }
//
//    // ── 400 — Generic illegal argument ────────────────────────────────────────
//
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
//        log.warn("IllegalArgumentException: {}", ex.getMessage());
//        return ResponseEntity.badRequest()
//                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
//    }
//
//    // ── 500 ───────────────────────────────────────────────────────────────────
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
//        // Log completo dello stack trace — visibile nella console IntelliJ
//        log.error("Unhandled exception on request", ex);
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                        ex.getClass().getSimpleName() + ": " + ex.getMessage()));
//    }
//
//    // ── ErrorResponse record ──────────────────────────────────────────────────
//
//    public record ErrorResponse(
//            int status,
//            String message,
//            Map<String, String> fieldErrors,
//            LocalDateTime timestamp
//    ) {
//        public ErrorResponse(int status, String message) {
//            this(status, message, null, LocalDateTime.now());
//        }
//        public ErrorResponse(int status, String message, Map<String, String> fieldErrors) {
//            this(status, message, fieldErrors, LocalDateTime.now());
//        }
//    }
//}