create table accounts
(
    id      uuid not null
        constraint accounts_pk
            primary key,
    balance bigserial
);

alter table accounts
    owner to postgres;

create table users
(
    id         uuid not null
        constraint users_pk
            primary key,
    account_id uuid not null
        constraint users_accounts_id_fk
            references accounts,
    name       text not null,
    surname    text not null,
    patronymic text not null,
    username   text not null,
    password   text not null,
    birth_date date not null
);

alter table users
    owner to postgres;

create table mobile_phones
(
    id           uuid not null
        constraint mobile_phones_pk
            primary key,
    user_id      uuid not null
        constraint mobile_phones_users_id_fk
            references users,
    mobile_phone text not null
);

alter table mobile_phones
    owner to postgres;

create table emails
(
    id      uuid not null
        constraint emails_pk
            primary key,
    user_id uuid not null
        constraint emails_users_id_fk
            references users,
    email   text not null
);

alter table emails
    owner to postgres;

