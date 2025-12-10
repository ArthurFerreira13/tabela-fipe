package principal;

import java.util.Scanner;

public class Principal {

    private Scanner scanner = new Scanner(System.in);
    public void exibeMenu(){
        var opcao = """
                #########OPÇÕES#########
                 - Carro
                 - Moto
                 - Caminhão
                 - Sair
                #########Escolha uma opção###############
                
                """;
        System.out.println(opcao);

    }
}
