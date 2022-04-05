package com.bezkoder.spring.security.postgresql.controllers;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bezkoder.spring.security.postgresql.payload.response.LabStaffPatientDiagnosisResponse;
import com.bezkoder.spring.security.postgresql.payload.response.LabStaffReportResponse;
import com.bezkoder.spring.security.postgresql.payload.response.LabTestsResponse;
import com.bezkoder.spring.security.postgresql.payload.response.MessageResponse;

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

	// @GetMapping("/reports")
    // @PreAuthorize("hasRole('LABSTAFF')")
	// public Object getLabStaffReports(@PathVariable long id) {

    //     Connection c = null;
    //     Statement stmt = null;
    //     String testName = "";
    //     String record = "";
    //     int inputter = -1;
    //     int patientID = -1;

    //     String status = "";
    //     Date dateRecommended = null;
    //     int recommender = -1;
    //     Date dateFilled = null;
    //     List<LabStaffReportResponse> out = new ArrayList<LabStaffReportResponse>();
        
    //     try {
    //         Class.forName("org.postgresql.Driver");
    //         c = DriverManager.getConnection("jdbc:postgresql://ec2-44-202-162-44.compute-1.amazonaws.com:5432/postgres","backend", "CSE545_SS_backend");
    //         System.out.println("Successfully Connected.");  
    //         stmt = (Statement) c.createStatement();

    //         String sql = "SELECT * FROM public.\"labTest\"";

    //         ResultSet rs = stmt.executeQuery(sql);
    //         while ( rs.next() ) {
    //             patientID = rs.getInt("patientID");
    //             testName = rs.getString("testName");
    //             record = rs.getString("record");
    //             inputter = rs.getInt("inputter");
    //             status  = rs.getString("status");
    //             dateRecommended  = rs.getDate("dateRecommended");
    //             recommender  = rs.getInt("recommender");
    //             dateFilled  = rs.getDate("dateFilled");
    //             out.add(new LabStaffReportResponse(patientID, testName, record, inputter, status, dateRecommended, recommender, dateFilled));
    //         }
    //         rs.close();
    //         stmt.close();
    //         c.close();
    //     } catch ( Exception e ) {
    //     System.err.println( e.getClass().getName()+": "+ e.getMessage() );
    //       }
    //     return out;
	// }

    //@GetMapping("/report/{id}/{testName}/{record}/{inputter}/{status}/{dateRecommended}/{recommender}/{dateFilled}")
    @RequestMapping(
        value = "/report/create", 
        method = RequestMethod.POST)
    //@PreAuthorize("hasRole('LABSTAFF')")
	public ResponseEntity<?> getCreateLabStaffReports(@RequestBody Map<String, Object> payload) {
        int patientID = (int)payload.get("patientID");
        int inputter = (int)payload.get("inputter"); 
        String status = (String)payload.get("status"); 
        int recommender = (int)payload.get("recommender"); 
        String testName = (String)payload.get("testName");
        String record = (String)payload.get("record");
        String date = (String)payload.get("dateRecommended");


        Connection c = null;
        Statement stmt = null;
        
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://ec2-44-202-162-44.compute-1.amazonaws.com:5432/postgres","backend", "CSE545_SS_backend");
            System.out.println("Successfully Connected.");  
            stmt = (Statement) c.createStatement();

            String sql = "INSERT INTO public.\"labTest\"( \"patientID\", \"testName\", record, inputter, status, \"dateRecommended\", recommender) VALUES (" + patientID + ", '" + testName + "','" + record + "', " + inputter + ", '" + status + "', '" + date + "', " + recommender + ");";
            ResultSet rs = stmt.executeQuery(sql);
            
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
        System.err.println( e.getClass().getName()+": "+ e.getMessage() );
          }
        return ResponseEntity.ok(new MessageResponse("Insertion Successful"));
        
	}
    //@GetMapping("/report/update/{id}/{testName}/{record}/{inputter}/{status}/{dateRecommended}/{recommender}/{dateFilled}")
    @RequestMapping(
        value = "/report/update", 
        method = RequestMethod.POST)
    //@PreAuthorize("hasRole('LABSTAFF')")
	public ResponseEntity<?> getUpdateLabStaffReports(@RequestBody Map<String, Object> payload) {
        int patientID = (int)payload.get("patientID");
        int inputter = (int)payload.get("inputter"); 
        String status = (String)(payload.get("status")); 
        int recommender = (int)payload.get("recommender"); 
        String testName = (String)payload.get("testName");
        String record = (String)payload.get("record");
        String date = (String)payload.get("dateRecommended");
        
        Connection c = null;
        Statement stmt = null;
        
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://ec2-44-202-162-44.compute-1.amazonaws.com:5432/postgres","backend", "CSE545_SS_backend");
            System.out.println("Successfully Connected.");  
            stmt = (Statement) c.createStatement();

            String sql = "UPDATE public.\"labTest\" SET \"testName\"='" + testName + "', record='" + record + "', inputter=" + inputter + ", status='" + status + "', \"dateRecommended\"='" + date + "', recommender=" + recommender + " WHERE \"patientID\"=" + patientID + ";";           
            ResultSet rs = stmt.executeQuery(sql);
            
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
        System.err.println( e.getClass().getName()+": "+ e.getMessage() );
          }
        return ResponseEntity.ok(new MessageResponse("Update Successful"));
        
	}

    @GetMapping("/diagnosis/{id}")
    //@PreAuthorize("hasRole('LABSTAFF')")
	public Object getPatientDiagnosis(@PathVariable long id) {

        Connection c = null;
        Statement stmt = null;
        int doctorID = -1;
        int patientID = -1;

        Date date = null;
        String diagnosis = "";
        int age = -1;
        String gender = "";
        String address = "";
        String phoneNumber = "";
        String creditCard = "";
        List<LabStaffPatientDiagnosisResponse> out = new ArrayList<LabStaffPatientDiagnosisResponse>();

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://ec2-44-202-162-44.compute-1.amazonaws.com:5432/postgres","backend", "CSE545_SS_backend");
            System.out.println("Successfully Connected.");  
            stmt = (Statement) c.createStatement();

            String sql = "SELECT * FROM public.diagnosis as d, public.patient as p where d.\"patientID\" = p.\"patientID\" and p.\"patientID\"=" + id;

            ResultSet rs = stmt.executeQuery(sql);
            while ( rs.next() ) {
                patientID = rs.getInt("patientID");
                doctorID = rs.getInt("doctorID");
                date = rs.getDate("date");
                diagnosis = rs.getString("diagnosis");
                age  = rs.getInt("age");
                gender  = rs.getString("gender");
                address  = rs.getString("address");
                phoneNumber  = rs.getString("phoneNumber");
                creditCard = rs.getString("creditCard");
                out.add(new LabStaffPatientDiagnosisResponse(patientID, doctorID, date, diagnosis, age, gender, address, phoneNumber, creditCard));
;            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
        System.err.println( e.getClass().getName()+": "+ e.getMessage() );
          }
        return out;
	}

    @GetMapping("/labTests")
    //@PreAuthorize("hasRole('LABSTAFF')")
	public Object getAllLabTests() {
        Connection c = null;
        Statement stmt = null;
        String testName = "";
        String record = "";
        int inputter = -1;
        int patientID = -1;
        String status = "";
        Date dateRecommended = null;
        int recommender = -1;
        Date dateFilled = null;
        List<LabTestsResponse> out = new ArrayList<LabTestsResponse>();
        
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://ec2-44-202-162-44.compute-1.amazonaws.com:5432/postgres","backend", "CSE545_SS_backend");
            System.out.println("Successfully Connected.");  
            stmt = (Statement) c.createStatement();

            String sql = "SELECT * FROM public.\"labTest\" where status='requested'";

            ResultSet rs = stmt.executeQuery(sql);
            while ( rs.next() ) {
                patientID = rs.getInt("patientID");
                testName = rs.getString("testName");
                record = rs.getString("record");
                inputter = rs.getInt("inputter");
                status  = rs.getString("status");
                dateRecommended  = rs.getDate("dateRecommended");
                recommender  = rs.getInt("recommender");
                dateFilled  = rs.getDate("dateFilled");
                out.add(new LabTestsResponse(patientID, testName, record, inputter, status, dateRecommended, recommender, dateFilled));
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
        System.err.println( e.getClass().getName()+": "+ e.getMessage() );
          }
        return out;
	}

    //@GetMapping("/labTests/update/{id}/{testName}/{record}/{inputter}/{status}")
    @RequestMapping(
        value = "/labTest/update", 
        method = RequestMethod.POST)
    //@PreAuthorize("hasRole('LABSTAFF')")
	public ResponseEntity<?> getLabTestsRequestUpdate(@RequestBody Map<String, Object> payload) {
        int patientID = (int)payload.get("patientID");
        String status = (String)payload.get("status"); 
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
        return ResponseEntity.ok(new MessageResponse("Update Successful"));
	}

    @GetMapping("/fetchAllLabTests")
    //@PreAuthorize("hasRole('LABSTAFF')")
	public Object getLabTests() {
        Connection c = null;
        Statement stmt = null;
        String testName = "";
        String record = "";
        int inputter = -1;
        int patientID = -1;
        String status = "";
        Date dateRecommended = null;
        int recommender = -1;
        Date dateFilled = null;
        List<LabTestsResponse> out = new ArrayList<LabTestsResponse>();
        
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://ec2-44-202-162-44.compute-1.amazonaws.com:5432/postgres","backend", "CSE545_SS_backend");
            System.out.println("Successfully Connected.");  
            stmt = (Statement) c.createStatement();

            String sql = "SELECT * FROM public.\"labTest\";";

            ResultSet rs = stmt.executeQuery(sql);
            while ( rs.next() ) {
                patientID = rs.getInt("patientID");
                testName = rs.getString("testName");
                record = rs.getString("record");
                inputter = rs.getInt("inputter");
                status  = rs.getString("status");
                dateRecommended  = rs.getDate("dateRecommended");
                recommender  = rs.getInt("recommender");
                dateFilled  = rs.getDate("dateFilled");
                out.add(new LabTestsResponse(patientID, testName, record, inputter, status, dateRecommended, recommender, dateFilled));
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
        System.err.println( e.getClass().getName()+": "+ e.getMessage() );
          }
        return out;
	}

    //@GetMapping("/labTests/update/{id}/{testName}/{record}/{inputter}/{status}")
    @RequestMapping(
        value = "/labTest/report/update", 
        method = RequestMethod.POST)
    //@PreAuthorize("hasRole('LABSTAFF')")
	public ResponseEntity<?> labTestsReportUpdate(@RequestBody Map<String, Object> payload) {
        int patientID = (int)payload.get("patientID");
        int inputter = (int)payload.get("inputter");
        String status = (String)payload.get("status"); 
        String testName = (String)payload.get("testName");
        String dateRecommended = (String)payload.get("dateRecommended");
        String dateFilled = (String)payload.get("dateFilled");
        int recommender = (int)payload.get("recommender"); 
        String record = (String)payload.get("record");

        Connection c = null;
        Statement stmt = null;
        
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://ec2-44-202-162-44.compute-1.amazonaws.com:5432/postgres","backend", "CSE545_SS_backend");
            System.out.println("Successfully Connected.");  
            stmt = (Statement) c.createStatement();

            String sql = "UPDATE public.\"labTest\" SET record='" + record + "', inputter=" + inputter + ", status='" + status + "', \"dateFilled\"='" + dateFilled + "' WHERE \"patientID\"=" + patientID + " AND \"testName\"='" + testName + "' AND recommender='" + recommender + "' AND \"dateRecommended\"='" + java.sql.Date.valueOf(dateRecommended) + "';";

            ResultSet rs = stmt.executeQuery(sql);
            
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
        System.err.println( e.getClass().getName()+": "+ e.getMessage() );
          }
        return ResponseEntity.ok(new MessageResponse("Update Successful"));
	}

    //@GetMapping("/labTests/update/{id}/{testName}/{record}/{inputter}/{status}")
    @RequestMapping(
        value = "/labTest/report/delete", 
        method = RequestMethod.POST)
    //@PreAuthorize("hasRole('LABSTAFF')")
	public ResponseEntity<?> deleteTestsReportUpdate(@RequestBody Map<String, Object> payload) {
        int patientID = (int)payload.get("patientID");
        String testName = (String)payload.get("testName");
        String dateRecommended = (String)payload.get("dateRecommended");
        int recommender = (int)payload.get("recommender"); 
        Connection c = null;
        Statement stmt = null;
        
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://ec2-44-202-162-44.compute-1.amazonaws.com:5432/postgres","backend", "CSE545_SS_backend");
            System.out.println("Successfully Connected.");  
            stmt = (Statement) c.createStatement();

            String sql = "DELETE FROM public.\"labTest\"  WHERE \"patientID\"=" + patientID + " AND \"testName\"='" + testName + "' AND recommender=" + recommender + " AND \"dateRecommended\"='" + dateRecommended + "';";

            ResultSet rs = stmt.executeQuery(sql);
            
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
        System.err.println( e.getClass().getName()+": "+ e.getMessage() );
          }
        return ResponseEntity.ok(new MessageResponse("Deletion Successful"));
	}
}