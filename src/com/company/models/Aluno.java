package com.company.models;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;

public class Aluno implements Serializable, Comparable<Aluno> {
    private static final long serialVersionUID = 1L;
    private String nome;
    private long matricula;
    private double nota;
    private static final int NAMESIZE = 20;
    public static final int DATASIZE = 56; //40 bytes (String 20 caracteres) + 8 bytes (long) + 8 bytes double

    public Aluno(String nome, long matricula, double nota) {
        StringBuilder finalName = new StringBuilder(nome);
        finalName.setLength(NAMESIZE);
        this.nome = finalName.toString();
        this.matricula = matricula;
        this.nota = nota;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public long getMatricula() {
        return matricula;
    }

    public void setMatricula(long matricula) {
        this.matricula = matricula;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    @Override
    public String toString() {
        return "Aluno [nome=" + nome + ", matricula=" + matricula + ", nota=" + nota + "]";
    }

    //Salva o aluno com a quantidade de bytes necessária no arquivo de nome especificado
    public void saveData(RandomAccessFile arq) throws IOException {
        StringBuilder finalName = new StringBuilder(this.nome);
        finalName.setLength(NAMESIZE);
        arq.writeLong(this.matricula);
        arq.writeChars(finalName.toString());
        arq.writeDouble(this.getNota());
    }

    public static Aluno readData(RandomAccessFile arq) throws IOException{
        Long matricula = arq.readLong();

        char name[] = new char[NAMESIZE];
        for (int i=0; i<NAMESIZE; i++) {
            name[i] = arq.readChar();
        }
        String nome = new String(name);
        nome = nome.trim();

        double nota = arq.readDouble();
        Aluno aluno = new Aluno(nome, matricula, nota);
        return aluno;
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
        Aluno other = (Aluno) obj;
        if (matricula != other.matricula)
            return false;
        return true;
    }


    @Override
    public int compareTo(Aluno aluno) {
        if (aluno == null)
            return -1;
        else if (this == null)
            return 1;
        else
            return this.getNome().compareTo(aluno.getNome());
    }
}
