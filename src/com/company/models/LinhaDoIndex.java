package com.company.models;

import java.io.Serializable;

public class LinhaDoIndex implements Serializable {

    private static final long serialVersionUID = 1L;
    private long matricula;
    private long posicao;

    public LinhaDoIndex(long matricula, long posicao) {
        this.matricula = matricula;
        this.posicao = posicao;
    }

    public long getMatricula() {
        return matricula;
    }

    public long getPosicao() {
        return posicao;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (matricula ^ (matricula >>> 32));
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LinhaDoIndex other = (LinhaDoIndex) obj;
        return matricula == other.matricula;
    }


}
