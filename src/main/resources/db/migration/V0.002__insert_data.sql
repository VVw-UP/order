
-- Insert client 
INSERT INTO t_client (name) VALUES ('OCBC');

-- Insert entity 
INSERT INTO t_entity (code, name, create_timestamp, create_by, last_modify_timestamp, last_modify_by) VALUES ('OCBC SG', 'OCBC Singapore Branch', now(),-1, now(), -1);

-- Insert user
INSERT INTO t_user (first_name, last_name, email, create_timestamp, create_by, last_modify_timestamp, last_modify_by, entity_id) VALUES ('System', 'User', 'admin@ocbc.com', now(), -1, now(), -1, 1);
INSERT INTO t_user (first_name, last_name, email, create_timestamp, create_by, last_modify_timestamp, last_modify_by, entity_id) VALUES ('CFS', 'User', 'cfs@ocbc.com', now(), 1, now(), 1, 1);


-- Insert order_types
INSERT INTO t_order_type (code,name) VALUES ('CALL', 'call');
INSERT INTO t_order_type (code,name) VALUES ('LIMIT', 'limit');

-- Insert order_status
INSERT INTO t_order_status (code,name) VALUES ('DRAFT', 'draft');
INSERT INTO t_order_status (code,name) VALUES ('ACTIVE', 'active');
INSERT INTO t_order_status (code,name) VALUES ('COMPLETED', 'completed');
INSERT INTO t_order_status (code,name) VALUES ('PEND_ACK', 'pend_ack');

-- Insert channel
INSERT INTO t_channel (code,name,create_timestamp,create_by,last_modify_timestamp,last_modify_by) VALUES ('CFS', 'MS-OCBC-CFS', now(), -1, now(), -1);
INSERT INTO t_channel (code,name,create_timestamp,create_by,last_modify_timestamp,last_modify_by) VALUES ('GT', 'OMS-GT', now(), -1, now(), -1);
