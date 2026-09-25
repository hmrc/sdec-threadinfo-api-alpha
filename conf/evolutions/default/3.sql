# --- !Ups
ALTER TABLE sdec_thread ADD COLUMN thread_creator VARCHAR(50) NOT NULL DEFAULT '';
ALTER TABLE sdec_thread ADD COLUMN thread_owner VARCHAR(50);
ALTER TABLE sdec_thread ADD COLUMN owning_team_name VARCHAR(100) NOT NULL DEFAULT '';
ALTER TABLE sdec_thread ADD COLUMN owning_team_type BOOLEAN NOT NULL DEFAULT FALSE;

# --- !Downs
ALTER TABLE sdec_thread DROP COLUMN threadCreator;
ALTER TABLE sdec_thread DROP COLUMN threadOwner;
ALTER TABLE sdec_thread DROP COLUMN owningTeamName;
ALTER TABLE sdec_thread DROP COLUMN owningTeamType;