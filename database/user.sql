-- ********************************************************************************
-- This script creates the database users and grants them the necessary permissions
-- ********************************************************************************
INSERT INTO transfers(transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES(2, 2, 1, 3, 50);
		DELETE FROM transfers;		
SELECT transfer_id, account_from, account_to, amount FROM transfers JOIN users ON transfers.account_to = users.user_id WHERE username = 'kaden';
SELECT * FROM transfers WHERE transfer_id = 15;

SELECT username FROM users JOIN transfers ON user_id = transfers.account_to WHERE user_id = 3;
SELECT t.transfer_id, t.account_from, t.account_to, t.amount, u.username FROM transfers t  
					 JOIN users u ON t.account_to = u.user_id WHERE u.username = 'kaden';

SELECT transfers.transfer_id, users.username, transfers.amount FROM transfers
JOIN accounts ON accounts.account_id = transfers.account_from
LEFT OUTER JOIN users ON users.user_id = accounts.user_id
WHERE transfers.account_to = 3;
SELECT transfers.transfer_id, users.username, transfers.amount FROM transfers
JOIN accounts ON accounts.account_id = transfers.account_to
JOIN users ON users.user_id = accounts.user_id
WHERE transfers.account_from = 3 OR transfers.account_to = 3; AND transfers.account_to <> 3;




SELECT * FROM transfers;
CREATE USER tenmo_owner
WITH PASSWORD 'tebucks';

GRANT ALL
ON ALL TABLES IN SCHEMA public
TO tenmo_owner;

GRANT ALL
ON ALL SEQUENCES IN SCHEMA public
TO tenmo_owner;

CREATE USER tenmo_appuser
WITH PASSWORD 'tebucks';

GRANT SELECT, INSERT, UPDATE, DELETE
ON ALL TABLES IN SCHEMA public
TO tenmo_appuser;

GRANT USAGE, SELECT
ON ALL SEQUENCES IN SCHEMA public
TO tenmo_appuser;
