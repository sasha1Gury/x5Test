set search_path to bank;

CREATE TABLE dividend
(
	id bigint NOT NULL,                       -- Technical id
	created timestamp without time zone NOT NULL,
	state varchar(32) NOT NULL,               -- "PAID", "EXPECTED", "FORECAST"
	state_date timestamp without time zone NOT NULL,
	company_name VARCHAR(255) NOT NULL,       -- Name of the company
	company_code VARCHAR(32) NOT NULL,        -- Code of the organization that paid dividends
	dividend_declaration_date DATE,           -- Date of dividend declaration
	close_register_date DATE NOT NULL,        -- Date of closure of the register
	dividend_period VARCHAR(64),              -- Year of recording of dividends
	dividend_amount NUMERIC(10, 2) NOT NULL,  -- Dividend amount per share
	buy_before_date DATE,                     -- The last date to buy shares to receive dividends
	payment_date DATE                         -- Date when dividends are paid (may be NULL if not yet paid)
)
;

/* Create Primary Keys, Indexes, Uniques, Checks */

ALTER TABLE dividend ADD CONSTRAINT "PK_dividend"
	PRIMARY KEY (id)
;

/* Create Table Comments, Sequences for Autonumber Columns */

COMMENT ON COLUMN dividend.id
 IS 'Technical id';

COMMENT ON COLUMN dividend.state 
 IS 'Status of the dividend: EXPECTED or PAID';

COMMENT ON COLUMN dividend.company_name
 IS 'Name of the company';

COMMENT ON COLUMN dividend.company_code
 IS 'Code of the organization that paid dividends';

COMMENT ON COLUMN dividend.dividend_declaration_date
 IS 'Date of dividend declaration';

COMMENT ON COLUMN dividend.close_register_date
 IS 'Date of closure of the register';

COMMENT ON COLUMN dividend.dividend_period
 IS 'Period for which the dividend is paid';

COMMENT ON COLUMN dividend.dividend_amount
 IS 'Dividend amount per share';

COMMENT ON COLUMN dividend.buy_before_date
 IS 'The last date to buy shares to receive dividends';

COMMENT ON COLUMN dividend.payment_date
 IS 'Date when dividends are paid (may be NULL if not yet paid)';
