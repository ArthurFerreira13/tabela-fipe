package principal;

import model.Dados;
import model.Modelos;
import model.Veiculo;
import service.ConsumoApi;
import service.ConversorDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    private final String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";
    private ConversorDados conversor = new ConversorDados();
    private Scanner scanner = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();

    public void exibeMenu(){
        var menu = """
                #########OPÇÕES#########
                 - Carro
                 - Moto
                 - Caminhão
                 - Sair
                #########Escolha uma opção###############
                
                """;
        System.out.println(menu);
        var opcao = scanner.nextLine();
        String endereco = "";
       if(opcao.toLowerCase().contains("carr")){
           endereco = URL_BASE + "carros/marcas";
       } else if (opcao.toLowerCase().contains("mot")) {
           endereco = URL_BASE + "motos/marcas";
       } else if (opcao.toLowerCase().contains("caminh")) {
           endereco = URL_BASE + "caminhoes/marcas";
       }

       var json = consumoApi.obterDados(endereco);
       System.out.println(json);
       var marcas = conversor.obterListaDados(json, Dados.class);
                    marcas.stream()
                            .sorted(Comparator.comparing(Dados::codigo))
                            .forEach(marca -> System.out.println(marca.codigo() + " - " + marca.nome()));

        System.out.println("Informe o codigo da consulta:");
        var codigoConsulta = scanner.nextLine();
         endereco = endereco + "/" + codigoConsulta + "/modelos";
        json = consumoApi.obterDados(endereco);
        var modeloLista = conversor.obterDados(json, Modelos.class);
        System.out.println("Modelos disponíveis:");
        modeloLista.modelos().stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.println("Digite o trecho do carro a ser buscado:");
        var nomeVeiculo = scanner.nextLine().toLowerCase().trim();
        List<Dados> modelosFiltrados = modeloLista.modelos().stream()
                .filter(modelo -> modelo.nome().toLowerCase().contains(nomeVeiculo))
                .collect(Collectors.toList());
        System.out.println("Modelos encontrados:");
        modelosFiltrados.forEach(modelo -> System.out.println(modelo.codigo() + " - " + modelo.nome()));

        System.out.println("Digite o código do modelo desejado para buscar os valores da avaliacão:");
        var codigoModelo = scanner.nextLine();
        endereco = endereco + "/" + codigoModelo + "/anos";
        json = consumoApi.obterDados(endereco);
        List<Dados> anos = conversor.obterListaDados(json, Dados.class);
        List<Veiculo> veiculos = new ArrayList<>();
        for (int i = 0; i < anos.size() ; i++) {
            var enderecosAnos = endereco + "/" + anos.get(i).codigo();
            json = consumoApi.obterDados(enderecosAnos);
            Veiculo veiculo = conversor.obterDados(json, Veiculo.class);
            veiculos.add(veiculo);
        }
        System.out.println("Valores encontrados:");
        veiculos.forEach(veiculo -> System.out.println(" Valor: " + veiculo.valor() +
                "  Marca: " + veiculo.marca() +
                "\n Modelo: " + veiculo.modelo() +
                "\n Ano Modelo: " + veiculo.ano() +
                "\n Combustivel: " + veiculo.tipoCombustivel()));

        scanner.close();
    }
}
