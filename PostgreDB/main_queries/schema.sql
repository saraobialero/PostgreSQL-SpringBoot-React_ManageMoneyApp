-- Drop schema if exists
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;
GRANT ALL ON SCHEMA public TO public;

-- Create ENUM types
CREATE TYPE transaction_type AS ENUM ('INCOME', 'EXPENSE');

CREATE TYPE category_type AS ENUM (
    'FOOD',
    'HOME',
    'WORK',
    'ENTERTAINMENT',
    'HEALTH',
    'HOLIDAY',
    'SHOPPING',
    'CAR',
    'TRANSPORT',
    'SPORT'
);

CREATE TYPE label_type AS ENUM (
    -- Food
    'DINNER',
    'LUNCH',
    'BREAKFAST',
    'HAPPY_HOUR',
    'MARKET_EXPENSES',
    'OTHER_FOOD',
    -- Home
    'UTILITY_BILLS',
    'GROCERY_SHOPPING',
    'OTHER_HOME',
    -- Work
    'SALARY',
    'TAXES',
    'REFUNDS',
    'OTHER_WORK',
    -- Entertainment
    'CINEMA',
    'ART',
    'OTHER_ENTERTAINMENT',
    -- Health
    'HEALTH_APPOINTMENTS',
    'HEALTH_SHOPPING',
    'OTHER_HEALTH',
    -- Holiday
    'FOOD_HOLIDAY',
    'ACTIVITIES',
    'ACCOMMODATION',
    'TRANSPORT_HOLIDAY',
    'OTHER_HOLIDAY',
    -- Shopping
    'CLOTHES',
    'TECH',
    'BOOKS',
    'OTHER_SHOPPING',
    -- Car
    'INSURANCE',
    'GASOLINE',
    'MAINTENANCE_CAR',
    'OTHER_CAR',
    -- Transport
    'TRAIN',
    'BUS',
    'AIRPLANE',
    'SHIP',
    'OTHER_TRANSPORT',
    -- Sport
    'GYM_ACTIVITY',
    'CYCLE_ACTIVITY',
    'RUN_ACTIVITY',
    'SWIM_EQUIPMENT',
    'CYCLE_EQUIPMENT',
    'RUN_EQUIPMENT',
    'HIKE_EQUIPMENT',
    'WORK_EQUIPMENT',
    'OTHER_SPORT'
);

CREATE TYPE account_type AS ENUM (
    'CHECKING',
    'SAVINGS',
    'CREDIT_CARD',
    'CASH',
    'INVESTMENT'
);

CREATE TYPE account_state AS ENUM (
    'ACTIVE',
    'INACTIVE',
    'BLOCKED',
    'CLOSED'
);

CREATE TYPE frequency_type AS ENUM (
    'DAILY',
    'WEEKLY',
    'MONTHLY',
    'YEARLY'
);

-- Create tables
CREATE TABLE accounts (
    id SERIAL PRIMARY KEY,
    account_type account_type NOT NULL,
    state account_state DEFAULT 'ACTIVE' NOT NULL,
    balance NUMERIC(19,2) DEFAULT 0
);

CREATE TABLE category_label_mappings (
    id SERIAL PRIMARY KEY,
    category_type category_type NOT NULL,
    allowed_label_type label_type NOT NULL,
    transaction_type transaction_type NOT NULL,
    CONSTRAINT category_label_mappings_category_allowed_label_type_key
        UNIQUE (category_type, allowed_label_type)
);

CREATE TABLE labels (
    id SERIAL PRIMARY KEY,
    category_type category_type NOT NULL,
    label_type label_type NOT NULL,
    transaction_type transaction_type NOT NULL,
    is_active BOOLEAN DEFAULT true,
    CONSTRAINT labels_category_label_type_fkey
        FOREIGN KEY (category_type, label_type)
        REFERENCES category_label_mappings(category_type, allowed_label_type)
);

CREATE TABLE recurring_transactions (
    id SERIAL PRIMARY KEY,
    label_id INTEGER REFERENCES labels(id),
    type transaction_type NOT NULL,
    amount NUMERIC(19,2) NOT NULL,
    frequency frequency_type NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    next_occurrence DATE NOT NULL,
    beneficiary VARCHAR(100),
    source VARCHAR(100),
    last_processed_date DATE,
    is_active BOOLEAN DEFAULT true
);

CREATE TABLE account_recurring_transactions (
    id SERIAL PRIMARY KEY,
    account_id INTEGER REFERENCES accounts(id),
    recurring_id INTEGER REFERENCES recurring_transactions(id),
    transaction_role VARCHAR(20) NOT NULL,
    CONSTRAINT account_recurring_transactions_transaction_role_check
        CHECK (transaction_role IN ('SOURCE', 'DESTINATION')),
    CONSTRAINT unique_account_recurring_role
        UNIQUE(account_id, recurring_id, transaction_role)
);

CREATE TABLE transactions (
    id SERIAL PRIMARY KEY,
    label_id INTEGER REFERENCES labels(id),
    account_id INTEGER REFERENCES accounts(id),
    type transaction_type NOT NULL,
    name VARCHAR(100) NOT NULL,
    location VARCHAR(200),
    amount NUMERIC(19,2) NOT NULL,
    beneficiary VARCHAR(100),
    source VARCHAR(100),
    is_recurring BOOLEAN DEFAULT false,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE saving_plans (
    id SERIAL PRIMARY KEY,
    account_id INTEGER REFERENCES accounts(id),
    target_amount NUMERIC(19,2) NOT NULL,
    current_amount NUMERIC(19,2) DEFAULT 0,
    start_date DATE DEFAULT CURRENT_DATE NOT NULL,
    target_date DATE
);

-- Create indexes
CREATE INDEX idx_labels_category ON labels(category_type);
CREATE INDEX idx_transactions_account ON transactions(account_id);
CREATE INDEX idx_transactions_label ON transactions(label_id);
CREATE INDEX idx_transactions_date ON transactions(transaction_date);
CREATE INDEX idx_recurring_label ON recurring_transactions(label_id);
CREATE INDEX idx_recurring_next ON recurring_transactions(next_occurrence);
CREATE INDEX idx_account_recurring ON account_recurring_transactions(recurring_id);