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

    // User Registration (with OTP - no token)
    public boolean userRegister(User us) {
        try {
            String sql = "INSERT INTO user(name, email, phno, password, verified) VALUES(?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, us.getName());
            ps.setString(2, us.getEmail());
            ps.setString(3, us.getPhno());
            ps.setString(4, us.getPassword());
            ps.setBoolean(5, us.isVerified());

            int i = ps.executeUpdate();
            return i > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // User Login
    public User login(String email, String password) {
        User us = null;

        try {
            String sql = "SELECT * FROM user WHERE email=? AND password=?";
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
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return us;
    }

    // Check if user exists
    public boolean checkUser(String email) {
        boolean exists = false;

        try {
            String sql = "SELECT * FROM user WHERE email=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();
            exists = rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return exists;
    }

    // Check user password for reset
    public boolean checkPasswordAuthen(String email, String phno) {
        boolean exists = false;

        try {
            String sql = "SELECT * FROM user WHERE email=? AND phno=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, phno);

            ResultSet rs = ps.executeQuery();
            exists = rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return exists;
    }

    // Update user profile
    public boolean updateProfile(User us) {
        boolean f = false;
        try {
            String sql = "UPDATE user SET name=?, email=?, phno=? WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, us.getName());
            ps.setString(2, us.getEmail());
            ps.setString(3, us.getPhno());
            ps.setInt(4, us.getId());

            int i = ps.executeUpdate();
            if (i == 1) {
                f = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return f;
    }

    // Check password validity
    public boolean checkPassword(int id, String password) {
        boolean f = false;
        try {
            String sql = "SELECT * FROM user WHERE id=? AND password=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            pst.setString(2, password);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                f = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return f;
    }

    // No longer needed unless you plan to use token-based verification
    // Removed updateUser() and verifyUser()

    public boolean saveFeedback(Feedback f) {
        boolean fa = false;
        try {
            String sql = "insert into feedback(bookId,userId,comment) values(?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, f.getBookId());
            ps.setInt(2, f.getUserId());
            ps.setString(3, f.getComment());

            int i = ps.executeUpdate();
            if (i == 1) {
                fa = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fa;
    }

    // Get user by ID
    public User getUserById(int uid) {
        User us = null;
        try {
            String sql = "select * from user where id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, uid);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                us = new User();
                us.setId(rs.getInt(1));
                us.setName(rs.getString(2));
                us.setEmail(rs.getString(3));
                us.setPhno(rs.getString(4));
                us.setPassword(rs.getString(5));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return us;
    }

    public boolean forgotPassword(String email, String phno, String password) {
        boolean f = false;
        try {
            String sql = "update user set password=? where email=? and phno=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, password);
            ps.setString(2, email);
            ps.setString(3, phno);

            int i = ps.executeUpdate();
            if (i == 1) {
                f = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }

	@Override
	public boolean updateUser(User us) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean verifyUser(String token) {
		// TODO Auto-generated method stub
		return false;
	}
}
