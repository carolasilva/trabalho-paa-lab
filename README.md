# Trabalho de Laboratório de Pojeto e Análise de Algoritmos
## A especificação
  Nas aulas teóricas e práticas, estudamos o conceito de arquivos binários sequenciais, sequenciais de acesso aleatório e indexados. Vimos um exemplo prático da criação de um arquivo sequencial com registro de tamanho fixo. Este exercício tem como objetivo colocar em prática estes conhecimentos em uma atividade individual de laboratório.
  Como anexo, você terá acesso a um arquivo de dados em formato texto, contendo mais de 690.000 registros de dados no formato visto em laboratório: um identificador inteiro (matrícula), um nome (string) e uma nota entre 0 e 100, sendo permitida uma casa decimal. Cada linha do arquivo contém estas informações separadas por pontoe-vírgula (‘;’).

**Suas tarefas nestes exercícios são:**
  * Realizar a leitura do arquivo texto e gravar os dados em um arquivo sequencial binário de acesso aleatório;
    * Após a criação deste arquivo, permitir a busca e exibição dos dados de um aluno por sua posição no arquivo.
  * Criar um índice simples para este arquivo (sugere-se índice baseado na matrícula);
    * O índice pode ser criado com o auxílio do uso de estruturas de memória principal. No entanto, ao final da execução do programa, este índice deve ser gravado em arquivo para que, na próxima execução do programa, ele apenas seja lido, sem necessidade de recriação.
    * Com o índice criado, deve ser permitida a busca e exibição dos dados de um aluno por sua matrícula (ou outro atributo, caso você assim escolha)
  * No momento pedido pelo usuário, exibir um relatório em ordem alfabética dos alunos, paginando os registros de 20 em 20. O relatório também pode ser exibido, a pedido do usuário, a partir de uma posição específica do arquivo.
    * Para esta tarefa, você precisará realizar a ordenação dos dados do arquivo em memória
secundária e, posteriormente, refazer seu índice. 

## A implementação
  ### Parte 1
 O arquivo texto foi lido utilizando o Scanner do java, com cada linha sendo lida por completo e as devidas conversões realizadas para que a String lida fosse transformada no objeto Aluno criado.
 O arquivo binário foi criado utilizando a classe RandomAccessFile, que permite que um registro seja encontrado dada a sua posição. Na classe Aluno foi feito um tratamento para que ele sempre ocupe a mesma quantidade na memória (56 bytes)
    
  ### Parte 2
  Para a criação do índice, criou-se uma classe auxiliar contendo o arquivo e uma lista com os objetos do índice. A cada aluno adicionado no arquivo binário, adicionava-se a respectinha posição e matrícula na lista de índices. Ao final da conversão, o metódo salvarIndiceCompleto é chamado e percorre a lista, adicionando cada linha ao arquivo.
  
  ### Parte 3
 Para o arquivo ser ordenado, utilizou-se o algoritmo de intercalação com seleção por substituição com um total de 8 arquivos sendo utilizados e um vetor auxiliar com tamanho para X alunos. Logo após, o índice do mesmo é atualizado para que a função de busca por matrícula continuasse funcionando normalmente. 
