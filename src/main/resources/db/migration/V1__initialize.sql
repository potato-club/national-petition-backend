create table board
(
    id                   bigint auto_increment
        primary key,
    created_date         datetime(6)   null,
    modified_date        datetime(6)   null,
    board_comment_counts int default 0 not null,
    category             varchar(255)  null,
    content              varchar(255)  null,
    is_deleted           bit           null,
    member_id            bigint        not null,
    petition_content     text          not null,
    petition_title       varchar(255)  not null,
    petition_url         varchar(255)  null,
    petitions_count      varchar(255)  null,
    title                varchar(100)  null,
    view_counts          int default 0 not null
);

create table board_like
(
    id            bigint auto_increment
        primary key,
    created_date  datetime(6)  null,
    modified_date datetime(6)  null,
    board_id      bigint       not null,
    board_state   varchar(255) not null,
    member_id     bigint       not null,
    constraint UKnb8ynsroigq12i6dl09dh1ipw
        unique (board_id, member_id)
);

create table comment
(
    id            bigint auto_increment
        primary key,
    created_date  datetime(6)  null,
    modified_date datetime(6)  null,
    board_id      bigint       null,
    content       varchar(255) null,
    depth         int          null,
    is_deleted    bit          not null,
    member_id     bigint       null,
    parent_id     bigint       null
);

create table delete_member
(
    id        bigint auto_increment
        primary key,
    email     varchar(255) not null,
    name      varchar(20)  not null,
    nick_name varchar(20)  not null,
    picture   varchar(255) not null
);

create table like_comment
(
    id                  bigint auto_increment
        primary key,
    created_date        datetime(6) null,
    modified_date       datetime(6) null,
    comment_id          bigint      null,
    like_comment_status int         null,
    member_id           bigint      null
);

create table member
(
    id            bigint auto_increment
        primary key,
    created_date  datetime(6)  null,
    modified_date datetime(6)  null,
    email         varchar(255) not null,
    name          varchar(20)  not null,
    nick_name     varchar(20)  null,
    picture       varchar(255) not null
);
