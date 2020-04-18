package application;

import java.util.List;
import java.util.Scanner;

import dao.PessoaDaoJDBC;
import db.DB;
import model.Pessoa;

public class Teste {

	public static void main(String[] args) {
		
		PessoaDaoJDBC dao = new PessoaDaoJDBC(DB.getConnection());
		
		System.out.println("TAREFA 4.2");
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Informe o nome:");
		String nome = sc.nextLine();
		
		System.out.println("Informe o endereço:");
		String endereco = sc.nextLine();
		
		System.out.println("Informe a idade:");
		int idade = sc.nextInt();
		
		Pessoa novaPessoa = new Pessoa(nome, endereco, idade);
		dao.insert(novaPessoa);
		
		List<Pessoa> list = dao.findAll();
		
		for(Pessoa p : list) {
			System.out.println("Id: " + p.getId() + "\n" +
					"Nome: " + p.getNome() + "\n" +
					"Idade: " + p.getIdade() + "\n" +
					"Endereço: " + p.getEndereco() + "\n"
			);
		}
		
		sc.close();
		
	}
	
}