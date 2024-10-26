-- [Tutti gli ENUM rimangono uguali]
CREATE TYPE account_type AS ENUM (
    'CHECKING', 'SAVINGS', 'CREDIT_CARD', 'CASH', 'INVESTMENT'
);

CREATE TYPE account_state AS ENUM (
    'ACTIVE', 'INACTIVE', 'BLOCKED', 'CLOSED'
);

CREATE TYPE label_type AS ENUM (
    'EXPENSE', 'INCOME', 'TRANSFER', 'INVESTMENT'
);

CREATE TYPE label_category AS ENUM (
    'FOOD', 'TRANSPORT', 'UTILITIES', 'RENT', 'SHOPPING', 'HEALTH',
    'ENTERTAINMENT', 'SALARY', 'FREELANCE', 'INVESTMENT_RETURN', 'GIFT', 'OTHER'
);

CREATE TYPE frequency_type AS ENUM (
    'DAILY', 'WEEKLY', 'MONTHLY', 'YEARLY'
);

CREATE TYPE transaction_type AS ENUM (
    'INCOME', 'EXPENSE'
);

-- [Labels e Accounts rimangono uguali]
CREATE TABLE Labels (
    IDLabel SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type label_type NOT NULL,
    category label_category NOT NULL,
    notes TEXT
);

CREATE TABLE Accounts (
    IDAccounts SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type account_type NOT NULL,
    state account_state NOT NULL DEFAULT 'ACTIVE',
    balance DECIMAL(10,2) DEFAULT 0,
    currency VARCHAR(3) NOT NULL,
    details TEXT
);

-- [Expenses, Incomes, ManageCount e SavingPlans rimangono uguali]
CREATE TABLE Expenses (
    IDExpense SERIAL PRIMARY KEY,
    FKLabel INTEGER REFERENCES Labels(IDLabel),
    FKAccount INTEGER REFERENCES Accounts(IDAccounts),
    location VARCHAR(200),
    is_recurring BOOLEAN DEFAULT FALSE,
    creation_date DATE DEFAULT CURRENT_DATE,
    amount DECIMAL(10,2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    beneficiary VARCHAR(100),
    notes TEXT
);

CREATE TABLE Incomes (
    IDIncome SERIAL PRIMARY KEY,
    FKIncomesLabel INTEGER REFERENCES Labels(IDLabel),
    FKAccount INTEGER REFERENCES Accounts(IDAccounts),
    location VARCHAR(200),
    is_recurring BOOLEAN DEFAULT FALSE,
    creation_date DATE DEFAULT CURRENT_DATE,
    amount DECIMAL(10,2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    income_source VARCHAR(100),
    notes TEXT
);

CREATE TABLE ManageCount (
    IDManageCount SERIAL PRIMARY KEY,
    FKIncome INTEGER REFERENCES Incomes(IDIncome),
    FKExpense INTEGER REFERENCES Expenses(IDExpense),
    budget DECIMAL(10,2),
    currency VARCHAR(3) NOT NULL,
    period_start DATE NOT NULL,
    period_end DATE NOT NULL
);

CREATE TABLE SavingPlans (
    IDSavingPlan SERIAL PRIMARY KEY,
    FKAccount INTEGER REFERENCES Accounts(IDAccounts),
    currency VARCHAR(3) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    notes TEXT
);

-- [RecurringTransactions modificata]
CREATE TABLE RecurringTransactions (
    IDRecurring SERIAL PRIMARY KEY,
    FKLabel INTEGER REFERENCES Labels(IDLabel),
    type transaction_type NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    frequency frequency_type NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    next_occurrence DATE NOT NULL,
    description TEXT,
    beneficiary VARCHAR(100),
    income_source VARCHAR(100),
    last_processed_date DATE,
    is_active BOOLEAN DEFAULT TRUE,
    notes TEXT
);

-- [Nuova tabella per la relazione many-to-many]
CREATE TABLE AccountRecurringTransactions (
    IDAccountRecurring SERIAL PRIMARY KEY,
    FKAccount INTEGER REFERENCES Accounts(IDAccounts),
    FKRecurring INTEGER REFERENCES RecurringTransactions(IDRecurring),
    transaction_role VARCHAR(20) NOT NULL CHECK (transaction_role IN ('SOURCE', 'DESTINATION')),
    UNIQUE(FKAccount, FKRecurring, transaction_role)
);

-- [Indici aggiornati]
CREATE INDEX idx_expenses_account ON Expenses(FKAccount);
CREATE INDEX idx_expenses_label ON Expenses(FKLabel);
CREATE INDEX idx_incomes_account ON Incomes(FKAccount);
CREATE INDEX idx_incomes_label ON Incomes(FKIncomesLabel);
CREATE INDEX idx_recurring_next_occurrence ON RecurringTransactions(next_occurrence);
CREATE INDEX idx_expenses_date ON Expenses(creation_date);
CREATE INDEX idx_incomes_date ON Incomes(creation_date);
CREATE INDEX idx_account_recurring ON AccountRecurringTransactions(FKAccount);
CREATE INDEX idx_recurring_label ON RecurringTransactions(FKLabel);