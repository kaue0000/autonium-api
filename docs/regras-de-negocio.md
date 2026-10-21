# Regras de Negócio

> Convenção: `RNxxx` identifica uma regra de negócio, ou seja, uma premissa do domínio que vale independentemente do sistema e que orienta os requisitos.
> Os requisitos funcionais estão em [requisitos-funcionais.md](requisitos-funcionais.md) e os não funcionais em [requisitos-nao-funcionais.md](requisitos-nao-funcionais.md).

## Produtos e preços

- **RN001 – Preço único por produto:** O preço de um produto é o mesmo para todos os sabores.
  - Relaciona-se com: RF015, RF015.1, RF018, RF026.

## Estoque do revendedor

- **RN002 – Estoque por produto:** O estoque do revendedor é controlado por produto, sem distinção de sabor.
  - Relaciona-se com: RF024.4, RF025.
- **RN003 – Cálculo da quantidade vendida:** A quantidade vendida por um revendedor é calculada pela diferença entre o estoque dele e a quantidade restante informada no acerto.
  - Relaciona-se com: RF025.2.

## Repasse

- **RN004 – Cálculo do repasse:** O valor repassado ao revendedor é calculado pela porcentagem de revenda cadastrada para ele, e o restante pertence ao produtor.
  - Relaciona-se com: RF001, RF026.

## Usuários e acesso

- **RN005 – Ator único:** O sistema possui um único ator, o administrador; os revendedores são cadastros e não acessam a plataforma.
  - Relaciona-se com: RF002, RF003.
