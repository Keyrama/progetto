package it.unifi.swam.cleanlabel.config;

import it.unifi.swam.cleanlabel.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class RoleGuard {

    public static final String ROLE_HEADER = "X-Mock-User-Role";

    /**
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