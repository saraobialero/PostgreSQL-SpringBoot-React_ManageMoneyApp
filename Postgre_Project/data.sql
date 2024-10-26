-- Insert Labels
INSERT INTO Labels (name, type, category, notes) VALUES
    ('Stipendio', 'INCOME', 'SALARY', 'Stipendio mensile'),
    ('Affitto', 'EXPENSE', 'RENT', 'Affitto casa'),
    ('Spesa', 'EXPENSE', 'FOOD', 'Spesa alimentare'),
    ('Bollette', 'EXPENSE', 'UTILITIES', 'Bollette casa'),
    ('Freelance', 'INCOME', 'FREELANCE', 'Lavori extra');

-- Insert Accounts
INSERT INTO Accounts (name, type, state, balance, currency, details) VALUES
    ('Conto Principale', 'CHECKING', 'ACTIVE', 5000.00, 'EUR', 'Conto principale per spese quotidiane'),
    ('Risparmi', 'SAVINGS', 'ACTIVE', 10000.00, 'EUR', 'Conto risparmio'),
    ('Carta di Credito', 'CREDIT_CARD', 'ACTIVE', -500.00, 'EUR', 'Carta di credito principale'),
    ('Contanti', 'CASH', 'ACTIVE', 200.00, 'EUR', 'Portafoglio contanti');

-- Insert Expenses
INSERT INTO Expenses (FKLabel, FKAccount, location, amount, currency, beneficiary, notes) VALUES
    (2, 1, 'Milano', 800.00, 'EUR', 'Proprietario Casa', 'Affitto Gennaio'),
    (3, 3, 'Esselunga', 150.00, 'EUR', 'Esselunga', 'Spesa settimanale'),
    (4, 1, 'Online', 100.00, 'EUR', 'Enel', 'Bolletta luce');

-- Insert Incomes
INSERT INTO Incomes (FKIncomesLabel, FKAccount, location, amount, currency, income_source, notes) VALUES
    (1, 1, 'Milano', 2500.00, 'EUR', 'Azienda SpA', 'Stipendio Gennaio'),
    (5, 1, 'Remote', 500.00, 'EUR', 'Cliente Freelance', 'Progetto web');

-- Insert ManageCount
INSERT INTO ManageCount (FKIncome, FKExpense, budget, currency, period_start, period_end) VALUES
    (1, 1, 3000.00, 'EUR', '2024-01-01', '2024-01-31'),
    (2, 2, 1000.00, 'EUR', '2024-01-01', '2024-01-31');

-- Insert SavingPlans
INSERT INTO SavingPlans (FKAccount, currency, amount, notes) VALUES
    (2, 'EUR', 5000.00, 'Obiettivo risparmio vacanze'),
    (2, 'EUR', 10000.00, 'Fondo emergenze');

-- Insert RecurringTransactions
INSERT INTO RecurringTransactions (
    FKLabel,
    type,
    amount,
    currency,
    frequency,
    start_date,
    next_occurrence,
    description,
    beneficiary
) VALUES
    (1, 'INCOME', 2500.00, 'EUR', 'MONTHLY', '2024-01-01', '2024-02-01', 'Stipendio mensile', 'Azienda SpA'),
    (2, 'EXPENSE', 800.00, 'EUR', 'MONTHLY', '2024-01-01', '2024-02-01', 'Affitto mensile', 'Proprietario Casa'),
    (4, 'EXPENSE', 100.00, 'EUR', 'MONTHLY', '2024-01-01', '2024-02-01', 'Bolletta luce', 'Enel');

-- Insert AccountRecurringTransactions
INSERT INTO AccountRecurringTransactions (FKAccount, FKRecurring, transaction_role) VALUES
    (1, 1, 'DESTINATION'),  -- Stipendio sul conto principale
    (1, 2, 'SOURCE'),      -- Affitto dal conto principale
    (1, 3, 'SOURCE');      -- Bolletta dal conto principale