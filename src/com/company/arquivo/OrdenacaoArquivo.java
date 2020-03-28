package com.company.arquivo;

import com.company.models.Aluno;
import com.company.models.VetorDeOrdenacao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrdenacaoArquivo {
    private final static int TAMANHO_VETOR = 1000;
    private static int ultimaPosicaoLidaDoArquivoOriginal = 0;
    private static List<String> nomesArquivosAuxiliares = new ArrayList<>();
    private int quantidadeArquivosAuxiliares = 0;

    public static void ordenarArquivoBinario(String nomeArquivoEntrada, String nomeArquivoSaida) {
        int arquivoAuxiliar = 0;
        // Divisão em blocos de arquivos ordenados
        // 1- Inicializar o vetor com os primeiros TAMANHO_VETOR registros
        VetorDeOrdenacao[] vetor = inicializaVetor(nomeArquivoEntrada);

        //Divide os registros em blocos presentes em diferentes arquivos
        while (existeRegistroNoVetor(vetor)) {
            adicionaNomesAListaDeArquivos(arquivoAuxiliar);
            while (existeRegistroNaoCongeladoNoVetor(vetor)) {
                // 2- Buscar menor registro possível para ir para o arquivo
                Aluno menorAluno = buscaMenorAlunoDoVetor(vetor);
                // 3 - Adicionar registro ao arquivo
                adicionarAoArquivoDeSaida(menorAluno, nomesArquivosAuxiliares.get(arquivoAuxiliar));
                // 4- Substituir no vetor o registro pelo próximo do arquivo de entrada
                // 5- Caso ele venha antes do que foi gravado agora, considere-o congelado
                substituirAlunoVetor(nomeArquivoEntrada, menorAluno, vetor);
            }

            vetor = descongelaTodosOsRegistros(vetor);
            arquivoAuxiliar++;
        }

        System.out.println("SAI");
    }

    private static boolean existeRegistroNoVetor(VetorDeOrdenacao[] vetor) {
        for (int i=0; i<TAMANHO_VETOR; i++) {
            if (vetor[i].getAluno() != null)
                return true;
        }

        return false;
    }

    private static VetorDeOrdenacao[] descongelaTodosOsRegistros(VetorDeOrdenacao[] vetor) {
        for (int i=0; i<TAMANHO_VETOR; i++) {
            vetor[i].setCongelado(false);
        }

        return vetor;
    }

    //Verifica se ainda é possível colocar algum registro no arquivo auxiliar
    private static boolean existeRegistroNaoCongeladoNoVetor(VetorDeOrdenacao[] vetor) {
        for (int i=0; i<TAMANHO_VETOR; i++)
            if (!vetor[i].isCongelado() && vetor[i].getAluno() != null)
                return true;

        return false;
    }

    //Tira o aluno que foi colocado no arquivo, substituindo pelo próximo do arquivo original
    //Se ele for menor que o ultimo colocado, congela
    private static VetorDeOrdenacao[] substituirAlunoVetor(String nomeArquivoEntrada, Aluno aluno, VetorDeOrdenacao[] vetor) {
        Aluno novoAluno = buscaProximoAlunoArquivoOriginal(nomeArquivoEntrada);
        boolean congelado = false;
        if (novoAluno != null) {
            if (novoAluno.getNome().compareTo(aluno.getNome()) < 0)
                congelado = true;
        }

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
    private static void adicionaNomesAListaDeArquivos(int arquivoNumero) {
        nomesArquivosAuxiliares.add("arquivoAux" + arquivoNumero + ".dat");
    }

    //Coloca o menor encontrado no vetor no arquivo auxiliar de saída atual
    private static void adicionarAoArquivoDeSaida(Aluno menorAluno, String nomeArquivoSaida) {
        if (menorAluno != null) {
            ArquivoBinarioAcessoAleatorio arquivoSaida = new ArquivoBinarioAcessoAleatorio(nomeArquivoSaida);
            arquivoSaida.adicionarAlunoNoArquivo(menorAluno);
        }
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
