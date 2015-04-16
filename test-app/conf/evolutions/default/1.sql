# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table page (
  id                        bigint not null,
  namespace                 varchar(255),
  key                       varchar(255),
  access                    varchar(255),
  state                     varchar(255),
  template                  varchar(255),
  revision_id               bigint,
  meta_info_id              bigint,
  constraint pk_page primary key (id))
;

create table page_meta_info (
  id                        bigint not null,
  summary                   varchar(255),
  thumb_image               varchar(255),
  meta_description          varchar(255),
  meta_key_words            varchar(255),
  page_id                   bigint,
  constraint pk_page_meta_info primary key (id))
;

create table revision (
  id                        bigint not null,
  page_id                   bigint,
  title                     varchar(255),
  source                    clob,
  content                   clob,
  create_date_time          timestamp not null,
  update_date_time          timestamp not null,
  constraint pk_revision primary key (id))
;

create sequence page_seq;

create sequence page_meta_info_seq;

create sequence revision_seq;

alter table page add constraint fk_page_revision_1 foreign key (revision_id) references revision (id) on delete restrict on update restrict;
create index ix_page_revision_1 on page (revision_id);
alter table page add constraint fk_page_metaInfo_2 foreign key (meta_info_id) references page_meta_info (id) on delete restrict on update restrict;
create index ix_page_metaInfo_2 on page (meta_info_id);
alter table page_meta_info add constraint fk_page_meta_info_page_3 foreign key (page_id) references page (id) on delete restrict on update restrict;
create index ix_page_meta_info_page_3 on page_meta_info (page_id);
alter table revision add constraint fk_revision_page_4 foreign key (page_id) references page (id) on delete restrict on update restrict;
create index ix_revision_page_4 on revision (page_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists page;

drop table if exists page_meta_info;

drop table if exists revision;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists page_seq;

drop sequence if exists page_meta_info_seq;

drop sequence if exists revision_seq;

