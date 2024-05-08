Documentação do Código: Análise de Sentimentos de Avaliações de Produtos
Objetivo:

O código Python apresentado tem como objetivo analisar o sentimento geral de avaliações de produtos e identificar pontos fortes e fracos mencionados. Ele utiliza a API OpenAI para gerar um resumo das avaliações e atribuir um sentimento geral (positivo, neutro ou negativo). Além disso, extrai três pontos fortes e três pontos fracos das avaliações.

Funcionalidades:

Carregar avaliações: O código busca por arquivos de avaliação no diretório "src/main/resources/avaliacoes".
Gerar análise: Para cada arquivo de avaliação, o código gera um prompt no formato especificado e envia para a API OpenAI. A API retorna um resumo das avaliações, o sentimento geral, pontos fortes e pontos fracos.
Salvar análise: A análise gerada para cada arquivo de avaliação é salva em um arquivo no diretório "src/main/resources/analises".
Descrição Detalhada:

Importações:

As bibliotecas com.theokanning.openai.completion.chat.* são utilizadas para interagir com a API OpenAI.
A biblioteca java.nio.file.* é utilizada para manipular arquivos.
A biblioteca java.time.Duration é utilizada para definir o tempo limite da requisição à API OpenAI.
A biblioteca java.util.Arrays é utilizada para criar listas de objetos.
A biblioteca java.util.stream.Collectors é utilizada para coletar dados em listas.
Método main:

Define o prompt do sistema que será enviado para o usuário.
Obtem o diretório dos arquivos de avaliação.
Percorre cada arquivo de avaliação:
Carrega o conteúdo do arquivo.
Cria um prompt no formato especificado, incluindo o conteúdo do arquivo de avaliação.
Envia o prompt para a API OpenAI e recebe a resposta.
Salva a análise gerada em um arquivo.
Trata possíveis exceções durante a leitura dos arquivos.
Método carregarArquivo:

Lê o conteúdo de um arquivo e o retorna como string.
Trata possíveis exceções durante a leitura do arquivo.
Método salvarAnalise:

Cria um arquivo com o nome especificado e salva a análise nele.
Trata possíveis exceções durante a gravação do arquivo.
Exemplos de Uso:

Para executar o código, é necessário ter a chave da API OpenAI configurada na variável de ambiente OPENAI_API_KEY. Além disso, os arquivos de avaliação devem estar no diretório "src/main/resources/avaliacoes".

O código gera um arquivo de análise para cada arquivo de avaliação no diretório "src/main/resources/analises". Cada arquivo de análise contém o nome do produto, resumo das avaliações, sentimento geral, pontos fortes e pontos fracos.
