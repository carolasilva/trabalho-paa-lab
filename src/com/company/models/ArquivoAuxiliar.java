package com.company.models;

import java.util.Objects;

public class ArquivoAuxiliar {
    private String nome;
    private int posicao;
    private boolean bloqueado;
    private boolean livre;

    public ArquivoAuxiliar(String nome, int posicao) {
        this.nome = nome;
        this.posicao = posicao;
        this.bloqueado = false;
        this.livre = false;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getPosicao() {
        return posicao;
    }

    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }

    public boolean isBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(boolean bloqueado) {
        this.bloqueado = bloqueado;
    }

    public boolean isLivre() {
        return livre;
    }

    public void setLivre(boolean livre) {
        this.livre = livre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArquivoAuxiliar that = (ArquivoAuxiliar) o;
        return Objects.equals(getNome(), that.getNome());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNome());
    }
}
