package com.bezkoder.spring.security.postgresql.controllers;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bezkoder.spring.security.postgresql.payload.response.PatientDiagnosisResponse;
import com.bezkoder.spring.security.postgresql.payload.response.PatientPrescriptionResponse;
import com.bezkoder.spring.security.postgresql.payload.response.PatientProfileResponse;
import com.bezkoder.spring.security.postgresql.payload.response.PatientReportResponse;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Time;
import java.text.SimpleDateFormat;

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
import org.springframework.web.bind.annotation.PathVariable;

import com.bezkoder.spring.security.postgresql.payload.response.MessageResponse;
import com.bezkoder.spring.security.postgresql.payload.response.PatientAppointmentViewResponse;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/patient")
public class PatientController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

	//@GetMapping("/profile/{id}")
    @RequestMapping(
    value = "/profile", 
    method = RequestMethod.POST)
    @PreAuthorize("hasRole('PATIENT')")
	public ResponseEntity<?> getPatientProfile(@RequestBody Map<String, Object> payload) {
        Connection c = null;
        Statement stmt = null;
        int age = -1;
        int id = Integer.parseInt((String)payload.get("Id"));
        String name = "", gender = "", address = "", phoneNumber = "", creditCard = "";
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://ec2-44-202-162-44.compute-1.amazonaws.com:5432/postgres","backend", "CSE545_SS_backend");
            System.out.println("Successfully Connected.");  
            stmt = (Statement) c.createStatement();

            String sql = "SELECT name, age, gender, address, \"phoneNumber\", \"creditCard\" FROM public.user as u, public.patient as p where u.\"userID\" = p.\"patientID\" and u.\"userID\"="+id+"";

            ResultSet rs = stmt.executeQuery(sql);
            while ( rs.next() ) {
                age = rs.getInt("age");
                name = rs.getString("name");
                gender  = rs.getString("gender");
                address  = rs.getString("address");
                phoneNumber  = rs.getString("phoneNumber");
                creditCard  = rs.getString("creditCard");
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
        System.err.println( e.getClass().getName()+": "+ e.getMessage() );
          }
        return ResponseEntity.ok(new PatientProfileResponse(name, age, gender, address, phoneNumber, creditCard));
	}

    //@GetMapping("/diagnosis/{id}")
    @RequestMapping(
    value = "/diagnosis", 
    method = RequestMethod.POST)
    @PreAuthorize("hasRole('PATIENT')")
	public ResponseEntity<?> getPatientDiagnosis(@RequestBody Map<String, Object> payload) {
        int id = Integer.parseInt((String)payload.get("Id"));
        Connection c = null;
        Statement stmt = null;
        int doctorID = -1;
        List<String> diagnosis = new ArrayList<String>();
        Date date = null;
        int age = -1;
        String gender = "", address = "", phoneNumber = "", creditCard = "";
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://ec2-44-202-162-44.compute-1.amazonaws.com:5432/postgres","backend", "CSE545_SS_backend");
            System.out.println("Successfully Connected.");  
            stmt = (Statement) c.createStatement();

            String sql = "SELECT * FROM public.diagnosis as d, public.patient as p where d.\"patientID\" = p.\"patientID\" and p.\"patientID\"="+id;

            ResultSet rs = stmt.executeQuery(sql);
            while ( rs.next() ) {
                doctorID = rs.getInt("doctorID");
                date  = rs.getDate("date");
                diagnosis.add(rs.getString("diagnosis"));
                age = rs.getInt("age");
                gender = rs.getString("gender");
                address = rs.getString("address");
                creditCard = rs.getString("creditCard");
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
        System.err.println( e.getClass().getName()+": "+ e.getMessage() );
          }
        return ResponseEntity.ok(new PatientDiagnosisResponse(doctorID, date, diagnosis, age, gender, address, phoneNumber, creditCard));
	}

    //@GetMapping("/prescription/{id}")
    @RequestMapping(
    value = "/prescription", 
    method = RequestMethod.POST)
    @PreAuthorize("hasRole('PATIENT')")
	public ResponseEntity<?> getPatientPrescription(@RequestBody Map<String, Object> payload) {
        int id = Integer.parseInt((String)payload.get("Id"));
        Connection c = null;
        Statement stmt = null;
        int doctorID = -1;
        List<Integer> prescriptionID = new ArrayList<Integer>();
        List<String> prescription = new ArrayList<String>();
        Date date = null;
        int age = -1;
        String gender = null;
        String address = null;
        String phoneNumber = null;
        String creditCard = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://ec2-44-202-162-44.compute-1.amazonaws.com:5432/postgres","backend", "CSE545_SS_backend");
            System.out.println("Successfully Connected.");  
            stmt = (Statement) c.createStatement();

            String sql = "SELECT * FROM public.prescription as d, public.patient as p where d.\"patientID\" = p.\"patientID\" and p.\"patientID\"="+id;
            ResultSet rs = stmt.executeQuery(sql);
            while ( rs.next() ) {
                doctorID = rs.getInt("doctorID");
                date  = rs.getDate("date");
                prescriptionID.add(rs.getInt("prescriptionID"));
                prescription.add(rs.getString("prescription" ));
                age = rs.getInt("age");
                gender = rs.getString("gender");
                address = rs.getString("address");
                phoneNumber = rs.getString("phoneNumber");
                creditCard = rs.getString("creditCard");
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
        System.err.println( e.getClass().getName()+": "+ e.getMessage() );
          }
        return ResponseEntity.ok(new PatientPrescriptionResponse(doctorID, date, prescriptionID, prescription, age, gender, address, phoneNumber, creditCard));
	}
    //@GetMapping("/report/{id}")
    @RequestMapping(
    value = "/report", 
    method = RequestMethod.POST)
    @PreAuthorize("hasRole('PATIENT')")
	public ResponseEntity<?> getPatientReport(@RequestBody Map<String, Object> payload) {
        int id = Integer.parseInt((String)payload.get("Id"));
        Connection c = null;
        Statement stmt = null;
        String testName = null;
        String record = null;
        int inputter = -1;
        String status = null;
        Date dateRecommended = null;
        int recommender = -1;
        Date dateFilled = null;
        int age = -1;
        String gender = null;
        String address = null;
        String phoneNumber = null;
        String creditCard = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://ec2-44-202-162-44.compute-1.amazonaws.com:5432/postgres","backend", "CSE545_SS_backend");
            System.out.println("Successfully Connected.");  
            stmt = (Statement) c.createStatement();

            String sql = "SELECT * FROM public.\"labTest\" as l, public.patient as p where l.\"patientID\" = p.\"patientID\" and l.\"patientID\"=" + id;
            ResultSet rs = stmt.executeQuery(sql);
            while ( rs.next() ) {
                testName = rs.getString("testName");
                record = rs.getString("record");
                inputter = rs.getInt("inputter");
                status = rs.getString("status");
                dateRecommended  = rs.getDate("dateRecommended");
                recommender = rs.getInt("recommender");
                dateFilled = rs.getDate("dateFilled");
                age = rs.getInt("age");
                gender = rs.getString("gender");
                address = rs.getString("address");
                phoneNumber = rs.getString("phoneNumber");
                creditCard = rs.getString("creditCard");
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
        System.err.println( e.getClass().getName()+": "+ e.getMessage() );
          }
        return ResponseEntity.ok(new PatientReportResponse(testName, record, inputter, status, dateRecommended, recommender, dateFilled, age, gender, address, phoneNumber, creditCard));
	}
	
    @GetMapping("/details/id")
	@PreAuthorize("hasRole('PATIENT')")
	public String userAccess1() {
		return "patient Content.";
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

    // View Patient Appoitments
    //@GetMapping("/appointment/{id}")
    @RequestMapping(
    value = "/appointment", 
    method = RequestMethod.POST)
    @PreAuthorize("hasRole('PATIENT')")
	public ResponseEntity<?> getPatientAppoitmentView(@RequestBody Map<String, Object> payload) {
        int id = Integer.parseInt((String)payload.get("Id"));
        Connection c = null;
        Statement stmt = null;
        int doctorID = -1, approver = -1;
        Date date = null;
        Time time = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://ec2-44-202-162-44.compute-1.amazonaws.com:5432/postgres","backend", "CSE545_SS_backend");
            System.out.println("Successfully Connected.");  
            stmt = (Statement) c.createStatement();

            String sql = "SELECT * FROM public.appointment WHERE \"patientID\"="+id+";";

            ResultSet rs = stmt.executeQuery(sql);
            while ( rs.next() ) {
                doctorID = rs.getInt("doctorID");
                time = rs.getTime("time");
                date  = rs.getDate("date");
                approver = rs.getInt("approver");
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
        System.err.println( e.getClass().getName()+": "+ e.getMessage() );
          }
        return ResponseEntity.ok(new PatientAppointmentViewResponse(doctorID, time, date, approver));
	}

    // Delete Patient Appointment
    //@GetMapping("/cancel/appointment/{patientID}/{doctorID}/{date}/{time}")
    @RequestMapping(
    value = "/cancel/appointment", 
    method = RequestMethod.POST)
    @PreAuthorize("hasRole('PATIENT')")
	public ResponseEntity<?> getPatientAppoitmentCancel(@RequestBody Map<String, Object> payload) {
        
        int patientID = Integer.parseInt((String)payload.get("patientID"));
        int doctorID = Integer.parseInt((String)payload.get("doctorID"));
        String time = (String)payload.get("time");
        String date = (String)payload.get("date");

        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://ec2-44-202-162-44.compute-1.amazonaws.com:5432/postgres","backend", "CSE545_SS_backend");
            System.out.println("Successfully Connected.");  
            stmt = (Statement) c.createStatement();
            
            String parsedString = time.split("-")[0];
            String sql = "DELETE FROM public.appointment WHERE \"patientID\"="+patientID+" AND \"doctorID\"="+doctorID+" AND date='"+ java.sql.Date.valueOf(date) +"' AND time='"+ java.sql.Time.valueOf(parsedString) +"';";

            ResultSet rs = stmt.executeQuery(sql);
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
        System.err.println( e.getClass().getName()+": "+ e.getMessage() );
          }
        return ResponseEntity.ok(new MessageResponse("Successfully Deleted"));
	}

    // Delete Patient Appointment
    //@GetMapping("/book/appointment/{patientID}/{doctorID}/{time}/{date}")
    @RequestMapping(
    value = "/book/appointment", 
    method = RequestMethod.POST)
    @PreAuthorize("hasRole('PATIENT')")
	public String bookAppointment(@RequestBody Map<String, Object> payload) {
        
        int patientID = Integer.parseInt((String)payload.get("patientID"));
        int doctorID = Integer.parseInt((String)payload.get("doctorID"));
        String time = (String)payload.get("time");
        String date = (String)payload.get("date");

        String parsedString = time.split("-")[0];
        String sql = "INSERT INTO public.appointment(\"patientID\", \"doctorID\", \"time\", date) VALUES (\'" + patientID + "\',\'" + doctorID + "\', \'" + java.sql.Time.valueOf(parsedString) + "\', \'" + java.sql.Date.valueOf(date) + "\');";

		int rows = jdbcTemplate.update(sql);
        if (rows > 0) {
            System.out.println("A new row has been inserted.");
        }
        return "Appointment Booked Successfully";
	}

}
