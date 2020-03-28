package com.company;

import com.company.arquivo.ArquivoBinarioAcessoAleatorio;
import com.company.arquivo.ArquivoTexto;
import com.company.arquivo.OrdenacaoArquivo;
import com.company.models.Aluno;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
	    ArquivoTexto arquivo = new ArquivoTexto("LabComp3_Exerc1_dadosTeste.txt");
        ArquivoBinarioAcessoAleatorio arquivoBinario = arquivo.transformaArquivoTextoEmBinario("alunos.dat");

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
            }
        } while (opcao != 4);

        leitor.close();
    }
}
