package com.bezkoder.spring.security.postgresql.controllers;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.jdbc.core.JdbcTemplate;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/patient")
public class PatientController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

	@GetMapping("/profile/{Id}")
    @PreAuthorize("hasRole('PATIENT')")
	public String getPatientProfile(@PathVariable Long id) {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://ec2-44-202-162-44.compute-1.amazonaws.com:5432/postgres","backend", "CSE545_SS_backend");
            System.out.println("Successfully Connected.");  
            stmt = (Statement) c.createStatement();
            ResultSet rs = stmt.executeQuery( "select * from public.\"patient\" ;" );
            while ( rs.next() ) {
                 int patientID = rs.getInt("patientID");
                 //String  title = rs.getString("Title");
                 //int artistid  = rs.getInt("ArtistId");
                 System.out.printf( "AlbumId = %s", patientID );
                 System.out.println();
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
        System.err.println( e.getClass().getName()+": "+ e.getMessage() );
          }
		return "Public Content.";
	}
	
	@GetMapping("/user")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public String userAccess() {
		return "User Content.";
	}

	@GetMapping("/mod")
	@PreAuthorize("hasRole('MODERATOR')")
	public String moderatorAccess() {
		return "Moderator Board.";
	}
 
	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String adminAccess() {
		return "Admin Board.";
	}
}
