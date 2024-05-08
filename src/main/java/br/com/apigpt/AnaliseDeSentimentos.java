
package br.com.apigpt;



import com.theokanning.openai.OpenAiHttpException;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.util.Arrays;
import java.util.stream.Collectors;

public class AnaliseDeSentimentos {

    public static void main(String[] args) {
        var tentativas = 0;
        var promptSistema = """
                Você é um analisador de sentimentos de avaliações de produtos.
                Escreva um parágrafo com até 50 palavras resumindo as avaliações e depois atribua qual o sentimento geral para o produto.
                Identifique também 3 pontos fortes e 3 pontos fracos identificados a partir das avaliações.
                                
                #### Formato de saída
                Nome do produto:
                Resumo das avaliações: [resuma em até 50 palavras]
                Sentimento geral: [deve ser: POSITIVO, NEUTRO ou NEGATIVO]
                Pontos fortes: [3 bullets points]
                Pontos fracos: [3 bullets points]
                """;

        //Acessa o diretório dos arquivos de avaliação
        var diretorioDeAvaliacoes = Path.of("src/main/resources/avaliacoes");
        try {
            //Armazena os arquivos do diretório
            var arquivosDeAvaliacoes = Files.walk(diretorioDeAvaliacoes, 1).
                    filter(path -> path.toString().endsWith(".txt")).
                    collect(Collectors.toList());
            //Itera sobre a lista de arquivos, usando o conteúdo como prompt de usuário
            for (Path arquivo : arquivosDeAvaliacoes){
            System.out.println("Analisando o arquivo : "+ arquivo.getFileName());
            var promptUsuario = carregarArquivo(arquivo);

            var request = ChatCompletionRequest
                    .builder()
                    .model("gpt-3.5-turbo-1106")
                    .messages(Arrays.asList(
                            new ChatMessage(
                                    ChatMessageRole.SYSTEM.value(),
                                    promptSistema),
                            new ChatMessage(
                                    ChatMessageRole.USER.value(),
                                    promptUsuario)))
                    .build();

            var chave = System.getenv("OPENAI_API_KEY");
            var service = new OpenAiService(chave, Duration.ofSeconds(60));

            while (tentativas !=5)
                try {
                    var resposta = service
                            .createChatCompletion(request)
                            .getChoices().get(0).getMessage().getContent();
                    salvarAnalise(arquivo.getFileName().toString(), resposta);
                } catch (OpenAiHttpException e){
                    var errorCode = e.statusCode;
                    System.out.println("Erro de API - Código: " + errorCode);
                    switch (errorCode){
                        case 401 -> throw  new RuntimeException("Chave de API inválida ou expirada");
                        case 500, 503 -> {
                            System.out.println("Ocorreu algum erro durante a conexão com a API, fazendo nova tentativa");
                            Thread.sleep(1000*5);
                            tentativas += 1;
                        }

                    }

                }


            }
        } catch (Exception e){
            System.out.println("Ocorreu algum erro durante a leitura dos arquivos");
        }
        System.out.println("Análise finalizada");
    }


    private static String carregarArquivo(Path arquivo) {
        try {
            return Files.readAllLines(arquivo).toString();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar o arquivo!", e);
        }
    }

    private static void salvarAnalise(String arquivo, String analise) {
        try {
            var path = Path.of("src/main/resources/analises/analise-sentimentos-" +arquivo );
            Files.writeString(path, analise, StandardOpenOption.CREATE_NEW);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar o arquivo!", e);
        }
    }

}



