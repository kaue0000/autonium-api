# Requisitos Funcionais

> Convenção: `RFxxx` é o requisito principal e `RFxxx.y` é um subrequisito (regra, validação ou efeito do requisito principal).
> Ator único do sistema: **administrador**. Os revendedores são cadastros administrados por ele e não acessam a plataforma.

## Cadastro de revendedores

- **RF001 – Cadastro de revendedor:** O sistema deve permitir que o administrador cadastre novos revendedores, informando nome, CPF, data de nascimento, endereço e a porcentagem de revenda.
  - **RF001.1 – Validar CPF:** O sistema deve impedir o cadastro caso o CPF já esteja registrado.

## Acesso

- **RF002 – Login do administrador:** O sistema deve permitir que o administrador acesse a plataforma, informando e-mail e senha.
  - **RF002.1 – Validar credenciais:** O sistema deve negar o acesso e exibir mensagem de erro caso as credenciais sejam inválidas.
- **RF003 – Logout do administrador:** O sistema deve permitir que o administrador autenticado encerre sua sessão.
  - **RF003.1 – Redirecionar após logout:** O sistema deve redirecionar o administrador para a tela de login após o encerramento da sessão.
  - **RF003.2 – Restringir acesso após logout:** O sistema deve impedir o acesso às áreas restritas até que o administrador se autentique novamente.

## Equipamentos e empréstimos

- **RF004 – Cadastro de equipamentos:** O sistema deve permitir que o administrador cadastre equipamentos utilizados na produção ou no armazenamento, informando nome, precificação, consumo de energia (se houver), se está ativo para uso e se passou por manutenção.
- **RF005 – Desativar equipamentos:** O sistema deve permitir que o administrador desative equipamentos cadastrados que não serão mais utilizados.
  - **RF005.1 – Validar equipamento emprestado:** O sistema deve impedir a desativação de um equipamento que esteja emprestado.
- **RF006 – Reativar equipamentos:** O sistema deve permitir que o administrador reative equipamentos desativados.
- **RF007 – Empréstimo de equipamentos:** O sistema deve permitir que o administrador registre o empréstimo de equipamentos para os revendedores, informando as datas de início e de término prevista.
  - **RF007.1 – Validar disponibilidade do equipamento:** O sistema deve impedir o empréstimo de um equipamento que esteja desativado ou que já esteja emprestado.
  - **RF007.2 – Registrar devolução de equipamento:** O sistema deve permitir que o administrador registre a devolução de um equipamento emprestado, informando a data de devolução.
  - **RF007.3 – Encerrar empréstimo:** O sistema deve marcar o empréstimo como encerrado e disponibilizar novamente o equipamento após o registro da devolução.

## Desativação e reativação de revendedores

- **RF008 – Desativação de revendedor:** O sistema deve permitir que o administrador desative revendedores cadastrados, informando o motivo da desativação.
  - **RF008.1 – Validar empréstimos ativos:** O sistema deve permitir a desativação somente se o revendedor não possuir empréstimo de equipamentos ativo (sem devolução registrada).
- **RF009 – Registro do momento de desativação de revendedor:** O sistema deve guardar, no momento da desativação, o valor financeiro total que o revendedor gerou para o negócio.
- **RF010 – Reativação de revendedor:** O sistema deve permitir que o administrador reative revendedores desativados.

## Insumos

- **RF011 – Cadastrar insumos:** O sistema deve permitir que o administrador cadastre os insumos, informando o nome do insumo, o tipo de insumo (ex.: leite), a marca, a quantidade unitária e a grandeza.
- **RF012 – Desativar insumos:** O sistema deve permitir que o administrador desative insumos que não serão mais utilizados.
- **RF013 – Reativar insumos:** O sistema deve permitir que o administrador reative insumos desativados.
- **RF014 – Registrar entrada de insumos:** O sistema deve permitir que o administrador registre a entrada de insumos, informando o insumo, a quantidade e a data de validade.

## Produtos

- **RF015 – Cadastrar produtos:** O sistema deve permitir que o administrador cadastre novos produtos, informando o nome do produto, o seu sabor e o preço unitário.
  - **RF015.1 – Restringir informação do preço:** O sistema deve exigir que o preço seja informado somente na primeira vez em que o produto é criado.
- **RF016 – Desativar produtos:** O sistema deve permitir que o administrador desative produtos que não serão mais produzidos.
- **RF017 – Reativar produtos:** O sistema deve permitir que o administrador reative produtos desativados.
- **RF018 – Alterar preço de produtos:** O sistema deve permitir que o administrador altere o preço do produto desejado.

## Receitas

- **RF019 – Cadastrar receita:** O sistema deve permitir que o administrador cadastre receitas para cada relação produto-sabor, informando uma lista de ingredientes, cada um deles com a sua quantidade necessária.
- **RF020 – Modificar receita:** O sistema deve permitir que o administrador altere os ingredientes de uma receita.
- **RF021 – Desativar receita:** O sistema deve permitir que o administrador desative receitas.

## Produção

- **RF022 – Registrar produção:** O sistema deve permitir que o administrador registre a produção de um produto, informando produto e quantidade.
  - **RF022.1 – Validar receita:** O sistema deve verificar se o produto possui receita vinculada antes de registrar a produção.
  - **RF022.2 – Validar insumos:** O sistema deve verificar se o estoque de insumos é suficiente para a quantidade produzida.
  - **RF022.3 – Baixar insumos:** O sistema deve debitar do estoque de insumos as quantidades consumidas, conforme a receita.
- **RF023 – Armazenar produção:** O sistema deve armazenar cada produção registrada, mantendo o produto, a quantidade produzida e a data de fabricação.
  - **RF023.1 – Atualizar estoque de produtos:** O sistema deve acrescentar a quantidade produzida ao estoque de produtos.

## Revenda e vendas

- **RF024 – Revenda de produtos:** O sistema deve permitir que o administrador atribua produtos armazenados a revendedores, informando o revendedor, os produtos (com seus sabores) e suas quantidades.
  - **RF024.1 – Validar estoque da empresa:** O sistema deve verificar se há quantidade suficiente de cada produto-sabor no estoque da empresa antes de atribuí-lo ao revendedor.
  - **RF024.2 – Baixar estoque da empresa:** O sistema deve retirar do estoque da empresa os produtos-sabores enviados para revenda.
  - **RF024.3 – Registrar histórico de envio:** O sistema deve registrar em um histórico a data, os produtos, os sabores e as quantidades enviados ao revendedor.
  - **RF024.4 – Creditar estoque do revendedor:** O sistema deve acrescentar ao estoque do revendedor, por produto (sem distinção de sabor), as quantidades enviadas para revenda.
- **RF025 – Registrar acerto do revendedor:** O sistema deve permitir que o administrador registre o acerto de um revendedor, informando os produtos e as quantidades restantes em seu poder.
  - **RF025.1 – Validar quantidade restante:** O sistema deve impedir o registro caso a quantidade restante informada de um produto seja maior que a existente no estoque do revendedor.
  - **RF025.2 – Calcular quantidade vendida:** O sistema deve calcular a quantidade vendida de cada produto pela diferença entre o estoque do revendedor e a quantidade restante informada.
  - **RF025.3 – Atualizar estoque do revendedor:** O sistema deve atualizar o estoque do revendedor de cada produto para a quantidade restante informada.
  - **RF025.4 – Registrar histórico de venda:** O sistema deve registrar em um histórico a data, o tipo da venda (direta ou por revendedor), o revendedor (quando houver), os produtos, as quantidades vendidas e os preços unitários.
  - **RF025.5 – Atualizar totais do revendedor:** O sistema deve atualizar, a cada acerto registrado, o número de vendas (lotes), a quantidade de produtos vendidos e o valor total vendido pelo revendedor.
- **RF026 – Calcular repasse da venda:** O sistema deve calcular, a cada acerto registrado, o valor devido ao produtor e o valor devido ao revendedor, a partir dos preços unitários dos produtos vendidos e da porcentagem de revenda do revendedor.
- **RF027 – Registrar venda direta:** O sistema deve permitir que o administrador registre a venda de produtos realizada diretamente pelo produtor, informando os produtos (com seus sabores) e as quantidades vendidas.
  - **RF027.1 – Validar estoque da empresa:** O sistema deve impedir o registro caso a quantidade informada de um produto-sabor seja maior que a existente no estoque da empresa.
  - **RF027.2 – Baixar estoque da empresa:** O sistema deve remover do estoque da empresa a quantidade vendida de cada produto-sabor.
  - **RF027.3 – Registrar venda direta no histórico:** O sistema deve registrar a venda direta no histórico de vendas, conforme descrito no RF025.4.
