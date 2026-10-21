# Requisitos Não Funcionais

> Convenção: `RNFxxx` identifica um requisito não funcional, que descreve **como** o sistema deve se comportar (qualidade, restrições e padrões), e não o que ele faz.
> Ator único do sistema: **administrador**. Os revendedores são cadastros administrados por ele e não acessam a plataforma.
> Os requisitos funcionais estão em [requisitos-funcionais.md](requisitos-funcionais.md) e as regras de negócio em [regras-de-negocio.md](regras-de-negocio.md).

## Segurança

- **RNF001 – Armazenamento de senha:** O sistema deve armazenar a senha do administrador de forma criptografada, nunca em texto puro.
- **RNF002 – Controle de acesso:** O sistema deve exigir autenticação para visualização ou interação de qualquer funcionalidade, exceto o login.
- **RNF003 – Sessão:** O sistema deve encerrar automaticamente a sessão após 30 minutos de inatividade.

## Proteção de dados pessoais

- **RNF005 – Dados dos revendedores:** O sistema deve tratar os dados pessoais dos revendedores (nome, CPF, data de nascimento e endereço) conforme a LGPD, limitando o acesso ao administrador.

## Usabilidade

- **RNF006 – Mensagens de erro:** O sistema deve exibir mensagens de erro claras, em português, indicando o motivo do bloqueio (por exemplo, "estoque insuficiente de leite").
- **RNF007 – Interface adaptável:** O sistema deve ser utilizável em computador e em celular, pois o acerto com os revendedores é feito no estabelecimento deles.

## Desempenho

- **RNF008 – Tempo de resposta:** O sistema deve responder às operações de cadastro e consulta em até 2 segundos em condições normais de uso.

## Confiabilidade e integridade dos dados

- **RNF009 – Atomicidade das operações:** O sistema deve executar cada operação composta (como registrar produção, que valida e baixa insumos, ou registrar acerto) de forma atômica: se uma etapa falhar, nenhuma alteração deve ser gravada.
- **RNF010 – Preservação do histórico:** O sistema não deve excluir fisicamente registros que possuam vínculo com outros dados, utilizando apenas desativação (exclusão lógica).

## Implantação

- **RNF012 – Carga inicial:** O sistema deve ser implantado com os dados atuais da empresa (produtos, receitas, insumos, equipamentos, revendedores e seus respectivos estoques) já carregados.

## Manutenibilidade e portabilidade

- **RNF013 – Documentação:** O projeto deve manter a documentação (requisitos e diagramas) versionada no repositório.
