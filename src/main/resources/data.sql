-- Inserir dados iniciais para teste

-- Planos
INSERT INTO planos (nome, descricao, valor, duracao_dias, ativo, data_criacao) VALUES
('Mensal', 'Plano mensal com acesso completo', 89.90, 30, true, NOW()),
('Trimestral', 'Plano trimestral com desconto', 239.90, 90, true, NOW()),
('Anual', 'Plano anual com maior desconto', 899.90, 365, true, NOW());

-- Usuários
INSERT INTO usuarios (nome, email, cpf, telefone, tipo, ativo, data_criacao) VALUES
('João Silva', 'joao.silva@email.com', '12345678901', '11999999999', 'ALUNO', true, NOW()),
('Maria Santos', 'maria.santos@email.com', '12345678902', '11888888888', 'INSTRUTOR', true, NOW()),
('Admin Sistema', 'admin@academia.com', '12345678903', '11777777777', 'ADMINISTRADOR', true, NOW());

-- Matrículas
INSERT INTO matriculas (data_inicio, data_fim, status, aluno_id, plano_id, data_criacao) VALUES
('2024-01-01', '2024-01-31', 'ATIVA', 1, 1, NOW());