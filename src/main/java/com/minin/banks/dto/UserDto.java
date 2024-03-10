package com.minin.banks.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Value;

import java.util.Date;
import java.util.List;

public enum UserDto {;

    private interface Name { String getName(); }
    private interface Surname { String getSurname(); }
    private interface Patronymic { String getPatronymic(); }
    private interface BirthDate { @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Europe/Moscow") Date getBirthDate(); }

    private interface EmailsBaseInfo { List<EmailDto.Response.BaseInfo> getEmails(); }
    private interface MobilePhonesBaseInfo { List<MobilePhoneDto.Response.BaseInfo> getMobilePhones(); }

    public enum Response {;

        @Value
        public static class BaseInfo implements Name, Surname, Patronymic, BirthDate, EmailsBaseInfo, MobilePhonesBaseInfo{
            String name;
            String surname;
            String patronymic;
            Date birthDate;
            List<EmailDto.Response.BaseInfo> emails;
            List<MobilePhoneDto.Response.BaseInfo> mobilePhones;
        }

    }

}

