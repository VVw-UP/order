
-- Insert t_counterparty_type
INSERT INTO t_counterparty_type (code,name) VALUES ('EXTERNAL', 'client');
INSERT INTO t_counterparty_type (code,name) VALUES ('BRANCH', 'branch');
INSERT INTO t_counterparty_type (code,name) VALUES ('LP', 'liquidity provider');
INSERT INTO t_counterparty_type (code,name) VALUES ('BOS', 'BOS client');

-- Insert t_counterparty_category
INSERT INTO t_counterparty_category (ms_segment_desc,mx_segment_code,segment_code) VALUES ('Wholesale Corporate', '10','W');
INSERT INTO t_counterparty_category (ms_segment_desc,mx_segment_code,segment_code) VALUES ('Enterprise Banking', '11','E');
INSERT INTO t_counterparty_category (ms_segment_desc,mx_segment_code,segment_code) VALUES ('SME-RE', '45','S');
INSERT INTO t_counterparty_category (ms_segment_desc,mx_segment_code,segment_code) VALUES ('Real Estate', '12','R');
INSERT INTO t_counterparty_category (ms_segment_desc,mx_segment_code,segment_code) VALUES ('Emb Large', '68','4');
INSERT INTO t_counterparty_category (ms_segment_desc,mx_segment_code,segment_code) VALUES ('Emb Mass', '66','3');
INSERT INTO t_counterparty_category (ms_segment_desc,mx_segment_code,segment_code) VALUES ('EmB Premier', '65','2');
INSERT INTO t_counterparty_category (ms_segment_desc,mx_segment_code,segment_code) VALUES ('Global Commodities Finance', '77','8');
INSERT INTO t_counterparty_category (ms_segment_desc,mx_segment_code,segment_code) VALUES ('Emerging Business 7', '43','A');
INSERT INTO t_counterparty_category (ms_segment_desc,mx_segment_code,segment_code) VALUES ('Financial Institutions - Bank', '13','B');
INSERT INTO t_counterparty_category (ms_segment_desc,mx_segment_code,segment_code) VALUES ('Financial Institutions - Non Bank', '14','F');
INSERT INTO t_counterparty_category (ms_segment_desc,mx_segment_code,segment_code) VALUES ('Emerging Business 3', '15','G');
INSERT INTO t_counterparty_category (ms_segment_desc,mx_segment_code,segment_code) VALUES ('Proprietary - Capital Market', '36','H');
INSERT INTO t_counterparty_category (ms_segment_desc,mx_segment_code,segment_code) VALUES ('Emerging Business 2', '20','T');

-- Insert t_counterparty
INSERT INTO t_counterparty(name, code, full_name, counterparty_type_id, counterparty_category_id, entity_id, create_timestamp, create_by, last_modify_timestamp, last_modify_by) VALUES('OCBC_SZ','OCBC_SZ','OCBC_SZ',2,2,1,now(),1,now(),1);

-- Insert t_product_type
INSERT INTO t_product_type (code,name) VALUES ('SPOT', 'fx spot');
INSERT INTO t_product_type (code,name) VALUES ('NDF', 'fx ndf');
INSERT INTO t_product_type (code,name) VALUES ('OUTRIGHT', 'fx outright');

-- Insert t_trade_status
INSERT INTO t_trade_status (code,name) VALUES ('DRAFT', 'draft');
INSERT INTO t_trade_status (code,name) VALUES ('REJECTED', 'rejected');
INSERT INTO t_trade_status (code,name) VALUES ('PEND_ACK', 'pend_ack');
INSERT INTO t_trade_status (code,name) VALUES ('ACTIVE', 'active');
INSERT INTO t_trade_status (code,name) VALUES ('RATE_TRIGGERED', 'rate_triggered');
INSERT INTO t_trade_status (code,name) VALUES ('INACTIVE', 'inactive');
INSERT INTO t_trade_status (code,name) VALUES ('ACTIVE_PARTIAL_FILLED', 'active_partial_filled');
INSERT INTO t_trade_status (code,name) VALUES ('EXPIRED_PARTIAL_FILLED', 'expired_partial_filled');
INSERT INTO t_trade_status (code,name) VALUES ('CANCELLED_PARTIAL_FILLED', 'cancelled_partial_filled');
INSERT INTO t_trade_status (code,name) VALUES ('FILLED', 'filled');
INSERT INTO t_trade_status (code,name) VALUES ('EXPIRED', 'expired');
INSERT INTO t_trade_status (code,name) VALUES ('CANCELLED', 'cancelled');
INSERT INTO t_trade_status (code,name) VALUES ('COMPLETED', 'completed');
INSERT INTO t_trade_status (code,name) VALUES ('COMPLETED_STP', 'completed_stp');
INSERT INTO t_trade_status (code,name) VALUES ('CALL_REQUIRED', 'call_required');

-- Insert t_currency
INSERT INTO t_currency (code,name,spot_days,create_timestamp,create_by,last_modify_timestamp,last_modify_by) VALUES ('AUD','AUD',2,now(),1,now(),1);
INSERT INTO t_currency (code,name,spot_days,create_timestamp,create_by,last_modify_timestamp,last_modify_by) VALUES ('USD','USD',2,now(),1,now(),1);
INSERT INTO t_currency (code,name,spot_days,create_timestamp,create_by,last_modify_timestamp,last_modify_by) VALUES ('EUR','EUR',2,now(),1,now(),1);
INSERT INTO t_currency (code,name,spot_days,create_timestamp,create_by,last_modify_timestamp,last_modify_by) VALUES ('JPY','JPY',2,now(),1,now(),1);
INSERT INTO t_currency (code,name,spot_days,create_timestamp,create_by,last_modify_timestamp,last_modify_by) VALUES ('GBP','GBP',2,now(),1,now(),1);
INSERT INTO t_currency (code,name,spot_days,create_timestamp,create_by,last_modify_timestamp,last_modify_by) VALUES ('CAD','CAD',2,now(),1,now(),1);
INSERT INTO t_currency (code,name,spot_days,create_timestamp,create_by,last_modify_timestamp,last_modify_by) VALUES ('CHF','CHF',2,now(),1,now(),1);
INSERT INTO t_currency (code,name,spot_days,create_timestamp,create_by,last_modify_timestamp,last_modify_by) VALUES ('HKD','HKD',2,now(),1,now(),1);
INSERT INTO t_currency (code,name,spot_days,create_timestamp,create_by,last_modify_timestamp,last_modify_by) VALUES ('SGD','SGD',2,now(),1,now(),1);
INSERT INTO t_currency (code,name,spot_days,create_timestamp,create_by,last_modify_timestamp,last_modify_by) VALUES ('NZD','NZD',2,now(),1,now(),1);
INSERT INTO t_currency (code,name,spot_days,create_timestamp,create_by,last_modify_timestamp,last_modify_by)  VALUES ('XAG','XAG',2,now(),1,now(),1);
INSERT INTO t_currency (code,name,spot_days,create_timestamp,create_by,last_modify_timestamp,last_modify_by)  VALUES ('CNH','CNH',2,now(),1,now(),1);
INSERT INTO t_currency (code,name,spot_days,create_timestamp,create_by,last_modify_timestamp,last_modify_by)  VALUES ('XAU','XAU',2,now(),1,now(),1);

-- Insert t_currency_pair
INSERT INTO t_currency_pair (ccy_pair,base_ccy_id,term_ccy_id,spot_pips,forward_pips,no_fly_zone_pips,temp_pips_very_hot,temp_pips_hot,temp_pips_warm,temp_pips_cold,temp_pips_inactive,create_timestamp,create_by,last_modify_timestamp,last_modify_by) VALUES ('EUR/USD',3,2,0.0001,0.0001,4000,1000,5000,10000,20000,30000,now(),1,now(),1);
INSERT INTO t_currency_pair (ccy_pair,base_ccy_id,term_ccy_id,spot_pips,forward_pips,no_fly_zone_pips,temp_pips_very_hot,temp_pips_hot,temp_pips_warm,temp_pips_cold,temp_pips_inactive,create_timestamp,create_by,last_modify_timestamp,last_modify_by) VALUES ('USD/BRO',3,2,0.0001,0.0001,4000,1000,5000,10000,20000,30000,now(),1,now(),1);
INSERT INTO t_currency_pair (ccy_pair,base_ccy_id,term_ccy_id,spot_pips,forward_pips,no_fly_zone_pips,temp_pips_very_hot,temp_pips_hot,temp_pips_warm,temp_pips_cold,temp_pips_inactive,create_timestamp,create_by,last_modify_timestamp,last_modify_by) VALUES ('EUR/INO',3,2,0.0001,0.0001,4000,1000,5000,10000,20000,30000,now(),1,now(),1);
INSERT INTO t_currency_pair (ccy_pair,base_ccy_id,term_ccy_id,spot_pips,forward_pips,no_fly_zone_pips,temp_pips_very_hot,temp_pips_hot,temp_pips_warm,temp_pips_cold,temp_pips_inactive,create_timestamp,create_by,last_modify_timestamp,last_modify_by) VALUES ('GBP/TRY',3,2,0.0001,0.0001,4000,1000,5000,10000,20000,30000,now(),1,now(),1);
INSERT INTO t_currency_pair (ccy_pair,base_ccy_id,term_ccy_id,spot_pips,forward_pips,no_fly_zone_pips,temp_pips_very_hot,temp_pips_hot,temp_pips_warm,temp_pips_cold,temp_pips_inactive,create_timestamp,create_by,last_modify_timestamp,last_modify_by) VALUES ('NZD/USD',3,2,0.0001,0.0001,4000,1000,5000,10000,20000,30000,now(),1,now(),1);
INSERT INTO t_currency_pair (ccy_pair,base_ccy_id,term_ccy_id,spot_pips,forward_pips,no_fly_zone_pips,temp_pips_very_hot,temp_pips_hot,temp_pips_warm,temp_pips_cold,temp_pips_inactive,create_timestamp,create_by,last_modify_timestamp,last_modify_by) VALUES ('SGD/CNH',3,2,0.0001,0.0001,4000,1000,5000,10000,20000,30000,now(),1,now(),1);
INSERT INTO t_currency_pair (ccy_pair,base_ccy_id,term_ccy_id,spot_pips,forward_pips,no_fly_zone_pips,temp_pips_very_hot,temp_pips_hot,temp_pips_warm,temp_pips_cold,temp_pips_inactive,create_timestamp,create_by,last_modify_timestamp,last_modify_by) VALUES ('XAG/AUD',3,2,0.0001,0.0001,4000,1000,5000,10000,20000,30000,now(),1,now(),1);


-- Insert t_timezone
INSERT INTO t_timezone (code) VALUES ('Singapore');

-- Insert t_time_in_force
INSERT INTO t_time_in_force (code,name) VALUES ('GTD','Good until Duration');

-- Insert t_watch_type
INSERT INTO t_watch_type (code,name) VALUES ('MANUAL','manual');
INSERT INTO t_watch_type (code,name) VALUES ('SYSTEM','system');

-- Insert t_country
INSERT INTO t_country (code,name) VALUES ('SG','SINGAPORE');

-- Insert t_desk
INSERT INTO t_desk (code,name,create_timestamp,create_by,last_modify_timestamp,last_modify_by) VALUES ('CFS','CFS DESK',now(),1,now(),1);

-- Insert t_rate_source
INSERT INTO t_rate_source (code,name,ordr) VALUES ('FLEXTRADE','Flex trade',0);
INSERT INTO t_rate_source (code,name,ordr) VALUES ('DEALHUB','Deal Hub',1);

-- Insert t_tenor
INSERT INTO t_tenor (code,name,rate_source_id) VALUES ('SP','SPOT',1);
INSERT INTO t_tenor (code,name,rate_source_id) VALUES ('TD','TD',1);

-- Insert t_direction
INSERT INTO t_direction (code,name) VALUES ('BUY','buy');
INSERT INTO t_direction (code,name) VALUES ('SELL','sell');

-- Insert t_customer_segment_code
INSERT INTO t_customer_segment_code (entity_id,segment_code,segment_description,segment_type,code_mx) VALUES (1,'M','MASS INDIVIDUAL','CFS',17);
INSERT INTO t_customer_segment_code (entity_id,segment_code,segment_description,segment_type,code_mx) VALUES (1,'K','EMERGING AFFLUENT','CFS',73);
INSERT INTO t_customer_segment_code (entity_id,segment_code,segment_description,segment_type,code_mx) VALUES (1,'X','PREMIER PRIVATE OFFSHORE','CFS',84);
INSERT INTO t_customer_segment_code (entity_id,segment_code,segment_description,segment_type,code_mx) VALUES (1,'Y','PREMIER PRIVATE ONSHORE','CFS',80);
INSERT INTO t_customer_segment_code (entity_id,segment_code,segment_description,segment_type,code_mx) VALUES (1,'V','PRIVATE CLIENT','CFS',18);
INSERT INTO t_customer_segment_code (entity_id,segment_code,segment_description,segment_type,code_mx) VALUES (1,'C','PRIVATE BANKING','CFS',16);
INSERT INTO t_customer_segment_code (entity_id,segment_code,segment_description,segment_type,code_mx) VALUES (1,'P','PREMIER BANKING ONSHORE','CFS',19);
INSERT INTO t_customer_segment_code (entity_id,segment_code,segment_description,segment_type,code_mx) VALUES (1,'Q','PREMIER BANKING OFFSHORE','CFS',48);

-- Insert t_user_channel
INSERT INTO t_user_channel (user_id,channel_id) VALUES (2,1);

