package com.minin.banks.services.user.profile.email;

import com.minin.banks.exceptions.AlreadyExistsException;
import com.minin.banks.exceptions.ResourceNotFoundException;
import com.minin.banks.mappers.EmailMapper;
import com.minin.banks.models.Email;
import com.minin.banks.models.User;
import com.minin.banks.repository.EmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final EmailRepository emailRepository;
    private final EmailMapper emailMapper;

    @Override
    public Email findById(UUID id) {
        return emailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("email_not_found"));
    }

    @Override
    public Email findByEmail(String email) {
        return emailRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("email_not_found"));
    }

    @Override
    public Email create(User user, String email) {

        if (isEmailAlreadyExists(email)) {
            throw new AlreadyExistsException("email_already_exists");
        }

        Email emailToSave = emailMapper.toEntity(email, user);

        return emailRepository.save(emailToSave);
    }

    @Override
    public void update(Email email) {
        emailRepository.save(email);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        emailRepository.deleteById(id);
    }

    @Override
    public boolean isEmailAlreadyExists(String email) {
        return emailRepository.existsByEmail(email);
    }

}
