# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table pages_meta (
  id                        bigint not null,
  summary                   varchar(255),
  thumb                     varchar(255),
  description               varchar(255),
  keywords                  varchar(255),
  category                  varchar(255),
  page_id                   bigint,
  constraint pk_pages_meta primary key (id))
;

create table pages (
  id                        bigint not null,
  namespace                 varchar(255),
  key                       varchar(255),
  access                    varchar(255),
  state                     varchar(255),
  template                  varchar(255),
  revision_id               bigint,
  meta_id                   bigint,
  constraint pk_pages primary key (id))
;

create table pages_revisions (
  id                        bigint not null,
  page_id                   bigint,
  title                     varchar(255),
  source                    clob,
  content                   clob,
  create_date_time          timestamp not null,
  update_date_time          timestamp not null,
  constraint pk_pages_revisions primary key (id))
;

create table pages_tags (
  id                        bigint not null,
  tag                       varchar(255),
  constraint pk_pages_tags primary key (id))
;

create sequence pages_meta_seq;

create sequence pages_seq;

create sequence pages_revisions_seq;

create sequence pages_tags_seq;

alter table pages_meta add constraint fk_pages_meta_page_1 foreign key (page_id) references pages (id) on delete restrict on update restrict;
create index ix_pages_meta_page_1 on pages_meta (page_id);
alter table pages add constraint fk_pages_revision_2 foreign key (revision_id) references pages_revisions (id) on delete restrict on update restrict;
create index ix_pages_revision_2 on pages (revision_id);
alter table pages add constraint fk_pages_meta_3 foreign key (meta_id) references pages_meta (id) on delete restrict on update restrict;
create index ix_pages_meta_3 on pages (meta_id);
alter table pages_revisions add constraint fk_pages_revisions_page_4 foreign key (page_id) references pages (id) on delete restrict on update restrict;
create index ix_pages_revisions_page_4 on pages_revisions (page_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists pages_meta;

drop table if exists pages;

drop table if exists pages_revisions;

drop table if exists pages_tags;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists pages_meta_seq;

drop sequence if exists pages_seq;

drop sequence if exists pages_revisions_seq;

drop sequence if exists pages_tags_seq;

