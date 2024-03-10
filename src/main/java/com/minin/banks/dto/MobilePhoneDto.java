package com.minin.banks.dto;

import lombok.Value;

import java.util.UUID;

public enum MobilePhoneDto {;

    private interface Id { UUID getId(); }
    private interface MobilePhone { String getMobilePhone(); }

    public enum Request {;

        @Value
        public static class Create implements MobilePhone {

            public Create() {}

            String mobilePhone = null;
        }

        @Value
        public static class Update implements Id, MobilePhone {
            UUID id;
            String mobilePhone;
        }

    }

    public enum Response {;

        @Value
        public static class BaseInfo implements MobilePhone {
            String mobilePhone;
        }

    }

}
