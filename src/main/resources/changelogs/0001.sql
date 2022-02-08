-- liquibase formatted sql

-- changeset michael walsh:1
create table sync_operation
(
    sync_operation_id VARCHAR(36) NOT NULL,
    customer_id       VARCHAR(36) NOT NULL,
    request_tmstp     DATETIME    NOT NULL,
    PRIMARY KEY (sync_operation_id)
);

-- rollback drop table sync_operation

-- changeset michael walsh:2
create table sync_operation_payload
(
    payload_id         VARCHAR(36) NOT NULL,
    sync_operation_id  VARCHAR(36) NOT NULL,
    location_url       TEXT        NOT NULL,
    payload_sync_tmstp DATETIME    NOT NULL,
    PRIMARY KEY (payload_id),
    FOREIGN KEY (sync_operation_id) REFERENCES sync_operation (sync_operation_id)
);

-- rollback drop table sync_payload

-- changeset michael walsh:3
create table sync_status
(
    sync_status_id VARCHAR(36) NOT NULL,
    name           VARCHAR(35) NOT NULL UNIQUE,
    created        DATETIME    NOT NULL,
    PRIMARY KEY (sync_status_id)
);

-- rollback drop table sync_status

-- changeset michael walsh:4
insert into sync_status(select uuid(), 'OPENED', now());
insert into sync_status(select uuid(), 'SUBMITTED', now());
insert into sync_status(select uuid(), 'PARSING', now());
insert into sync_status(select uuid(), 'COMPLETED', now());
insert into sync_status(select uuid(), 'CANCELLED', now());

-- rollback delete from sync_status

-- changeset michael walsh:5
create table sync_operation_status
(
    sync_operation_status_id VARCHAR(36) NOT NULL,
    sync_status_id           VARCHAR(36) NOT NULL,
    sync_operation_id        VARCHAR(36) NOT NULL,
    created                  DATETIME    NOT NULL,
    PRIMARY KEY (sync_operation_status_id),
    FOREIGN KEY (sync_operation_id) REFERENCES sync_operation (sync_operation_id),
    FOREIGN KEY (sync_status_id) REFERENCES sync_status (sync_status_id)
);

-- rollback delete from sync_operation_status