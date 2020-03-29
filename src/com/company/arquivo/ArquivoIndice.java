package com.company.arquivo;

import com.company.models.LinhaDoIndex;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

class ArquivoIndice {
    private ObjectOutputStream outputFile;
    private File arquivo;

    private List<LinhaDoIndex> indice = new ArrayList<>();

    ArquivoIndice(String nomeArquivo) {
        arquivo = new File(nomeArquivo);
        boolean append = arquivo.exists();
        try {
            FileOutputStream fos = new FileOutputStream(arquivo, append);
            outputFile = new AppendableObjectOutputStream(fos, append);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void addIndice(LinhaDoIndex linha) {
        this.indice.add(linha);
    }

    // Percorre cada linha do índice salvo em memória e salva em um arquivo
    void salvarIndiceCompleto() {
        try {
            for(LinhaDoIndex linha : indice){
                this.outputFile.writeObject(linha);      //escreve (sequencialmente) no formatio binÃ¡rio
            }

        } catch (IOException e) {
            System.out.println("Problema com o arquivo de índice: " + e.getMessage());
        }
    }

    void reinicializaIndice() {
        indice = new ArrayList<>();
    }

    //Retorna a posição do aluno no arquivo de acordo com a linha especificada no índice
    int procurarPorMatricula(Long matricula) {

        LinhaDoIndex linha;

        try (FileInputStream fis = new FileInputStream(arquivo); ObjectInputStream inputFile = new ObjectInputStream(fis)) {
            while (fis.available() > 0) {
                linha = (LinhaDoIndex) inputFile.readObject();

                if (matricula == linha.getMatricula()) {
                    return (int) linha.getPosicao();
                }
            }
        } catch (Exception e) {
            System.out.println("ERRO ao ler a matricula '" + matricula + "' do disco!");
            e.printStackTrace();
        }
        return -1;
    }
}
