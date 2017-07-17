package com.example.demo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.example.bean.Port;
import com.example.bean.Requests;
import com.example.bean.Users;
@Service("DemoDaoImpl")
@Component
public class DemoDaoImpl implements DemoDao {
	
    @Autowired
    private JdbcTemplate jdbcTemplate;
	
	final String SQL_AUTH = "SELECT * FROM USERS where NAME = ? and PASSWORD = ? ";
	final String SQL_MYREQUEST = "SELECT req.ID, req.PORT_ID, req.STATUS, u.NAME, u.EMAIL FROM REQUESTS req, USERS u where OPERATOR = ? and req.PORTOWNER = u.ID and req.DELETE_FLAG = '0'";
	final String SQL_REQUESTTOME = "SELECT req.ID, req.PORT_ID, req.STATUS, u.NAME, u.EMAIL FROM REQUESTS req, USERS u where PORTOWNER = ? and req.OPERATOR = u.ID and req.DELETE_FLAG = '0'";
	final String SQL_UPDATEREQSTATUS = "UPDATE REQUESTS set STATUS = ? where ID = ? ";
	final String SQL_DELETEREQ = "UPDATE REQUESTS set DELETE_FLAG = '1' where ID = ? ";
	final String SQL_GETOWNER = "SELECT u.NAME, u.ID, u.EMAIL from USERS u, PORTS p where p.OWNER_ID = u.ID and p.PORT = ?";
	final String SQL_NEWREQ = "INSERT into REQUESTS (PORT_ID, OPERATOR, PORTOWNER, STATUS, DELETE_FLAG, UPDATE_TIME) values (?, ?, ?, 'Pending', '0', CURRENT_TIMESTAMP)";
	final String SQL_GETOWNEREMAIL = "SELECT u.* from USERS u, REQUESTS req where req.ID = ? and req.OPERATOR = u.ID";
	final String SQL_GETPORTS = "SELECT * from PORTS where ID = ?";
	
	@Override
	public Users AuthenticateUser(String username, String password) {

		return jdbcTemplate.execute(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				return conn.prepareStatement(SQL_AUTH);
			}
		}, new PreparedStatementCallback<Users>() {
			@Override
			public Users doInPreparedStatement(PreparedStatement pstmt) throws SQLException, DataAccessException {
				pstmt.setObject(1, username);
				pstmt.setObject(2, password);
				pstmt.execute();
				ResultSet rs = pstmt.getResultSet();
				if (rs.next()) {
					Users user = new Users();
					user.setEmail(rs.getString("EMAIL"));
					user.setID(rs.getInt("ID"));
					return user;
				}
				return null;
			}
		});
	}
	
	@Override
	public Users getOwnerEmail(int reqID) {

		return jdbcTemplate.execute(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				return conn.prepareStatement(SQL_GETOWNEREMAIL);
			}
		}, new PreparedStatementCallback<Users>() {
			@Override
			public Users doInPreparedStatement(PreparedStatement pstmt) throws SQLException, DataAccessException {
				pstmt.setObject(1, reqID);
				pstmt.execute();
				ResultSet rs = pstmt.getResultSet();
				if (rs.next()) {
					Users user = new Users();
					user.setEmail(rs.getString("EMAIL"));
//					user.setID(rs.getInt("ID"));
					return user;
				}
				return null;
			}
		});
	}
	
	@Override
	public Port getPorts(int reqID) {

		return jdbcTemplate.execute(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				return conn.prepareStatement(SQL_GETPORTS);
			}
		}, new PreparedStatementCallback<Port>() {
			@Override
			public Port doInPreparedStatement(PreparedStatement pstmt) throws SQLException, DataAccessException {
				pstmt.setObject(1, reqID);
				pstmt.execute();
				ResultSet rs = pstmt.getResultSet();
				if (rs.next()) {
					Port port = new Port();
					port.setPort(rs.getString("PORT"));
//					user.setID(rs.getInt("ID"));
					return port;
				}
				return null;
			}
		});
	}

	@Override
	public List<Requests> getMyRequests(int userId) {
		// TODO Auto-generated method stub

		return jdbcTemplate.execute(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				return conn.prepareStatement(SQL_MYREQUEST);
			}
		}, new PreparedStatementCallback<List<Requests>>() {
			@Override
			public List<Requests> doInPreparedStatement(PreparedStatement pstmt) throws SQLException, DataAccessException {
				pstmt.setObject(1, userId);
				pstmt.execute();
				ResultSet rs = pstmt.getResultSet();
				List<Requests> requestList = new ArrayList<Requests>();
				while (rs.next()) {
					Requests req = new Requests();
					req.setPort(rs.getString("PORT_ID"));
					req.setStatus(rs.getString("STATUS"));
					req.setPortOwner(rs.getString("NAME"));
					req.setID(rs.getInt("ID"));
					requestList.add(req);
				}
				return requestList;
			}
		});
	}

	@Override
	public List<Requests> getRequestsToMe(int userId) {
		// TODO Auto-generated method stub
		return jdbcTemplate.execute(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				return conn.prepareStatement(SQL_REQUESTTOME);
			}
		}, new PreparedStatementCallback<List<Requests>>() {
			@Override
			public List<Requests> doInPreparedStatement(PreparedStatement pstmt) throws SQLException, DataAccessException {
				pstmt.setObject(1, userId);
				pstmt.execute();
				ResultSet rs = pstmt.getResultSet();
				List<Requests> requestList = new ArrayList<Requests>();
				while (rs.next()) {
					Requests req = new Requests();
					req.setPort(rs.getString("PORT_ID"));
					req.setStatus(rs.getString("STATUS"));
					req.setOperator(rs.getString("NAME"));					
					req.setID(rs.getInt("ID"));
					requestList.add(req);
				}
				return requestList;
			}
		});
	}

	@Override
	public void updateRequestStatus(int requestID, String status) {
		// TODO Auto-generated method stub
		jdbcTemplate.execute(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				return conn.prepareStatement(SQL_UPDATEREQSTATUS);
			}
		}, new PreparedStatementCallback<Integer>() {
			@Override
			public Integer doInPreparedStatement(PreparedStatement pstmt) throws SQLException, DataAccessException {
				pstmt.setObject(1, status);
				pstmt.setObject(2, requestID);
				return pstmt.executeUpdate();
			}
		});
	}

	@Override
	public void deleteRequest(int requestID) {
		// TODO Auto-generated method stub
		jdbcTemplate.execute(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				return conn.prepareStatement(SQL_DELETEREQ);
			}
		}, new PreparedStatementCallback<Integer>() {
			@Override
			public Integer doInPreparedStatement(PreparedStatement pstmt) throws SQLException, DataAccessException {
				pstmt.setObject(1, requestID);
				return pstmt.executeUpdate();
			}
		});
	}

	@Override
	public Users getOwnerByPort(String port) {
		// TODO Auto-generated method stub
		return jdbcTemplate.execute(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				return conn.prepareStatement(SQL_GETOWNER);
			}
		}, new PreparedStatementCallback<Users>() {
			@Override
			public Users doInPreparedStatement(PreparedStatement pstmt) throws SQLException, DataAccessException {
				pstmt.setObject(1, port);
				pstmt.execute();
				ResultSet rs = pstmt.getResultSet();
				Users user = new Users();
				while (rs.next()) {
					user.setID(rs.getInt("ID"));
					user.setName(rs.getString("NAME"));
					user.setEmail(rs.getString("EMAIL"));
					return user;
				}
				user.setID(0);
				user.setName("");
				return user;
			}
		});
	}

	@Override
	public void newRequest(int userid, int ownerid, String port) {
		
		// TODO Auto-generated method stub
		jdbcTemplate.execute(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				return conn.prepareStatement(SQL_NEWREQ);
			}
		}, new PreparedStatementCallback<Integer>() {
			@Override
			public Integer doInPreparedStatement(PreparedStatement pstmt) throws SQLException, DataAccessException {
				pstmt.setObject(1, port);
				pstmt.setObject(2, userid);
				pstmt.setObject(3, ownerid);

				return pstmt.executeUpdate();
			}
		});
	}

}
