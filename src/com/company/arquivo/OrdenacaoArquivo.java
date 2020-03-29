package com.company.arquivo;

import com.company.models.Aluno;
import com.company.models.AlunoAuxiliar;
import com.company.models.ArquivoAuxiliar;
import com.company.models.VetorDeOrdenacao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OrdenacaoArquivo {
    private final static int TAMANHO_VETOR = 100000;
    private static int ultimaPosicaoLidaDoArquivoOriginal = 0;
    private static List<ArquivoAuxiliar> arquivosAuxiliares = new ArrayList<>();
    private static List<ArquivoAuxiliar> arquivosSaida = new ArrayList<>();
    private static int quantidadeTotalArquivosAuxiliares = 4;

    public static void ordenarArquivoBinario(String nomeArquivoEntrada, String nomeArquivoFinal) {
        int arquivoAuxiliar = 1;
        boolean repetiu = false;

        // Divisão em blocos de arquivos ordenados
        // 1- Inicializar o vetor com os primeiros TAMANHO_VETOR registros
        VetorDeOrdenacao[] vetor = inicializaVetor(nomeArquivoEntrada);


        //Divide os registros em blocos presentes em diferentes arquivos
        while (existeRegistroNoVetor(vetor)) {
            if (!repetiu)
                adicionaNomesAListaDeArquivos(arquivoAuxiliar);
            while (existeRegistroNaoCongeladoNoVetor(vetor)) {
                // 2- Buscar menor registro possível para ir para o arquivo
                Aluno menorAluno = buscaMenorAlunoDoVetor(vetor);
                // 3 - Adicionar registro ao arquivo
                adicionarAoArquivoDeSaida(menorAluno, arquivosAuxiliares.get(arquivoAuxiliar-1).getNome());
                // 4- Substituir no vetor o registro pelo próximo do arquivo de entrada
                // 5- Caso ele venha antes do que foi gravado agora, considere-o congelado
                substituirAlunoVetor(nomeArquivoEntrada, menorAluno, vetor);
            }

            adicionarBlocoAoArquivo(arquivosAuxiliares.get(arquivoAuxiliar-1).getNome());
            descongelaTodosOsRegistros(vetor);
            if (arquivoAuxiliar == quantidadeTotalArquivosAuxiliares) {
                arquivoAuxiliar = 1;
                repetiu = true;
            }
            else
                arquivoAuxiliar++;
        }

        //Intercalação dos arquivos
        inicializaArquivosSaida();
        intercalacaoArquivos();
        renomeiaArquivoFinal(nomeArquivoFinal);
    }

    private static void renomeiaArquivoFinal(String nomeArquivoFinal) {

        for (ArquivoAuxiliar arquivoAuxiliar : arquivosAuxiliares) {
            if (! arquivoAuxiliar.isLivre()) {
                ArquivoBinarioAcessoAleatorio arquivoBinarioAcessoAleatorio = new ArquivoBinarioAcessoAleatorio(arquivoAuxiliar.getNome());
                arquivoBinarioAcessoAleatorio.renomearArquivo(nomeArquivoFinal);
                break;
            }
        }
    }

    private static void inicializaArquivosSaida() {
        ArquivoBinarioAcessoAleatorio arquivoBinarioAcessoAleatorio;
        for (int i=1; i<=quantidadeTotalArquivosAuxiliares; i++)  {
            arquivosSaida.add(new ArquivoAuxiliar("saida" + i, 0));
            arquivoBinarioAcessoAleatorio = new ArquivoBinarioAcessoAleatorio("saida" + i);
            arquivoBinarioAcessoAleatorio.apagarArquivo();
        }
    }

    private static void intercalacaoArquivos() {
        List<AlunoAuxiliar> alunos;
        int arquivoSaida = 1;
        boolean entrei;

        while(existeMaisDeUmArquivoNaoVazio()) {
            while (existeConteudoArquivos()) {
                entrei = false;
                alunos = buscaProximoRegistroDeCadaArquivo();
                while (alunos.size() > 0) {
                    entrei = true;
                    Collections.sort(alunos);
                    adicionarAoArquivoDeSaida(alunos.get(0).getAluno(), arquivosSaida.get(arquivoSaida - 1).getNome());
                    atualizaPosicaoArquivo(alunos.get(0).getNomeArquivo());
                    alunos = buscaProximoRegistroDeCadaArquivo();
                }
                if (entrei)
                    adicionarBlocoAoArquivo(arquivosSaida.get(arquivoSaida - 1).getNome());
                if (arquivoSaida == quantidadeTotalArquivosAuxiliares) {
                    arquivoSaida = 1;
                } else
                    arquivoSaida++;
                desbloquearArquivos();
            }

            renomearArquivosSaida();
            liberaArquivos();
        }
    }

    private static void atualizaPosicaoArquivo(String nomeArquivo) {
        for (ArquivoAuxiliar arquivoAuxiliar : arquivosAuxiliares) {
            if (arquivoAuxiliar.getNome().equals(nomeArquivo)) {
                int posicao = arquivoAuxiliar.getPosicao();
                arquivoAuxiliar.setPosicao(posicao + 1);
            }
        }
    }

    private static boolean existeMaisDeUmArquivoNaoVazio() {
        int count = 0;
        for (ArquivoAuxiliar arquivoAuxiliar : arquivosAuxiliares) {
            if (! arquivoAuxiliar.isLivre())
                count++;
        }

        return count > 1;
    }

    private static void liberaArquivos() {
        ArquivoBinarioAcessoAleatorio arquivo;
        for (ArquivoAuxiliar arquivoAuxiliar : arquivosAuxiliares) {
            arquivo = new ArquivoBinarioAcessoAleatorio(arquivoAuxiliar.getNome());
            if (!arquivo.estaVazio()) {
                arquivoAuxiliar.setLivre(false);
                arquivoAuxiliar.setPosicao(0);
            }
        }
    }

    private static void renomearArquivosSaida() {
        ArquivoBinarioAcessoAleatorio arquivo;
        int i = 1;
        for (ArquivoAuxiliar arquivoAuxiliar : arquivosSaida) {
            arquivo = new ArquivoBinarioAcessoAleatorio(arquivoAuxiliar.getNome());
            arquivo.renomearArquivo(Integer.toString(i));
            i++;
        }
    }

    private static void desbloquearArquivos() {
        for (ArquivoAuxiliar arquivoAuxiliar : arquivosAuxiliares) {
            if (!arquivoAuxiliar.isLivre() && arquivoAuxiliar.isBloqueado())
                arquivoAuxiliar.setBloqueado(false);
        }
    }

    private static boolean existeConteudoArquivos() {
        for (ArquivoAuxiliar arquivoAuxiliar : arquivosAuxiliares) {
            if(! arquivoAuxiliar.isLivre())
                return true;
        }

        return false;
    }

    private static List<AlunoAuxiliar> buscaProximoRegistroDeCadaArquivo() {
        Aluno aluno;
        List<AlunoAuxiliar> alunos = new ArrayList<>();
        int posicao;

        ArquivoBinarioAcessoAleatorio arquivo;
        for (ArquivoAuxiliar arq : arquivosAuxiliares) {
            if (! arq.isBloqueado() && ! arq.isLivre()) {
                arquivo = new ArquivoBinarioAcessoAleatorio(arq.getNome());
                posicao = arq.getPosicao();
                aluno = arquivo.procurarAlunoPorPosicaoNoArquivo(posicao);
                if (aluno.getMatricula() == -1) {
                    arq.setBloqueado(true);
                    aluno = arquivo.procurarAlunoPorPosicaoNoArquivo(posicao+1);
                    if (aluno == null){
                        arq.setLivre(true);
                        arquivo.apagarArquivo();
                    }
                } else {
                    AlunoAuxiliar alunoAuxiliar = new AlunoAuxiliar(aluno, arq.getNome());
                    alunos.add(alunoAuxiliar);
                }
            }
        }

        return alunos;
    }

    private static void adicionarBlocoAoArquivo(String nomeArquivo) {
        ArquivoBinarioAcessoAleatorio arquivo = new ArquivoBinarioAcessoAleatorio(nomeArquivo);
        arquivo.adicionarAlunoNoArquivo(new Aluno(";", -1, -1L));
    }

    private static boolean existeRegistroNoVetor(VetorDeOrdenacao[] vetor) {
        for (int i=0; i<TAMANHO_VETOR; i++) {
            if (vetor[i].getAluno() != null)
                return true;
        }

        return false;
    }

    private static void descongelaTodosOsRegistros(VetorDeOrdenacao[] vetor) {
        for (int i=0; i<TAMANHO_VETOR; i++) {
            vetor[i].setCongelado(false);
        }

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
    private static void substituirAlunoVetor(String nomeArquivoEntrada, Aluno aluno, VetorDeOrdenacao[] vetor) {
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

    }

    //Busca próximo registro no arquivo original
    private static Aluno buscaProximoAlunoArquivoOriginal(String nomeArquivoEntrada) {
        ArquivoBinarioAcessoAleatorio arquivoEntrada = new ArquivoBinarioAcessoAleatorio(nomeArquivoEntrada);
        Aluno aluno = arquivoEntrada.procurarAlunoPorPosicaoNoArquivo(ultimaPosicaoLidaDoArquivoOriginal);
        ultimaPosicaoLidaDoArquivoOriginal++;
        return aluno;
    }

    //Adiciona os nomes dos arquivos auxiliares na lista de arquivos
    private static void adicionaNomesAListaDeArquivos(int arquivoAuxiliar) {
        ArquivoAuxiliar auxiliar = new ArquivoAuxiliar(Integer.toString(arquivoAuxiliar), 0);
        ArquivoBinarioAcessoAleatorio arquivoBinarioAcessoAleatorio = new ArquivoBinarioAcessoAleatorio(auxiliar.getNome());
        arquivoBinarioAcessoAleatorio.apagarArquivo();
        arquivosAuxiliares.add(auxiliar);
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
