package com.company.arquivo;

import com.company.models.Aluno;
import com.company.models.LinhaDoIndex;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ArquivoBinarioAcessoAleatorio {
    private RandomAccessFile arquivoAcessoAleatorio;
    private File arquivo;
    private ArquivoIndice arquivoIndice;
    private String nomeArquivo;

    private Long posicao = 0L;

    //Cria o arquivo binário com o nome especificado e já cria um arquivo de índice para ele
    public ArquivoBinarioAcessoAleatorio(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
        arquivo = new File(nomeArquivo);
        arquivoIndice = new ArquivoIndice("indice" + nomeArquivo + ".dat");
    }

    // Procura a última posição do arquivo e salva o novo aluno nela.
    // Cria uma linha de índice para o aluno criado, para que posteriormente o mesmo seja salvo
    void adicionarAlunoNoArquivo(Aluno aluno) {
        try {
            this.arquivoAcessoAleatorio = new RandomAccessFile(arquivo, "rw");
            this.arquivoAcessoAleatorio.seek(this.arquivo.length());
            aluno.saveData(this.arquivoAcessoAleatorio);
            arquivoIndice.addIndice(new LinhaDoIndex(aluno.getMatricula(), this.posicao));
            this.posicao++;
            this.arquivoAcessoAleatorio.close();

        } catch (IOException e) {
            System.out.println("Erro com o arquivo: " + e.getMessage());
        }

    }

    public Aluno procurarAlunoPorPosicaoNoArquivo (int posicao) {
        try {
            this.arquivoAcessoAleatorio = new RandomAccessFile(arquivo, "r");
            this.arquivoAcessoAleatorio.seek(posicao * Aluno.DATASIZE);
            Aluno aluno;

            try{
                aluno = Aluno.readData(this.arquivoAcessoAleatorio);
            }
            catch(EOFException e){  //para quando terminar os dados do arquivo
                aluno = null;
            }

            this.arquivoAcessoAleatorio.close();
            return aluno;
        } catch (IOException e) {
            System.out.println("Erro com o arquivo: " + e.getMessage());
        }
        return null;
    }

    ArquivoIndice getArquivoIndice() {
        return arquivoIndice;
    }

    // Retorna o aluno conforma a matrícula
    public Aluno procurarAlunoPorMatricula(Long matricula) {
        int posicao = getArquivoIndice().procurarPorMatricula(matricula);
        return procurarAlunoPorPosicaoNoArquivo(posicao);
    }

    void apagarArquivo() {
        this.arquivo.delete();
        File indice = new File("indice" + this.nomeArquivo + ".dat");
        indice.delete();
    }

    void renomearArquivo(String novoNome) {
        File auxiliar = new File(novoNome);
        this.arquivo.renameTo(auxiliar);
        File indice = new File("indice" + this.nomeArquivo + ".dat");
        File novoIndice = new File("indice" + novoNome + ".dat");
        indice.renameTo(novoIndice);
    }

    boolean estaVazio() {
        return this.arquivo.length() <= 56;
    }

    boolean comparaTamanho(String arquivo) {
        File auxiliar = new File(arquivo);
        return auxiliar.length() == this.arquivo.length();
    }

    public void atualizarIndice() {
        try {
            this.arquivoAcessoAleatorio = new RandomAccessFile(arquivo, "r");
            this.arquivoIndice.reinicializaIndice();
            Aluno aluno;

            try{
                for(int i=0; i<arquivo.length(); ) {
                    this.arquivoAcessoAleatorio.seek(i * Aluno.DATASIZE);
                    aluno = Aluno.readData(this.arquivoAcessoAleatorio);
                    arquivoIndice.addIndice(new LinhaDoIndex(aluno.getMatricula(), i));
                    i ++;
                }

                this.arquivoIndice.salvarIndiceCompleto();
            }
            catch(EOFException e){  //para quando terminar os dados do arquivo
            }

            this.arquivoAcessoAleatorio.close();
        } catch (IOException e) {
            System.out.println("Erro com o arquivo: " + e.getMessage());
        }
        arquivoIndice.salvarIndiceCompleto();
    }
}
