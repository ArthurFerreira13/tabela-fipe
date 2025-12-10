package service;

public interface IConversorDados {

     <T>  T obterDados(String json, Class<T> destino);
}

