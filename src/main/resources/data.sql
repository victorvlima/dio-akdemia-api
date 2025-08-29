-- Inserir dados iniciais para teste

-- Planos
INSERT INTO tb_planos (nome, descricao, valor, duracao_dias, ativo, data_criacao) VALUES
('Mensal', 'Plano mensal com acesso completo', 89.90, 30, true, NOW()),
('Trimestral', 'Plano trimestral com desconto', 239.90, 90, true, NOW()),
('Anual', 'Plano anual com maior desconto', 899.90, 365, true, NOW());

-- Aluno
INSERT INTO tb_alunos (email, cpf, nome, telefone, tipo, numero_matricula, ativo, data_criacao) VALUES
('joao.silva@email.com', '11999999999', 'João Silva', '12345678901', 'ALUNO', '1', true, NOW()),
('maria.santos@email.com', '11888888888', 'Maria Santos', '12345678902', 'ALUNO', '2', true, NOW()),
('jose.moreira@email.com', '11777777777', 'José Moreira', '12345678903', 'ALUNO', '3', true, NOW());

-- Matrículas
INSERT INTO tb_matriculas (data_inicio, data_fim, data_matricula, status, aluno_id, plano_id) VALUES
('2024-01-01', '2024-01-31', NOW(), 'ATIVA', 1, 1);