package com.company.models;

public class AlunoAuxiliar implements Comparable<AlunoAuxiliar> {
    private Aluno aluno;
    private String nomeArquivo;

    public AlunoAuxiliar(Aluno aluno, String nomeArquivo) {
        this.aluno = aluno;
        this.nomeArquivo = nomeArquivo;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    @Override
    public int compareTo(AlunoAuxiliar aluno) {
        if (aluno == null)
            return -1;
        else if (this == null)
            return 1;
        else
            return this.getAluno().getNome().compareTo(aluno.getAluno().getNome());
    }
}
