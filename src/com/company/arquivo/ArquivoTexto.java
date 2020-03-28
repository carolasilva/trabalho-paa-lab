package com.company.arquivo;

import com.company.models.Aluno;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ArquivoTexto {
    File arquivoTexto;

    public ArquivoTexto(String nomeArquivo) {
        this.arquivoTexto = new File(nomeArquivo);
    }

    // Lê o arquivo texto e, aluno por aluno, adiciona no arquivo binário
    // Assim que termina, salva o índice completo
    public ArquivoBinarioAcessoAleatorio transformaArquivoTextoEmBinario(String nomeArquivoBinario) {
        Scanner leitor;
        String[] dividido;
        Aluno aluno;
        ArquivoBinarioAcessoAleatorio arquivoBinarioAcessoAleatorio = new ArquivoBinarioAcessoAleatorio(nomeArquivoBinario);

        try {
            leitor = new Scanner(arquivoTexto);
            while(leitor.hasNextLine()){
                String frase = leitor.nextLine();
                dividido = frase.split(";");
                aluno = new Aluno(dividido[1], Long.parseLong(dividido[0]), Double.parseDouble(dividido[2].replace(',', '.')));
                arquivoBinarioAcessoAleatorio.adicionarAlunoNoArquivo(aluno);
            }
            arquivoBinarioAcessoAleatorio.getArquivoIndice().salvarIndiceCompleto();
            leitor.close();
            System.out.println("Arquivo binário criado com sucesso!");
            return arquivoBinarioAcessoAleatorio;
        } catch (FileNotFoundException e) {
            System.out.println("Algo deu errado na leitura do arquivo...");
        }
        return arquivoBinarioAcessoAleatorio;
    }
}
