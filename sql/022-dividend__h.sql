CREATE TABLE dividend__h (
	hid BIGINT NOT NULL,                      -- History row ID
	hd TIMESTAMP NOT NULL,                    -- History timestamp
	hop VARCHAR(8) NOT NULL,                  -- Operation type: INSERT, UPDATE, DELETE
	huser VARCHAR(32) NULL,                   -- User who performed the operation
	hip VARCHAR(32) NULL,                     -- IP address of the user
	id BIGINT NOT NULL,                       -- Origin ID
	created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
	state VARCHAR(32) NOT NULL,
	state_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
	company_name VARCHAR(255) NOT NULL,       -- Name of the company
	company_code VARCHAR(32) NOT NULL,        -- Code of the organization that paid dividends
	dividend_declaration_date DATE,           -- Date of dividend declaration
	close_register_date DATE NOT NULL,        -- Date of closure of the register
	dividend_period VARCHAR(64) NOT NULL,     -- Year of recording of dividends
	dividend_amount NUMERIC(10, 2) NOT NULL,  -- Dividend amount per share
	buy_before_date DATE,                     -- The last date to buy shares to receive dividends
	payment_date DATE                         -- Date when dividends are paid (may be NULL if not yet paid)
);

/* Create Index */
CREATE INDEX dividend__h_idx_id ON dividend__h (id ASC);

/* Create Comments */
COMMENT ON COLUMN dividend__h.hid
	IS 'history row id'
;
COMMENT ON COLUMN dividend__h.hd
	IS 'history timestamp'
;
COMMENT ON COLUMN dividend__h.huser
	IS 'current_user'
;
COMMENT ON COLUMN dividend__h.hip
	IS 'inet_client_addr'
;

COMMENT ON COLUMN dividend__h.id
	IS 'origin id'
;

/* Create Sequence for history table */
CREATE SEQUENCE dividend__hs START 1;

/* Create Functions */
CREATE OR REPLACE FUNCTION dividend__tbdf()
RETURNS trigger 
SECURITY DEFINER
AS
$$
BEGIN
    INSERT INTO bank.dividend__h (hid, hd, hop, huser, hip,
        id, created, state, state_date, company_name, company_code,
        dividend_declaration_date, close_register_date, dividend_period, dividend_amount, buy_before_date, payment_date)
    VALUES (nextval('bank.dividend__hs'), now(), TG_OP, session_user, inet_client_addr(),
        OLD.id, OLD.created, OLD.state, OLD.state_date, OLD.company_name, OLD.company_code,
        OLD.dividend_declaration_date, OLD.close_register_date, OLD.dividend_period, OLD.dividend_amount,
        OLD.buy_before_date, OLD.payment_date);
    RETURN OLD;
END;
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION dividend__tbif()
RETURNS trigger 
SECURITY DEFINER
AS
$$
BEGIN
    INSERT INTO bank.dividend__h (hid, hd, hop, huser, hip,
        id, created, state, state_date, company_name, company_code,
        dividend_declaration_date, close_register_date, dividend_period, dividend_amount, buy_before_date, payment_date)
    VALUES (nextval('bank.dividend__hs'), now(), TG_OP, session_user, inet_client_addr(),
        NEW.id, NEW.created, NEW.state, NEW.state_date, NEW.company_name, NEW.company_code,
        NEW.dividend_declaration_date, NEW.close_register_date, NEW.dividend_period, NEW.dividend_amount,
        NEW.buy_before_date, NEW.payment_date);
    RETURN NEW;
END;
$$
LANGUAGE plpgsql;

/* Create Triggers */
CREATE TRIGGER dividend__tbd
BEFORE DELETE
ON dividend
FOR EACH ROW
EXECUTE FUNCTION dividend__tbdf();

CREATE TRIGGER dividend__tbi
BEFORE INSERT
ON dividend
FOR EACH ROW
EXECUTE FUNCTION dividend__tbif();

CREATE TRIGGER dividend__tbu
BEFORE UPDATE
ON dividend
FOR EACH ROW
WHEN (OLD.* IS DISTINCT FROM NEW.*)
EXECUTE FUNCTION dividend__tbif();

/* Grants (optional if you need them) */
grant select on dividend__h to dividends_user;
grant usage, select on dividend__hs to dividends_user;
