package it.unifi.swam.cleanlabel.config;

import it.unifi.swam.cleanlabel.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Mock role-based access control guard.
 *
 * Real authentication is not implemented per assignment requirements.
 * Instead, callers include the header:
 *
 *   X-Mock-User-Role: CONSUMER | SPECIALIST | CORPORATE
 *
 * If the header is absent, the request is allowed through (open access mode,
 * useful for testing with curl or Postman without setting headers).
 * If the header is present but the role is not in the allowed list, a 403 is thrown.
 *
 * Usage in controllers:
 *   roleGuard.require(request, User.Role.SPECIALIST, User.Role.CORPORATE);
 */
@Component
public class RoleGuard {

    public static final String ROLE_HEADER = "X-Mock-User-Role";

    /**
     * Checks that the role declared in X-Mock-User-Role is among the allowed ones.
     * No-op if the header is absent.
     *
     * @param request  the incoming HTTP request
     * @param allowed  one or more roles that are permitted to perform this operation
     * @throws ResponseStatusException 403 if the declared role is not allowed
     * @throws ResponseStatusException 400 if the header value is not a valid Role
     */
    public void require(HttpServletRequest request, User.Role... allowed) {
        String headerValue = request.getHeader(ROLE_HEADER);

        // Header absent — open access (mock mode with no role simulation)
        if (headerValue == null || headerValue.isBlank()) return;

        User.Role declared = parseRole(headerValue);

        boolean permitted = Arrays.asList(allowed).contains(declared);
        if (!permitted) {
            String allowedNames = Arrays.stream(allowed)
                    .map(Enum::name)
                    .collect(Collectors.joining(", "));
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Role '" + declared.name() + "' is not authorized for this operation. " +
                            "Required: " + allowedNames + "."
            );
        }
    }

    /**
     * Returns the role declared in the request header, or null if absent.
     * Useful for read operations that want to tailor the response by role
     * without blocking access entirely.
     */
    public User.Role extract(HttpServletRequest request) {
        String headerValue = request.getHeader(ROLE_HEADER);
        if (headerValue == null || headerValue.isBlank()) return null;
        return parseRole(headerValue);
    }

    private User.Role parseRole(String value) {
        try {
            return User.Role.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid value for header " + ROLE_HEADER + ": '" + value + "'. " +
                            "Allowed values: CONSUMER, SPECIALIST, CORPORATE."
            );
        }
    }
}