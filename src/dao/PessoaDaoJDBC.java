package dao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import db.DB;
import db.DbException;
import model.Pessoa;

public class PessoaDaoJDBC {

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
