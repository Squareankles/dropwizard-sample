-- liquibase formatted sql

-- changeset michael walsh:1
alter table sync_operation_payload
    add file_key TEXT NOT NULL AFTER location_url;

-- rollback alter table sync_operation_payload drop file_key
