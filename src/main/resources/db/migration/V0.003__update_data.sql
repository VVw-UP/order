
-- Update data due to circular reference
UPDATE t_user SET create_by=1, last_modify_timestamp=now(), last_modify_by=1, entity_id=1 WHERE id=1;

UPDATE t_entity SET create_by=1, last_modify_timestamp=now(), last_modify_by=1 WHERE id=1;
ALTER TABLE t_entity ADD CONSTRAINT t_entity_t_user_fk1 FOREIGN KEY (create_by) REFERENCES t_user(id);
ALTER TABLE t_entity ADD CONSTRAINT t_entity_t_user_fk2 FOREIGN KEY (last_modify_by) REFERENCES t_user(id);

UPDATE t_channel set create_by=1, last_modify_timestamp=now(), last_modify_by=1 where id=1;
UPDATE t_channel set create_by=1, last_modify_timestamp=now(), last_modify_by=1 where id=2;

