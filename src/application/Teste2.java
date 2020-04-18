package application;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import db.DbException;
import model.Pessoa;

public class Teste2 {

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

class DB {

    private static Connection conn = null;
	
	public static Connection getConnection() {
		if(conn == null) {
			try {
				Properties props = loadProperties();
				String url = props.getProperty("dburl");
				conn = (Connection) DriverManager.getConnection(url, props);
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
		return conn;
	}
	
	public static void closeConnection() {
		if(conn!= null) {
			try {
			conn.close();
			}catch(SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}
	
	private static Properties loadProperties() {
		try(FileInputStream fs = new FileInputStream("db.properties")){
			Properties props = new Properties();
			props.load(fs);
			return props;
		}catch(IOException e) {
			throw new DbException(e.getMessage());
		}
	}
	
	public static void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}

	public static void closePreparedStatement(PreparedStatement st) {
		if (st != null) {
			try {
				st.close();
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}
}

class PessoaDaoJDBC {

	private Connection conn;

	public PessoaDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	public void insert(Pessoa obj) {
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("INSERT INTO tb_pessoa (nome, endereco, idade) "
									 + "VALUES " + "(?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getNome());
			st.setString(2, obj.getEndereco());
			st.setDouble(3, obj.getIdade());

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			} else {
				throw new DbException("Error ao inserir uma nova pessoa");
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closePreparedStatement(st);
		}
	}
	
	public List<Pessoa> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT * FROM tb_pessoa ORDER BY id");
			
			rs = st.executeQuery();
			
			List<Pessoa> list = new ArrayList<>();
			
			while(rs.next()) {
				Pessoa obj = new Pessoa(rs.getString("nome"), rs.getString("endereco"), rs.getInt("idade"));
				obj.setId(rs.getInt("id"));
				list.add(obj);
			}
			return list;
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closePreparedStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
}