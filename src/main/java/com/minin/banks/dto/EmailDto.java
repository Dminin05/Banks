package com.minin.banks.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.Value;
import java.util.UUID;

public enum EmailDto {;

    private interface Id { UUID getId(); }
    private interface Email { String getEmail(); }

    public enum Request {;

        @Value
        public static class Create implements Email {

            public Create() {}

            String email = null;
        }

        @Value
        public static class Update implements Id, Email {
            UUID id;
            String email;
        }

    }

    public enum Response {;

        @Value
        public static class BaseInfo implements Email {
            String email;
        }

    }

}
