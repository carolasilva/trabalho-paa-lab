package com.company.arquivo;

import com.company.models.Aluno;
import com.company.models.VetorDeOrdenacao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrdenacaoArquivo {
    private final static int TAMANHO_VETOR = 200;
    private static int ultimaPosicaoLidaDoArquivoOriginal = 0;
    private static List<String> nomesArquivosAuxiliares = new ArrayList<>();
    private int quantidadeArquivosAuxiliares = 0;

    public static void ordenarArquivoBinario(String nomeArquivoEntrada, String nomeArquivoSaida) {
        int arquivoAuxiliar = 0;
        // Divisão em blocos de arquivos ordenados
        // 1- Inicializar o vetor com os primeiros TAMANHO_VETOR registros
        VetorDeOrdenacao[] vetor = inicializaVetor(nomeArquivoEntrada);

        //Inicializar array com nomes dos arquivos auxiliares
        nomesArquivosAuxiliares = adicionaNomesAListaDeArquivos();

        while (existeRegistroNaoCongeladoNoVetor(vetor)) {
            System.out.println("----------------------------------------------");
            for (int i=0; i<TAMANHO_VETOR; i++) {
                boolean congelado = false;
                String asterisco = "";
                if (vetor[i].isCongelado())
                    asterisco = "*";
                System.out.println(vetor[i].getNome() + " " + asterisco);
            }
            // 2- Buscar menor registro possível para ir para o arquivo
            Aluno menorAluno = buscaMenorAlunoDoVetor(vetor);

            // 3 - Adicionar registro ao arquivo
            adicionarAoArquivoDeSaida(menorAluno, nomesArquivosAuxiliares.get(arquivoAuxiliar));

            // 4- Substituir no vetor o registro pelo próximo do arquivo de entrada
            // 5- Caso ele venha antes do que foi gravado agora, considere-o congelado
            vetor = substituirAlunoVetor(nomeArquivoEntrada, menorAluno, vetor);
        }


        // 6- Se ainda tiver algum registro não congelado, voltar para o passo 3
        // 7 - Caso contrário:
            // fechar o arquivo de saída
            // descongelar registros congelados
            // abrir nova partição de saída
            // voltar ao passo 2
    }

    private static boolean existeRegistroNaoCongeladoNoVetor(VetorDeOrdenacao[] vetor) {
        for (int i=0; i<TAMANHO_VETOR; i++)
            if (! vetor[i].isCongelado())
                return true;

        return false;
    }

    //Tira o aluno que foi colocado no arquivo, substituindo pelo próximo do arquivo original
    //Se ele for menor que o ultimo colocado, congela
    private static VetorDeOrdenacao[] substituirAlunoVetor(String nomeArquivoEntrada, Aluno aluno, VetorDeOrdenacao[] vetor) {
        Aluno novoAluno = buscaProximoAlunoArquivoOriginal(nomeArquivoEntrada);
        boolean congelado = false;
        if (novoAluno.getNome().compareTo(aluno.getNome()) < 0)
            congelado = true;

        VetorDeOrdenacao aux = new VetorDeOrdenacao(novoAluno, congelado);
        for (int i=0; i<TAMANHO_VETOR; i++) {
            if (vetor[i].getAluno() == aluno) {
                vetor[i] = aux;
            }
        }

        return vetor;
    }

    //Busca próximo registro no arquivo original
    private static Aluno buscaProximoAlunoArquivoOriginal(String nomeArquivoEntrada) {
        ArquivoBinarioAcessoAleatorio arquivoEntrada = new ArquivoBinarioAcessoAleatorio(nomeArquivoEntrada);
        Aluno aluno = arquivoEntrada.procurarAlunoPorPosicaoNoArquivo(ultimaPosicaoLidaDoArquivoOriginal);
        ultimaPosicaoLidaDoArquivoOriginal++;
        return aluno;
    }

    //Adiciona os nomes dos arquivos auxiliares na lista de arquivos
    private static List<String> adicionaNomesAListaDeArquivos() {
        List<String> lista = new ArrayList<>();
        for (int i=0; i<TAMANHO_VETOR; i++) {
            lista.add("arquivoAux" + i + ".dat");
        }

        return lista;
    }

    //Coloca o menor encontrado no vetor no arquivo auxiliar de saída atual
    private static void adicionarAoArquivoDeSaida(Aluno menorAluno, String nomeArquivoSaida) {
        ArquivoBinarioAcessoAleatorio arquivoSaida = new ArquivoBinarioAcessoAleatorio(nomeArquivoSaida);
        arquivoSaida.adicionarAlunoNoArquivo(menorAluno);
    }

    // Busca menor aluno no vetor que pode ser colocado no arquivo de saída
    private static Aluno buscaMenorAlunoDoVetor(VetorDeOrdenacao[] vetor) {
        Arrays.sort(vetor);
        return vetor[0].getAluno();
    }

    // Inicializa um vetor com os TAMANHO_VETOR primeiros registros do arquivo de entrada
    private static VetorDeOrdenacao[] inicializaVetor(String nomeArquivoEntrada) {
        ArquivoBinarioAcessoAleatorio arquivoEntrada = new ArquivoBinarioAcessoAleatorio(nomeArquivoEntrada);
        VetorDeOrdenacao[] vetor = new VetorDeOrdenacao[TAMANHO_VETOR];
        Aluno aluno;

        for (int i=0; i < TAMANHO_VETOR; i++) {
            aluno = arquivoEntrada.procurarAlunoPorPosicaoNoArquivo(ultimaPosicaoLidaDoArquivoOriginal);
            VetorDeOrdenacao aux = new VetorDeOrdenacao(aluno, false);
            vetor[i] = aux;
            ultimaPosicaoLidaDoArquivoOriginal++;
        }

        return vetor;
    }
}
