package com.minin.banks.services.user.profile.email;

import com.minin.banks.models.Email;
import com.minin.banks.models.User;

import java.util.UUID;

public interface EmailService {

    Email findById(UUID id);

    Email findByEmail(String email);

    Email create(User user, String email);

    void update(Email email);

    void deleteById(UUID id);

    boolean isEmailAlreadyExists(String email);

}
