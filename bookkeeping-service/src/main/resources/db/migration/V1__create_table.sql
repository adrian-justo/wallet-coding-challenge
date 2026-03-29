create table account (
    id varchar(255) primary key,
    created_at datetime(6) not null
);

create table transaction (
    id varchar(255) primary key,
    account_id varchar(255) not null,
    amount decimal(19,2) not null,
    group_id varchar(255) not null,
    created_at datetime(6) not null,
    constraint fk_account foreign key (account_id) references account(id)
);