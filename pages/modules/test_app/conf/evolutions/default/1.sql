# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table pages_access (
  id                        bigint not null,
  access_                   varchar(255),
  page_id                   bigint,
  constraint pk_pages_access primary key (id))
;

create table pages_meta (
  id                        bigint not null,
  title                     varchar(255),
  summary                   clob,
  thumb                     varchar(255),
  description               clob,
  keywords                  clob,
  category                  varchar(255),
  template                  varchar(255),
  page_id                   bigint,
  constraint uq_pages_meta_page_id unique (page_id),
  constraint pk_pages_meta primary key (id))
;

create table pages (
  id                        bigint not null,
  namespace                 varchar(255),
  key_                      varchar(255),
  access_id                 bigint,
  state_id                  bigint,
  revision_id               bigint,
  meta_id                   bigint,
  constraint uq_pages_access_id unique (access_id),
  constraint uq_pages_state_id unique (state_id),
  constraint uq_pages_revision_id unique (revision_id),
  constraint uq_pages_meta_id unique (meta_id),
  constraint pk_pages primary key (id))
;

create table pages_revisions (
  id                        bigint not null,
  page_id                   bigint,
  source                    clob,
  content                   clob,
  create_date_time          timestamp not null,
  constraint pk_pages_revisions primary key (id))
;

create table pages_state (
  id                        bigint not null,
  state_                    varchar(255),
  page_id                   bigint,
  update_date_time          timestamp not null,
  constraint pk_pages_state primary key (id))
;

create table pages_tags (
  id                        bigint not null,
  tag                       varchar(255),
  constraint pk_pages_tags primary key (id))
;

create sequence pages_access_seq;

create sequence pages_meta_seq;

create sequence pages_seq;

create sequence pages_revisions_seq;

create sequence pages_state_seq;

create sequence pages_tags_seq;

alter table pages_access add constraint fk_pages_access_page_1 foreign key (page_id) references pages (id) on delete restrict on update restrict;
create index ix_pages_access_page_1 on pages_access (page_id);
alter table pages_meta add constraint fk_pages_meta_page_2 foreign key (page_id) references pages (id) on delete restrict on update restrict;
create index ix_pages_meta_page_2 on pages_meta (page_id);
alter table pages add constraint fk_pages_access_3 foreign key (access_id) references pages_access (id) on delete restrict on update restrict;
create index ix_pages_access_3 on pages (access_id);
alter table pages add constraint fk_pages_state_4 foreign key (state_id) references pages_state (id) on delete restrict on update restrict;
create index ix_pages_state_4 on pages (state_id);
alter table pages add constraint fk_pages_revision_5 foreign key (revision_id) references pages_revisions (id) on delete restrict on update restrict;
create index ix_pages_revision_5 on pages (revision_id);
alter table pages add constraint fk_pages_meta_6 foreign key (meta_id) references pages_meta (id) on delete restrict on update restrict;
create index ix_pages_meta_6 on pages (meta_id);
alter table pages_revisions add constraint fk_pages_revisions_page_7 foreign key (page_id) references pages (id) on delete restrict on update restrict;
create index ix_pages_revisions_page_7 on pages_revisions (page_id);
alter table pages_state add constraint fk_pages_state_page_8 foreign key (page_id) references pages (id) on delete restrict on update restrict;
create index ix_pages_state_page_8 on pages_state (page_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists pages_access;

drop table if exists pages_meta;

drop table if exists pages;

drop table if exists pages_revisions;

drop table if exists pages_state;

drop table if exists pages_tags;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists pages_access_seq;

drop sequence if exists pages_meta_seq;

drop sequence if exists pages_seq;

drop sequence if exists pages_revisions_seq;

drop sequence if exists pages_state_seq;

drop sequence if exists pages_tags_seq;

