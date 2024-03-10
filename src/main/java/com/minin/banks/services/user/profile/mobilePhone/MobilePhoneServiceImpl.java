package com.minin.banks.services.user.profile.mobilePhone;

import com.minin.banks.exceptions.AlreadyExistsException;
import com.minin.banks.exceptions.ResourceNotFoundException;
import com.minin.banks.mappers.MobilePhoneMapper;
import com.minin.banks.models.MobilePhone;
import com.minin.banks.models.User;
import com.minin.banks.repository.MobilePhoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MobilePhoneServiceImpl implements MobilePhoneService {

    private final MobilePhoneRepository mobilePhoneRepository;
    private final MobilePhoneMapper mobilePhoneMapper;

    @Override
    public MobilePhone findById(UUID id) {
        return mobilePhoneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("mobile_phone_not_found"));
    }

    @Override
    public MobilePhone findByMobilePhone(String mobilePhoneValue) {
        return mobilePhoneRepository.findByMobilePhone(mobilePhoneValue)
                .orElseThrow(() -> new ResourceNotFoundException("mobile_phone_not_found"));
    }

    @Override
    public MobilePhone create(User user, String mobilePhone) {

        if (isMobilePhoneAlreadyExists(mobilePhone)) {
            throw new AlreadyExistsException("mobile_phone_already_exists");
        }

        MobilePhone mobilePhoneToSave = mobilePhoneMapper.toEntity(mobilePhone, user);

        return mobilePhoneRepository.save(mobilePhoneToSave);
    }

    @Override
    public void update(MobilePhone mobilePhone) {
        mobilePhoneRepository.save(mobilePhone);
    }

    @Override
    public void deleteById(UUID id) {
        mobilePhoneRepository.deleteById(id);
    }

    @Override
    public boolean isMobilePhoneAlreadyExists(String mobilePhone) {
        return mobilePhoneRepository.existsByMobilePhone(mobilePhone);
    }

}
