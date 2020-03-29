package com.company;

import com.company.arquivo.ArquivoBinarioAcessoAleatorio;
import com.company.arquivo.ArquivoTexto;
import com.company.arquivo.OrdenacaoArquivo;
import com.company.models.Aluno;
import com.company.models.ArquivoAuxiliar;

import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        File arquivos = new File("alunos.dat");
        ArquivoBinarioAcessoAleatorio arquivoBinario;
        if (!arquivos.exists()) {
            System.out.println("Aguarde enquanto transformamos o arquivo texto fornecido em binário...");
            ArquivoTexto arquivo = new ArquivoTexto("LabComp3_Exerc1_dadosTeste.txt");
            arquivoBinario = arquivo.transformaArquivoTextoEmBinario("alunos.dat");
        } else {
            arquivoBinario = new ArquivoBinarioAcessoAleatorio("alunos.dat");
        }

        int opcao;
        Scanner leitor = new Scanner(System.in);
        Aluno aluno;

        do {
            System.out.println("=== BEM VINDO AO GESTOR DE ALUNOS ===");
            System.out.println("1- Procurar aluno por posição");
            System.out.println("2- Procurar aluno por matrícula");
            System.out.println("3- Listar alunos em ordem alfabética");
            System.out.println("4- Sair");
            opcao = leitor.nextInt();
            leitor.nextLine();

            switch(opcao) {
                case 1: System.out.print("Posição do registro: ");
                    int pos = leitor.nextInt();
                    aluno = arquivoBinario.procurarAlunoPorPosicaoNoArquivo(pos);
                    if(aluno != null) System.out.println(aluno);
                    System.out.println();
                    break;
                case 2: System.out.println("Matrícula: ");
                    Long matricula = leitor.nextLong();
                    aluno = arquivoBinario.procurarAlunoPorMatricula(matricula);
                    if(aluno != null) System.out.println(aluno);
                    System.out.println();
                    break;
                case 3: System.out.println("Aguarde um momento enquanto o arquivo é ordenado");
                    OrdenacaoArquivo.ordenarArquivoBinario("alunos.dat", "alunosOrdenados.dat");
                    arquivoBinario = new ArquivoBinarioAcessoAleatorio("alunosOrdenados.dat");
                    arquivoBinario.atualizarIndice();
                    System.out.println("Ok, o arquivo foi ordenado!");
                    int posicao = 0, i=0;

                    String resposta = "";
                    while (! resposta.toUpperCase().equals("S") && ! resposta.toUpperCase().equals("N")) {
                        System.out.println("Você deseja começar a listagem de uma posição específica do arquivo? (S - N)");
                        resposta = leitor.nextLine();
                    }
                    if (resposta.toUpperCase().equals("S")) {
                        System.out.println("Digite a posição que você deseja começar a listar: ");
                        posicao = leitor.nextInt();
                        leitor.nextLine();
                    }

                    aluno = arquivoBinario.procurarAlunoPorPosicaoNoArquivo(posicao);
                    while (aluno.getMatricula() != -1) {
                        i = 0;
                        while (aluno.getMatricula() != -1 && i < 20) {
                            System.out.println(aluno);
                            posicao++;
                            aluno = arquivoBinario.procurarAlunoPorPosicaoNoArquivo(posicao);
                            i++;
                        }
                        System.out.println("Digite <enter> para ir para a próxima página");
                        leitor.nextLine();
                    }
            }
        } while (opcao != 4);

        leitor.close();
    }
}
