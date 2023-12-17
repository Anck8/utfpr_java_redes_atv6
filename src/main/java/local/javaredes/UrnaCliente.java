package local.testeredes;

import local.javaredes.Candidato;
import local.javaredes.Eleicao;

import java.io.NotSerializableException;
import java.io.WriteAbortedException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;

public class UrnaCliente {

    static Eleicao stub;
    private static int inputInterface = 9;
    private static String inputContinua = "s";
    static Scanner input = new Scanner(System.in);

    public UrnaCliente() throws RemoteException, NotBoundException{
        Registry servidorRegistro = LocateRegistry.getRegistry("127.0.0.1", 1099);
        stub = (Eleicao) servidorRegistro.lookup("servidorEleicao");
    }

    public static void main(String[] args) {
        try {
            new UrnaCliente();
            System.out.println("Urna Java \n---------");
            while (!inputContinua.equalsIgnoreCase("n")) {
                try {
                    AbstractMap.SimpleEntry<Integer, Integer> apuracao = apuraCandidato();
                    stub.apuraVotos(apuracao.getKey(), apuracao.getValue());
                    System.out.println("Continuar (S/N)?");
                    inputContinua = input.next().substring(0, 1);
                } catch (InputMismatchException e) {
                    System.out.println("Erro de digitação na apuração...");
                }
            }
        } catch (RemoteException | NotBoundException | NotSerializableException | WriteAbortedException e) {
            System.out.println("Erro na execução da Urna...");
            e.printStackTrace();
        }
    }

    public static AbstractMap.SimpleEntry<Integer, Integer> apuraCandidato() throws RemoteException, NotSerializableException, WriteAbortedException {
        int numeroVotos;
        int numeroCandidato;
        System.out.println("Candidatos:");
        List<Candidato> candidatos = (List<Candidato>) stub.getCandidatos();
        candidatos.forEach(c -> System.out.println(String.format("%s - %s", c.getNome(), c.getLegenda())));
        System.out.println("Entre o número do candidato: ");
        numeroCandidato = input.nextInt();
        System.out.println("Entre o número de votos: ");
        numeroVotos = input.nextInt();
        return new AbstractMap.SimpleEntry(numeroCandidato, numeroVotos);
    }
}
