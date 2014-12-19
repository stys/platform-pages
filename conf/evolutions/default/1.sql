# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table html_page (
  id                        bigint not null,
  namespace                 clob,
  key                       varchar(255),
  version                   bigint,
  html                      clob,
  create_date_time          timestamp not null,
  update_date_time          timestamp not null,
  constraint pk_html_page primary key (id))
;

create sequence html_page_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists html_page;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists html_page_seq;

