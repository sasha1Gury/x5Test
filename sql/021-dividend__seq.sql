set search_path to bank;

CREATE SEQUENCE dividend__seq START 1;
grant select, insert, update, delete on dividend to dividends_user;

grant select on dividend to dividends_user; 