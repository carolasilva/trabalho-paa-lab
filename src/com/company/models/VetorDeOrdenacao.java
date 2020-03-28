package com.company.models;

public class VetorDeOrdenacao implements Comparable<VetorDeOrdenacao> {
    private Aluno aluno;
    private boolean congelado;

    public VetorDeOrdenacao(Aluno aluno, boolean congelado) {
        this.aluno = aluno;
        this.congelado = congelado;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public boolean isCongelado() {
        return congelado;
    }

    public void setCongelado(boolean congelado) {
        this.congelado = congelado;
    }

    public String getNome() {
        return this.aluno.getNome();
    }

    @Override
    public int compareTo(VetorDeOrdenacao vetorDeOrdenacao) {
        if (this.aluno == null)
            return 1;
        else if (vetorDeOrdenacao.aluno == null)
            return -1;
        else {
            if ((this.isCongelado() && vetorDeOrdenacao.isCongelado()) ||
                (!this.isCongelado() && !vetorDeOrdenacao.isCongelado())) {
                if (this.getNome().compareTo(vetorDeOrdenacao.getNome()) > 0)
                    return 1;
                else if (this.getNome().compareTo(vetorDeOrdenacao.getNome()) < 0)
                    return -1;
            }
            else if (this.isCongelado())
                return 1;
            else if (vetorDeOrdenacao.isCongelado())
                return -1;
            else
                return 0;
        }
        return 0;
    }
}
