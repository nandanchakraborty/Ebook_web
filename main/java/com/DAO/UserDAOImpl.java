package com.DAO;

import com.entity.Feedback;
import com.entity.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAOImpl implements UserDAO {

    public Connection conn;

    
    public UserDAOImpl(Connection conn) {
        this.conn = conn;
    }

    public boolean userRegister(User us) {
        try {
            String sql = "INSERT INTO user(name, email, phno, password, verified, verification_token) VALUES(?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, us.getName());
            ps.setString(2, us.getEmail());
            ps.setString(3, us.getPhno());
            ps.setString(4, us.getPassword());
            ps.setBoolean(5, us.isVerified());
            ps.setString(6, us.getVerificationToken());
            
            int i = ps.executeUpdate();
            return i > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

   
    public User login(String email, String password) {
        User us = null;

        try {
            String sql = "select * from user where email=? and password=?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                us = new User();
                us.setId(rs.getInt(1));
                us.setName(rs.getString(2));
                us.setEmail(rs.getString(3));
                us.setPhno(rs.getString(4));
                us.setPassword(rs.getString(5));
                us.setAddress(rs.getString(6));
                us.setLandmark(rs.getString(7));
                us.setCity(rs.getString(8));
                us.setState(rs.getString(9));
                us.setPincode(rs.getString(10));

            }
        } catch (Exception e) {
            e.printStackTrace();

        }

        return us;

    }

    public boolean checkPassword(int id, String ps) {

        boolean f = false;
        try {

            String sql = "select * from user where id=? and password=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            pst.setString(2, ps);

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                f = true;

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return f;
    }

    public boolean updateProfile(User us) {

        boolean f = false;
        try {
            String sql = "update user set name=?,email=?,phno=? where id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, us.getName());
            ps.setString(2, us.getEmail());
            ps.setString(3, us.getPhno());
            ps.setInt(4, us.getId());
            int i = ps.executeUpdate();
            if (i == 1) {
                f = true;

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;

    }

    public boolean checkUser(String em) {
        boolean exists = false;

        try {

            String sql = "select * from user where email=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, em);

            ResultSet rs = ps.executeQuery();
            exists = rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return exists;
    }

	@Override
	public boolean checkPasswordAuthen(String email, String phno) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveFeedback(Feedback f) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public User getUserById(int uid) {
		// TODO Auto-generated method stub
		return null;
	}
	public boolean verifyUser(String token) {
	    try {
	        // First check if token exists and user is not already verified
	        String checkSql = "SELECT id FROM user WHERE verification_token = ? AND verified = false";
	        PreparedStatement checkPs = conn.prepareStatement(checkSql);
	        checkPs.setString(1, token);
	        ResultSet rs = checkPs.executeQuery();
	        
	        if (rs.next()) {
	            // Token is valid and user needs verification
	            String updateSql = "UPDATE user SET verified = true, verification_token = NULL WHERE verification_token = ?";
	            PreparedStatement updatePs = conn.prepareStatement(updateSql);
	            updatePs.setString(1, token);
	            int updated = updatePs.executeUpdate();
	            return updated > 0;
	        }
	        return false;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	

}