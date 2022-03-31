package com.bezkoder.spring.security.postgresql.controllers;

import java.sql.Statement;
import java.util.Map;

import com.bezkoder.spring.security.postgresql.payload.response.LabStaffPatientDiagnosisResponse;
import com.bezkoder.spring.security.postgresql.payload.response.LabStaffReportResponse;
import com.bezkoder.spring.security.postgresql.payload.response.LabTestsResponse;
import com.bezkoder.spring.security.postgresql.payload.response.MessageResponse;
import com.bezkoder.spring.security.postgresql.payload.response.PatientDiagnosisResponse;
import com.bezkoder.spring.security.postgresql.payload.response.PatientProfileResponse;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.jdbc.core.JdbcTemplate;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/labStaff")
public class LabStaffController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

	//@GetMapping("/report/{id}")
    @RequestMapping(
        value = "/report", 
        method = RequestMethod.POST)
    @PreAuthorize("hasRole('LABSTAFF')")
	public ResponseEntity<?> getLabStaffReports(@RequestBody Map<String, Object> payload) {
        int id = Integer.parseInt((String)payload.get("Id"));

        Connection c = null;
        Statement stmt = null;
        String testName = "";
        String record = "";
        int inputter = -1;
        String status = "";
        Date dateRecommended = null;
        int recommender = -1;
        Date dateFilled = null;
        
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://ec2-44-202-162-44.compute-1.amazonaws.com:5432/postgres","backend", "CSE545_SS_backend");
            System.out.println("Successfully Connected.");  
            stmt = (Statement) c.createStatement();

            String sql = "SELECT * FROM public.\"labTest\"";

            ResultSet rs = stmt.executeQuery(sql);
            while ( rs.next() ) {
                testName = rs.getString("testName");
                record = rs.getString("record");
                inputter = rs.getInt("inputter");
                status  = rs.getString("status");
                dateRecommended  = rs.getDate("dateRecommended");
                recommender  = rs.getInt("recommender");
                dateFilled  = rs.getDate("dateFilled");
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
        System.err.println( e.getClass().getName()+": "+ e.getMessage() );
          }
        return ResponseEntity.ok(new LabStaffReportResponse(testName, record, inputter, status, dateRecommended, recommender, dateFilled));
	}

    //@GetMapping("/report/{id}/{testName}/{record}/{inputter}/{status}/{dateRecommended}/{recommender}/{dateFilled}")
    @RequestMapping(
        value = "/report/create", 
        method = RequestMethod.POST)
    @PreAuthorize("hasRole('LABSTAFF')")
	public ResponseEntity<?> getCreateLabStaffReports(@RequestBody Map<String, Object> payload) {
        int patientID = Integer.parseInt((String)(payload.get("patientID")));
        int inputter = Integer.parseInt((String)(payload.get("inputter"))); 
        int status = Integer.parseInt((String)(payload.get("status"))); 
        int recommender = Integer.parseInt((String)(payload.get("recommender"))); 
        String testName = (String)payload.get("testName");
        String record = (String)payload.get("record");
        String date = (String)payload.get("date");


        Connection c = null;
        Statement stmt = null;
        
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://ec2-44-202-162-44.compute-1.amazonaws.com:5432/postgres","backend", "CSE545_SS_backend");
            System.out.println("Successfully Connected.");  
            stmt = (Statement) c.createStatement();

            String sql = "INSERT INTO public.\"labTest\"( \"patientID\", \"testName\", record, inputter, status, date, recommender) VALUES (" + patientID + ", " + testName + "," + record + ", " + inputter + ", " + status + ", " + date + ", " + recommender + ");";
            ResultSet rs = stmt.executeQuery(sql);
            
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
        System.err.println( e.getClass().getName()+": "+ e.getMessage() );
          }
        return ResponseEntity.ok(new MessageResponse("successful"));
        
	}
    //@GetMapping("/report/update/{id}/{testName}/{record}/{inputter}/{status}/{dateRecommended}/{recommender}/{dateFilled}")
    @RequestMapping(
        value = "/report/update", 
        method = RequestMethod.POST)
    @PreAuthorize("hasRole('LABSTAFF')")
	public ResponseEntity<?> getUpdateLabStaffReports(@RequestBody Map<String, Object> payload) {
        int patientID = Integer.parseInt((String)(payload.get("patientID")));
        int inputter = Integer.parseInt((String)(payload.get("inputter"))); 
        int status = Integer.parseInt((String)(payload.get("status"))); 
        int recommender = Integer.parseInt((String)(payload.get("recommender"))); 
        String testName = (String)payload.get("testName");
        String record = (String)payload.get("record");
        String date = (String)payload.get("date");
        
        Connection c = null;
        Statement stmt = null;
        
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://ec2-44-202-162-44.compute-1.amazonaws.com:5432/postgres","backend", "CSE545_SS_backend");
            System.out.println("Successfully Connected.");  
            stmt = (Statement) c.createStatement();

            String sql = "UPDATE public.\"labTest\" SET \"testName\"=" + testName + ", record=" + record + ", inputter=" + inputter + ", status=" + status + ", date=" + date + ", recommender=" + recommender + " WHERE \"patientID\"=" + patientID + ";";           
            ResultSet rs = stmt.executeQuery(sql);
            
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
        System.err.println( e.getClass().getName()+": "+ e.getMessage() );
          }
        return ResponseEntity.ok(new MessageResponse("successful"));
        
	}

    //@GetMapping("/diagnosis/{id}")
    @RequestMapping(
        value = "/diagnosis", 
        method = RequestMethod.POST)
    @PreAuthorize("hasRole('LABSTAFF')")
	public ResponseEntity<?> getPatientDiagnosis(@RequestBody Map<String, Object> payload) {
        int id = Integer.parseInt((String)payload.get("Id"));

        Connection c = null;
        Statement stmt = null;
        int doctorID = -1;
        Date date = null;
        String diagnosis = "";
        int age = -1;
        String gender = "";
        String address = "";
        String phoneNumber = "";
        String creditCard = "";
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://ec2-44-202-162-44.compute-1.amazonaws.com:5432/postgres","backend", "CSE545_SS_backend");
            System.out.println("Successfully Connected.");  
            stmt = (Statement) c.createStatement();

            String sql = "SELECT * FROM public.diagnosis as d, public.patient as p where d.\"patientID\" = p.\"patientID\" and p.\"patientID\"=" + id;

            ResultSet rs = stmt.executeQuery(sql);
            while ( rs.next() ) {
                doctorID = rs.getInt("doctorID");
                date = rs.getDate("date");
                diagnosis = rs.getString("diagnosis");
                age  = rs.getInt("age");
                gender  = rs.getString("gender");
                address  = rs.getString("address");
                phoneNumber  = rs.getString("phoneNumber");
                creditCard = rs.getString("creditCard");
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
        System.err.println( e.getClass().getName()+": "+ e.getMessage() );
          }
        return ResponseEntity.ok(new LabStaffPatientDiagnosisResponse(doctorID, date, diagnosis, age, gender, address, phoneNumber, creditCard));
	}

    //@GetMapping("/labTests/{id}")
    @RequestMapping(
        value = "/labTests", 
        method = RequestMethod.POST)
    @PreAuthorize("hasRole('LABSTAFF')")
	public ResponseEntity<?> getLabTests(@RequestBody Map<String, Object> payload) {
        Connection c = null;
        Statement stmt = null;
        String testName = "";
        String record = "";
        int inputter = -1;
        String status = "";
        Date dateRecommended = null;
        int recommender = -1;
        Date dateFilled = null;
        
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://ec2-44-202-162-44.compute-1.amazonaws.com:5432/postgres","backend", "CSE545_SS_backend");
            System.out.println("Successfully Connected.");  
            stmt = (Statement) c.createStatement();

            String sql = "SELECT * FROM public.\"labTest\" where status='requested'";

            ResultSet rs = stmt.executeQuery(sql);
            while ( rs.next() ) {
                testName = rs.getString("testName");
                record = rs.getString("record");
                inputter = rs.getInt("inputter");
                status  = rs.getString("status");
                dateRecommended  = rs.getDate("dateRecommended");
                recommender  = rs.getInt("recommender");
                dateFilled  = rs.getDate("dateFilled");
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
        System.err.println( e.getClass().getName()+": "+ e.getMessage() );
          }
        return ResponseEntity.ok(new LabTestsResponse(testName, record, inputter, status, dateRecommended, recommender, dateFilled));
	}

    //@GetMapping("/labTests/update/{id}/{testName}/{record}/{inputter}/{status}")
    @RequestMapping(
        value = "/labTests/update", 
        method = RequestMethod.POST)
    @PreAuthorize("hasRole('LABSTAFF')")
	public ResponseEntity<?> getLabTestsRequestUpdate(@RequestBody Map<String, Object> payload) {
        int patientID = Integer.parseInt((String)(payload.get("patientID")));
        int status = Integer.parseInt((String)(payload.get("status"))); 
        String testName = (String)payload.get("testName");

        Connection c = null;
        Statement stmt = null;
        
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://ec2-44-202-162-44.compute-1.amazonaws.com:5432/postgres","backend", "CSE545_SS_backend");
            System.out.println("Successfully Connected.");  
            stmt = (Statement) c.createStatement();

            String sql = "UPDATE public.\"labTest\" SET status ='" + status + "' WHERE \"patientID\"=" + patientID + " AND \"testName\"='" + testName + "';";

            ResultSet rs = stmt.executeQuery(sql);
            
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
        System.err.println( e.getClass().getName()+": "+ e.getMessage() );
          }
        return ResponseEntity.ok(new MessageResponse("successful"));
	}
}