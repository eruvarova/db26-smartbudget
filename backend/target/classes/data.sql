-- PROVIDED – seed data loaded automatically on startup
-- You will see this data in H2 console and via the API on Day 1

INSERT INTO categories (name, type) VALUES ('Salary',    'INCOME');
INSERT INTO categories (name, type) VALUES ('Freelance', 'INCOME');
INSERT INTO categories (name, type) VALUES ('Food',      'EXPENSE');
INSERT INTO categories (name, type) VALUES ('Transport', 'EXPENSE');
INSERT INTO categories (name, type) VALUES ('Utilities', 'EXPENSE');

INSERT INTO users (name, email) VALUES ('Alice Smith',  'alice@bank.com');
INSERT INTO users (name, email) VALUES ('Bob Jones',    'bob@bank.com');
INSERT INTO users (name, email) VALUES ('Carol White',  'carol@bank.com');
INSERT INTO users (name, email) VALUES ('Dave Brown',   'dave@bank.com');
INSERT INTO users (name, email) VALUES ('Eve Davis',    'eve@bank.com');

INSERT INTO savings_goals (user_id, name, target_amount, current_amount, deadline) VALUES (1, 'Holiday Fund',   2000.00,  500.00, '2026-12-01');
INSERT INTO savings_goals (user_id, name, target_amount, current_amount, deadline) VALUES (1, 'Emergency Fund', 5000.00, 1200.00, '2027-01-01');
INSERT INTO savings_goals (user_id, name, target_amount, current_amount, deadline) VALUES (2, 'New Laptop',     1500.00,  750.00, '2026-09-01');
INSERT INTO savings_goals (user_id, name, target_amount, current_amount, deadline) VALUES (3, 'Wedding Fund',  10000.00, 3000.00, '2027-06-01');

INSERT INTO transactions (user_id, category_id, amount, txn_date, description, type) VALUES (1, 1, 3500.00, '2026-05-01', 'May salary',        'INCOME');
INSERT INTO transactions (user_id, category_id, amount, txn_date, description, type) VALUES (1, 3,   45.50, '2026-05-03', 'Team lunch',         'EXPENSE');
INSERT INTO transactions (user_id, category_id, amount, txn_date, description, type) VALUES (1, 4,   12.00, '2026-05-05', 'Bus pass',           'EXPENSE');
INSERT INTO transactions (user_id, category_id, amount, txn_date, description, type) VALUES (1, 5,   95.00, '2026-05-06', 'Electricity bill',   'EXPENSE');
INSERT INTO transactions (user_id, category_id, amount, txn_date, description, type) VALUES (2, 1, 4200.00, '2026-05-01', 'May salary',         'INCOME');
INSERT INTO transactions (user_id, category_id, amount, txn_date, description, type) VALUES (2, 2,  800.00, '2026-05-10', 'Freelance project',  'INCOME');
INSERT INTO transactions (user_id, category_id, amount, txn_date, description, type) VALUES (2, 5,  120.00, '2026-05-07', 'Gas bill',           'EXPENSE');
INSERT INTO transactions (user_id, category_id, amount, txn_date, description, type) VALUES (3, 1, 3100.00, '2026-04-01', 'April salary',       'INCOME');
INSERT INTO transactions (user_id, category_id, amount, txn_date, description, type) VALUES (3, 3,   55.00, '2026-04-15', 'Dinner out',         'EXPENSE');
INSERT INTO transactions (user_id, category_id, amount, txn_date, description, type) VALUES (4, 1, 3800.00, '2026-04-01', 'April salary',       'INCOME');
INSERT INTO transactions (user_id, category_id, amount, txn_date, description, type) VALUES (4, 2,  500.00, '2026-04-12', 'Freelance writing',  'INCOME');
INSERT INTO transactions (user_id, category_id, amount, txn_date, description, type) VALUES (5, 1, 2900.00, '2026-03-01', 'March salary',       'INCOME');
INSERT INTO transactions (user_id, category_id, amount, txn_date, description, type) VALUES (5, 3,   40.00, '2026-03-10', 'Lunch',              'EXPENSE');
INSERT INTO transactions (user_id, category_id, amount, txn_date, description, type) VALUES (1, 1, 3500.00, '2026-04-01', 'April salary',       'INCOME');
INSERT INTO transactions (user_id, category_id, amount, txn_date, description, type) VALUES (1, 3,   62.00, '2026-04-08', 'Grocery run',        'EXPENSE');
