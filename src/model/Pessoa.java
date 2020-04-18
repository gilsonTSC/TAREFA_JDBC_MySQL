package model;

public class Pessoa {

private Integer id;
	
	private String nome;
	private String endereco;
	private Integer idade;
	
	public Pessoa(String nome, String endereco, Integer idade) {
		this.nome = nome;
		this.endereco = endereco;
		this.idade = idade;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	
	public Integer getIdade() {
		return idade;
	}

	public void setIdade(Integer idade) {
		this.idade = idade;
	}

}