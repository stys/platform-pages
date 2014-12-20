# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table dummy (
  id                        bigint not null,
  txt                       varchar(255),
  constraint pk_dummy primary key (id))
;

create table simple_page_entity (
  revision                  bigint not null,
  namespace                 varchar(255),
  key                       varchar(255),
  content                   clob,
  template                  varchar(255),
  create_date_time          timestamp not null,
  update_date_time          timestamp not null,
  constraint pk_simple_page_entity primary key (revision))
;

create sequence dummy_seq;

create sequence simple_page_entity_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists dummy;

drop table if exists simple_page_entity;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists dummy_seq;

drop sequence if exists simple_page_entity_seq;

