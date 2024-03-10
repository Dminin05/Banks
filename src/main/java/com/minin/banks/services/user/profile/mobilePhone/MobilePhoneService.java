package com.minin.banks.services.user.profile.mobilePhone;

import com.minin.banks.models.MobilePhone;
import com.minin.banks.models.User;

import java.util.UUID;

public interface MobilePhoneService {

    MobilePhone findById(UUID id);

    MobilePhone findByMobilePhone(String mobilePhoneValue);

    MobilePhone create(User user, String mobilePhone);

    void update(MobilePhone mobilePhone);

    void deleteById(UUID id);

    boolean isMobilePhoneAlreadyExists(String mobilePhone);

}
