# --- !Ups
ALTER TABLE sdec_thread ADD COLUMN thread_owner_id BIGINT;
ALTER TABLE sdec_thread ADD COLUMN owning_team_name VARCHAR(100) NOT NULL DEFAULT '';
ALTER TABLE sdec_thread ADD COLUMN owning_team_type BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE sdec_thread ADD CONSTRAINT fk_thread_owner FOREIGN KEY (thread_owner_id) REFERENCES sdec_staff (id);

# --- !Downs
ALTER TABLE sdec_thread DROP CONSTRAINT fk_thread_owner;
ALTER TABLE sdec_thread DROP COLUMN thread_owner_id;
ALTER TABLE sdec_thread DROP COLUMN owning_team_name;
ALTER TABLE sdec_thread DROP COLUMN owning_team_type;