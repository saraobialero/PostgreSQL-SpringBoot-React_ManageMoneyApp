-- Insert valid category-label mappings
INSERT INTO category_label_mappings (category, allowed_label_type, transaction_type) VALUES
    -- FOOD
    ('FOOD', 'DINNER', 'EXPENSE'),
    ('FOOD', 'LUNCH', 'EXPENSE'),
    ('FOOD', 'BREAKFAST', 'EXPENSE'),
    ('FOOD', 'MARKET_EXPENSES', 'EXPENSE'),
    ('FOOD', 'OTHER_FOOD', 'EXPENSE'),

    -- HOME
    ('HOME', 'UTILITY_BILLS', 'EXPENSE'),
    ('HOME', 'GROCERY_SHOPPING', 'EXPENSE'),
    ('HOME', 'OTHER_HOME', 'EXPENSE'),

    -- WORK
    ('WORK', 'SALARY', 'INCOME'),
    ('WORK', 'TAXES', 'EXPENSE'),
    ('WORK', 'REFUNDS', 'INCOME'),
    ('WORK', 'OTHER_WORK', 'EXPENSE');

-- Insert accounts
INSERT INTO accounts (type, balance, currency, details) VALUES
    ('CHECKING', 5000.00, 'EUR', 'Conto principale'),
    ('SAVINGS', 10000.00, 'EUR', 'Conto risparmio'),
    ('CREDIT_CARD', -500.00, 'EUR', 'Carta principale'),
    ('CASH', 200.00, 'EUR', 'Portafoglio');

-- Insert labels
INSERT INTO labels (category, label_type, transaction_type) VALUES
    ('FOOD', 'DINNER', 'EXPENSE'),
    ('FOOD', 'LUNCH', 'EXPENSE'),
    ('HOME', 'UTILITY_BILLS', 'EXPENSE'),
    ('WORK', 'SALARY', 'INCOME');

-- Insert transactions
INSERT INTO transactions (
    label_id,
    account_id,
    type,
    name,
    location,
    amount,
    currency,
    beneficiary,
    source,
    notes,
    transaction_date
) VALUES
    (1, 3, 'EXPENSE', 'Cena Sabato', 'Ristorante Da Mario', 45.00, 'EUR', 'Ristorante Da Mario', NULL, 'Cena con amici', '2024-12-07'),
    (2, 4, 'EXPENSE', 'Pranzo Lavoro', 'Bar Centrale', 12.50, 'EUR', 'Bar Centrale', NULL, 'Pranzo di lavoro', '2024-12-07'),
    (3, 1, 'EXPENSE', 'Bolletta Luce', 'Online', 95.30, 'EUR', 'Enel', NULL, 'Bolletta luce dicembre', '2024-12-05'),
    (4, 1, 'INCOME', 'Stipendio', 'Milano', 2500.00, 'EUR', NULL, 'Azienda SpA', 'Stipendio dicembre', '2024-12-01');

-- Insert recurring transactions
INSERT INTO recurring_transactions (
    label_id,
    type,
    amount,
    currency,
    frequency,
    start_date,
    next_occurrence,
    description
) VALUES
    (4, 'INCOME', 2500.00, 'EUR', 'MONTHLY', '2024-01-01', '2024-01-27', 'Stipendio mensile'),
    (3, 'EXPENSE', 100.00, 'EUR', 'MONTHLY', '2024-01-01', '2024-01-15', 'Bolletta luce');

-- Insert account recurring transactions
INSERT INTO account_recurring_transactions (
    account_id,
    recurring_id,
    transaction_role
) VALUES
    (1, 1, 'DESTINATION'),  -- Stipendio va sul conto CHECKING
    (1, 2, 'SOURCE');      -- Bolletta esce dal conto CHECKING

-- Insert saving plans
INSERT INTO saving_plans (
    account_id,
    target_amount,
    current_amount,
    currency,
    start_date,
    target_date,
    notes
) VALUES
    (2, 15000.00, 10000.00, 'EUR', '2024-01-01', '2024-12-31', 'Risparmio per vacanze'),
    (2, 5000.00, 1000.00, 'EUR', '2024-01-01', '2024-06-30', 'Fondo emergenze');