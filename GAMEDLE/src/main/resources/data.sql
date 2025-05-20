INSERT INTO app_user (id, email, password, rol, name) VALUES
('8f1b7e1f-5d67-4d3e-9c0a-4c547a9c1510', 'admin@gamedle.com',   'admin123',   'ADMIN', 'Super Admin'),
('1b2c3d4e-5f60-4a11-8899-0abcde123456', 'juan@gamedle.com',    'passJuan',   'USER',  'Juan Pérez'),
('2c3d4e5f-6172-4b22-99aa-1bcdef234567', 'maria@gamedle.com',   'passMaria',  'USER',  'María López'),
('3d4e5f60-7283-4c33-aabb-2cdef3456789', 'lucas@gamedle.com',   'passLucas',  'USER',  'Lucas García'),
('4e5f6071-8394-4d44-bbcc-3def4567890a', 'ana@gamedle.com',     'passAna',    'USER',  'Ana Sánchez'),
('5f607182-94a5-4e55-ccdd-4ef567890ab1', 'sofia@gamedle.com',   'passSofia',  'USER',  'Sofía Ramírez'),
('60718293-a5b6-4f66-ddee-5f67890abc12', 'carlos@gamedle.com',  'passCarlos', 'USER',  'Carlos Díaz'),
('718293a4-b6c7-5077-eeff-67890abcd123', 'paula@gamedle.com',   'passPaula',  'USER',  'Paula Ruiz'),
('8293a4b5-c7d8-5188-ff00-7890bcde2345', 'david@gamedle.com',   'passDavid',  'USER',  'David Molina'),
('93a4b5c6-d8e9-5299-0011-890cdef34567', 'laura@gamedle.com',   'passLaura',  'USER',  'Laura Gómez');


INSERT INTO game_score (id, user_id, played_at, attempts_wordle, success_wordle) VALUES
('a1010001-0000-0000-0000-000000000001', '8f1b7e1f-5d67-4d3e-9c0a-4c547a9c1510', '2025-05-20', 2,  TRUE),
('a1010002-0000-0000-0000-000000000002', '8f1b7e1f-5d67-4d3e-9c0a-4c547a9c1510', '2025-05-19', 5,  FALSE),
('a1010003-0000-0000-0000-000000000003', '8f1b7e1f-5d67-4d3e-9c0a-4c547a9c1510', '2025-05-18', 3,  TRUE),
('a1010004-0000-0000-0000-000000000004', '8f1b7e1f-5d67-4d3e-9c0a-4c547a9c1510', '2025-05-17', 4,  TRUE),
('a1010005-0000-0000-0000-000000000005', '8f1b7e1f-5d67-4d3e-9c0a-4c547a9c1510', '2025-05-16', 6,  FALSE),
('a1020001-0000-0000-0000-000000000006', '1b2c3d4e-5f60-4a11-8899-0abcde123456', '2025-05-20', 3, TRUE),
('a1020002-0000-0000-0000-000000000007', '1b2c3d4e-5f60-4a11-8899-0abcde123456', '2025-05-19', 4, TRUE),
('a1020003-0000-0000-0000-000000000008', '1b2c3d4e-5f60-4a11-8899-0abcde123456', '2025-05-18', 2, TRUE),
('a1020004-0000-0000-0000-000000000009', '1b2c3d4e-5f60-4a11-8899-0abcde123456', '2025-05-17', 6, FALSE);

INSERT INTO words (id, date, wordle_word) VALUES
('bf202020-2222-3333-4444-555566667777', '2025-05-19', 'shove'),
('cf303030-3333-4444-5555-666677778888', '2025-05-18', 'carry');