package com.minin.banks.services.user;

import com.minin.banks.dto.AuthDto;
import com.minin.banks.dto.EmailDto;
import com.minin.banks.dto.MobilePhoneDto;
import com.minin.banks.dto.UserDto;
import com.minin.banks.exceptions.AlreadyExistsException;
import com.minin.banks.exceptions.BadRequestException;
import com.minin.banks.exceptions.ForbiddenException;
import com.minin.banks.exceptions.ResourceNotFoundException;
import com.minin.banks.mappers.UserMapper;
import com.minin.banks.models.Account;
import com.minin.banks.models.Email;
import com.minin.banks.models.MobilePhone;
import com.minin.banks.models.User;
import com.minin.banks.properties.PatternProperties;
import com.minin.banks.repository.UserRepository;
import com.minin.banks.services.user.profile.email.EmailService;
import com.minin.banks.services.user.profile.mobilePhone.MobilePhoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final MobilePhoneService mobilePhoneService;
    private final EmailService emailService;

    private final PatternProperties patternProperties;

    @Override
    public User findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user_not_found"));
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("user_not_found"));
    }

    @Override
    public UserDto.Response.BaseInfo findByEmail(String emailValue) {

        Email email = emailService.findByEmail(emailValue);

        return userMapper.toBaseInfo(email.getUser());
    }

    @Override
    public UserDto.Response.BaseInfo findByMobilePhone(String mobilePhoneValue) {

        String validMobilePhone = formatMobilePhone(mobilePhoneValue);

        MobilePhone mobilePhone = mobilePhoneService.findByMobilePhone(validMobilePhone);

        return userMapper.toBaseInfo(mobilePhone.getUser());
    }

    @Override
    public List<UserDto.Response.BaseInfo> findByBirthDateAfter(Date birthDate, int page, int pageSize, String sort) {

        Sort.Direction sortDirection = getSortDirection(sort);
        Sort.Order order = new Sort.Order(sortDirection, "birthDate");

        return userMapper.toBaseInfoList(
                userRepository.findAllByBirthDateAfter(
                        birthDate,
                        PageRequest.of(page - 1, pageSize, Sort.by(order))
                ).toList()
        );
    }

    @Override
    public List<UserDto.Response.BaseInfo> findByFIOLike(String like, int page, int pageSize, String sort) {

        Sort.Direction sortDirection = getSortDirection(sort);

        String[] likeRequest = like.split(" ");

        switch (likeRequest.length) {
            case 1 -> {

                Sort.Order order = new Sort.Order(sortDirection, "surname");

                return userMapper.toBaseInfoList(
                        userRepository.findAllBySurnameLike(
                                like + "%",
                                PageRequest.of(page - 1, pageSize, Sort.by(order))
                        ).toList()
                );
            }
            case 2 -> {

                List<Sort.Order> orders = List.of(
                        new Sort.Order(sortDirection, "surname"),
                        new Sort.Order(sortDirection, "name")
                );

                return userMapper.toBaseInfoList(
                        userRepository.findAllBySurnameLikeAndNameLike(
                                likeRequest[0] + "%",
                                likeRequest[1] + "%",
                                PageRequest.of(page - 1, pageSize, Sort.by(orders))
                        ).toList()
                );
            }
            case 3 -> {

                List<Sort.Order> orders = List.of(
                        new Sort.Order(sortDirection, "surname"),
                        new Sort.Order(sortDirection, "name"),
                        new Sort.Order(sortDirection, "patronymic")
                );

                return userMapper.toBaseInfoList(
                        userRepository.findAllBySurnameLikeAndNameLikeAndPatronymicLike(
                                likeRequest[0] + "%",
                                likeRequest[1] + "%",
                                likeRequest[2] + "%",
                                PageRequest.of(page - 1, pageSize, Sort.by(orders))
                        ).toList()
                );
            }

        }

        return userMapper.toBaseInfoList(userRepository.findAll());
    }

    @Override
    public User create(AuthDto.Request.Registration registerDto, Account account, String codedPassword) {

        if (isAlreadyExistsByUsername(registerDto.getUsername())) {
            throw new AlreadyExistsException("username_already_exists");
        }

        if (!isEmailValid(registerDto.getEmail())) {
            throw new BadRequestException("invalid_email");
        }

        String validMobilePhone = formatMobilePhone(registerDto.getMobilePhone());

        if (validMobilePhone.isEmpty()) {
            throw new BadRequestException("invalid_mobile_phone");
        }

        User user = userMapper.toEntity(registerDto, codedPassword, account);
        user.setMobilePhones(new ArrayList<>());
        user.setEmails(new ArrayList<>());
        user = userRepository.save(user);

        mobilePhoneService.create(user, validMobilePhone);
        emailService.create(user, registerDto.getEmail());

        return user;
    }

    @Override
    public void addEmail(String username, EmailDto.Request.Create requestDto) {

        if (!isEmailValid(requestDto.getEmail())) {
            throw new BadRequestException("invalid_email");
        }

        User user = findByUsername(username);

        emailService.create(user, requestDto.getEmail());
    }

    @Override
    public void addMobilePhone(String username, MobilePhoneDto.Request.Create requestDto) {

        String validMobilePhone = formatMobilePhone(requestDto.getMobilePhone());

        if (validMobilePhone.isEmpty()) {
            throw new BadRequestException("invalid_mobile_phone");
        }

        User user = findByUsername(username);

        mobilePhoneService.create(user, validMobilePhone);
    }

    @Override
    public void updateMobilePhone(String username, MobilePhoneDto.Request.Update requestDto) {

        String validMobilePhone = formatMobilePhone(requestDto.getMobilePhone());
        if (validMobilePhone.isEmpty()) {
            throw new BadRequestException("invalid_mobile_phone");
        }

        User user = findByUsername(username);
        MobilePhone mobilePhone = mobilePhoneService.findById(requestDto.getId());

        if (!user.getMobilePhones().contains(mobilePhone)) {
            throw new ForbiddenException("you_cannot_change_this_mobile_phone");
        }

        if (mobilePhoneService.isMobilePhoneAlreadyExists(requestDto.getMobilePhone())) {
            throw new AlreadyExistsException("mobile_phone_already_exists");
        }

        mobilePhone.setMobilePhone(requestDto.getMobilePhone());
        mobilePhoneService.update(mobilePhone);
    }

    @Override
    public void updateEmail(String username, EmailDto.Request.Update requestDto) {

        if (!isEmailValid(requestDto.getEmail())) {
            throw new BadRequestException("invalid_email");
        }

        User user = findByUsername(username);
        Email email = emailService.findById(requestDto.getId());

        if (!user.getEmails().contains(email)) {
            throw new ForbiddenException("you_cannot_change_this_email");
        }

        if (emailService.isEmailAlreadyExists(requestDto.getEmail())) {
            throw new AlreadyExistsException("email_already_exists");
        }

        email.setEmail(requestDto.getEmail());
        emailService.update(email);
    }

    @Override
    public void deleteById(UUID id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteEmailById(String username, UUID id) {

        User user = findByUsername(username);
        List<Email> userEmails = user.getEmails();

        if (userEmails.size() == 1) {
            throw new BadRequestException("you_cannot_delete_last_email");
        }

        emailService.deleteById(id);
    }

    @Override
    public void deleteMobilePhoneById(String username, UUID id) {

        User user = findByUsername(username);
        List<MobilePhone> userMobilePhones = user.getMobilePhones();

        if (userMobilePhones.size() == 1) {
            throw new BadRequestException("you_cannot_delete_last_mobile_phone");
        }

        mobilePhoneService.deleteById(id);
    }

    @Override
    public boolean isAlreadyExistsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    private Sort.Direction getSortDirection(String sort) {
        return switch (sort) {
            case "asc" -> Sort.Direction.ASC;
            case "desc" -> Sort.Direction.DESC;
            default -> throw new BadRequestException("invalid_sorting_signature");
        };
    }

    private String formatMobilePhone(String mobilePhone) {

        StringBuilder builder = new StringBuilder();
        boolean hasPlus = false;
        boolean isValid = true;

        for (char ch : mobilePhone.trim().toCharArray()) {
            if (Character.isDigit(ch)) {
                builder.append(ch);
            } else if (ch == '+' && builder.isEmpty() && !hasPlus) {
                hasPlus = true;
                builder.append(ch);
            }
        }

        if (builder.charAt(0) == '+' && builder.charAt(1) != '7'
                || builder.charAt(0) == '+' && builder.length() != 12
                || builder.charAt(0) == '8' && builder.length() != 11) {
            isValid = false;
        }

        if (builder.length() == 11 && isValid) {
            return builder.substring(1);
        } else if (builder.length() == 12 && isValid) {
            return builder.substring(2);
        } else {
            return "";
        }
    }

    private boolean isEmailValid(String email) {
        Pattern patternForEmail = Pattern.compile(patternProperties.getEmailPattern(), Pattern.CASE_INSENSITIVE);
        return patternForEmail.matcher(email).matches();
    }

}