package service;

import java.util.List;

public interface IConversorDados {

     <T>  T obterDados(String json, Class<T> destino);

     <T> List<T> obterListaDados(String json, Class<T> destino);

}

