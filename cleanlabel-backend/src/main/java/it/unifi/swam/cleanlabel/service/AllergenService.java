package it.unifi.swam.cleanlabel.service;

import it.unifi.swam.cleanlabel.dtos.AllergenDTO;
import it.unifi.swam.cleanlabel.exception.ResourceNotFoundException;
import it.unifi.swam.cleanlabel.mappers.AllergenMapper;
import it.unifi.swam.cleanlabel.model.Allergen;
import it.unifi.swam.cleanlabel.repository.AllergenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AllergenService {

    private final AllergenRepository allergenRepository;
    private final AllergenMapper allergenMapper;

    public List<AllergenDTO> findAll() {
        return allergenMapper.toDTOList(allergenRepository.findAll());
    }

    public AllergenDTO findById(Long id) {
        return allergenMapper.toDTO(getAllergenOrThrow(id));
    }

    @Transactional
    public AllergenDTO create(AllergenDTO dto) {
        Allergen allergen = allergenMapper.toEntity(dto);
        return allergenMapper.toDTO(allergenRepository.save(allergen));
    }

    @Transactional
    public AllergenDTO update(Long id, AllergenDTO dto) {
        Allergen allergen = getAllergenOrThrow(id);
        allergen.setName(dto.getName());
        allergen.setCode(dto.getCode());
        allergen.setDescription(dto.getDescription());
        return allergenMapper.toDTO(allergenRepository.save(allergen));
    }

    @Transactional
    public void delete(Long id) {
        getAllergenOrThrow(id);
        allergenRepository.deleteById(id);
    }

    // ── Internal helper ───────────────────────────────────────────────────────

    public Allergen getAllergenOrThrow(Long id) {
        return allergenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Allergen", id));
    }
}