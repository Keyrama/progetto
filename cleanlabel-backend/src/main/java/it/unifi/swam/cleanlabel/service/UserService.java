package it.unifi.swam.cleanlabel.service;

import it.unifi.swam.cleanlabel.dtos.UserDTO;
import it.unifi.swam.cleanlabel.exception.ResourceNotFoundException;
import it.unifi.swam.cleanlabel.mappers.UserMapper;
import it.unifi.swam.cleanlabel.model.User;
import it.unifi.swam.cleanlabel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserDTO> findAll() {
        return userMapper.toDTOList(userRepository.findAll());
    }

    public UserDTO findById(Long id) {
        return userMapper.toDTO(getUserOrThrow(id));
    }

    @Transactional
    public UserDTO create(UserDTO dto) {
        User user = userMapper.toEntity(dto);
        return userMapper.toDTO(userRepository.save(user));
    }

    @Transactional
    public UserDTO update(Long id, UserDTO dto) {
        User user = getUserOrThrow(id);
        userMapper.updateEntity(dto, user);
        return userMapper.toDTO(userRepository.save(user));
    }

    @Transactional
    public void delete(Long id) {
        getUserOrThrow(id);
        userRepository.deleteById(id);
    }

    // ── Mock auth ─────────────────────────────────────────────────────────────

    /**
     * Returns a pre-built mock user for the given role string.
     * Used by the FE to simulate a logged-in session without implementing
     * real authentication, as per assignment requirements.
     *
     * If a user with the mock username already exists in the DB it is returned,
     * otherwise a transient (non-persisted) instance is returned so the method
     * works even with an empty database.
     */
    public UserDTO getMockUser(String roleStr) {
        User.Role role = parseRole(roleStr);

        String mockUsername = "mock_" + role.name().toLowerCase();

        return userRepository.findByUsernameIgnoreCase(mockUsername)
                .map(userMapper::toDTO)
                .orElseGet(() -> buildTransientMockUser(role, mockUsername));
    }

    // ── Internal helpers ──────────────────────────────────────────────────────

    private UserDTO buildTransientMockUser(User.Role role, String username) {
        return UserDTO.builder()
                .id(null) // transient — no DB entry
                .username(username)
                .email(username + "@cleanlabel.mock")
                .role(role)
                .build();
    }

    private User.Role parseRole(String roleStr) {
        try {
            return User.Role.valueOf(roleStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Invalid role: '" + roleStr + "'. Allowed: CONSUMER, SPECIALIST, CORPORATE");
        }
    }

    private User getUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }
}