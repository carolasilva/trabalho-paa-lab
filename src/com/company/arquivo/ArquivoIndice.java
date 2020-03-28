package com.company.arquivo;

import com.company.models.LinhaDoIndex;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

class ArquivoIndice {
    private ObjectOutputStream outputFile;

    private List<LinhaDoIndex> indice = new ArrayList<>();

    ArquivoIndice(String nomeArquivo) {
        File file = new File(nomeArquivo);
        boolean append = file.exists();
        try {
            FileOutputStream fos = new FileOutputStream(file, append);
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

    //Retorna a posição do aluno no arquivo de acordo com a linha especificada no índice
    int procurarPorMatricula(Long matricula) {
        for (LinhaDoIndex linha : indice) {
            if (linha.getMatricula() == matricula)
                return (int) linha.getPosicao();
        }

        return -1;
    }

}
