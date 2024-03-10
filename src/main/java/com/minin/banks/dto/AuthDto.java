package com.minin.banks.dto;

import lombok.Value;

import java.util.Date;

public enum AuthDto {;

    private interface Name { String getName(); }
    private interface Surname { String getSurname(); }
    private interface Patronymic { String getPatronymic(); }
    private interface Username { String getUsername(); }
    private interface Password { String getPassword(); }
    private interface StartBalance { long getStartBalance(); }
    private interface MobilePhone { String getMobilePhone(); }
    private interface Email { String getEmail(); }
    private interface BirthDate { Date getBirthDate(); }

    private interface AccessToken { String getAccessToken(); }

    public enum Request {;

        @Value
        public static class Registration implements Name, Surname, Patronymic, Username, Password, StartBalance, MobilePhone, Email, BirthDate {
            String name;
            String surname;
            String patronymic;
            String username;
            String password;
            long startBalance;
            String mobilePhone;
            String email;
            Date birthDate;

        }

        @Value
        public static class Login implements Username, Password {
            String username;
            String password;
        }

    }

    public enum Response {;

        @Value
        public static class Tokens implements AccessToken {
            String accessToken;
        }

    }

}
