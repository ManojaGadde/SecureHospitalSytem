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
import java.sql.ResultSetMetaData;
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

import com.bezkoder.spring.security.postgresql.payload.response.DoctorNamesResponse;
import com.bezkoder.spring.security.postgresql.payload.response.FetchAllDoctorAppointmentsResponse;
import com.bezkoder.spring.security.postgresql.payload.response.FetchAllTransactionsResponse;
import com.bezkoder.spring.security.postgresql.payload.response.FetchBillsResponse;
import com.bezkoder.spring.security.postgresql.payload.response.MessageResponse;
import com.bezkoder.spring.security.postgresql.payload.response.PatientAppointmentViewResponse;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/patient")
public class PatientController {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @GetMapping("/prescription/{id}")
    //@PreAuthorize("hasRole('PATIENT')")
	public Object getPatientPrescription(@PathVariable long id) {
        Connection c = null;
        Statement stmt = null;
        int doctorID = -1;
        int prescriptionID;
        String prescription = "";
        Date date = null;
        int age = -1;
        String gender = null;
        String address = null;
        String phoneNumber = null;
        String creditCard = null;
        int patientID = -1;
        List<PatientPrescriptionResponse> out = new ArrayList<PatientPrescriptionResponse>();

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://ec2-44-202-162-44.compute-1.amazonaws.com:5432/postgres","backend", "CSE545_SS_backend");
            System.out.println("Successfully Connected.");  
            stmt = (Statement) c.createStatement();

            String sql = "SELECT * FROM public.prescription as d, public.patient as p where d.\"patientID\" = p.\"patientID\" and p.\"patientID\"="+id;
            ResultSet rs = stmt.executeQuery(sql);
            while ( rs.next() ) {
                
                patientID = rs.getInt("patientID");
                doctorID = rs.getInt("doctorID");
                date  = rs.getDate("date");
                prescriptionID = rs.getInt("prescriptionID");
                prescription = rs.getString("prescription" );
                age = rs.getInt("age");
                gender = rs.getString("gender");
                address = rs.getString("address");
                phoneNumber = rs.getString("phoneNumber");
                creditCard = rs.getString("creditCard");
                out.add(new PatientPrescriptionResponse(patientID, doctorID, date, prescriptionID, prescription, age, gender, address, phoneNumber, creditCard));
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
        System.err.println( e.getClass().getName()+": "+ e.getMessage() );
          }
        return out;
	}
    @GetMapping("/report/{id}")
    //@PreAuthorize("hasRole('PATIENT')")
	public Object getPatientReport(@PathVariable long id) {
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
        int patientID = -1;

        String gender = null;
        String address = null;
        String phoneNumber = null;
        String creditCard = null;
        List<PatientReportResponse> out = new ArrayList<PatientReportResponse>();
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://ec2-44-202-162-44.compute-1.amazonaws.com:5432/postgres","backend", "CSE545_SS_backend");
            System.out.println("Successfully Connected.");  
            stmt = (Statement) c.createStatement();

            String sql = "SELECT * FROM public.\"labTest\" as l, public.patient as p where l.\"patientID\" = p.\"patientID\" and l.\"patientID\"=" + id;
            ResultSet rs = stmt.executeQuery(sql);
            while ( rs.next() ) {
                patientID = rs.getInt("patientID");
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
                out.add(new PatientReportResponse(patientID, testName, record, inputter, status, dateRecommended, recommender, dateFilled, age, gender, address, phoneNumber, creditCard));
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
        System.err.println( e.getClass().getName()+": "+ e.getMessage() );
          }
        return out;
	}
	
    @GetMapping("/details/id")
	//@PreAuthorize("hasRole('PATIENT')")
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
    @GetMapping("/appointment/{id}")
    //@PreAuthorize("hasRole('PATIENT')")
	public Object getPatientAppoitmentView(@PathVariable long id) {
        Connection c = null;
        Statement stmt = null;
        int doctorID = -1, approver = -1;
        Date date = null;
        Time time = null;
        int patientID = -1;
        String status = "";
        int amount = -1;
        List<PatientAppointmentViewResponse> response = new ArrayList<PatientAppointmentViewResponse>();

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://ec2-44-202-162-44.compute-1.amazonaws.com:5432/postgres","backend", "CSE545_SS_backend");
            System.out.println("Successfully Connected.");  
            stmt = (Statement) c.createStatement();

            String sql = "SELECT * FROM public.appointment WHERE \"patientID\"="+id+";";
            ResultSet rs = stmt.executeQuery(sql);
            while ( rs.next() ) {
                patientID = rs.getInt("patientID");
                doctorID = rs.getInt("doctorID");
                time = rs.getTime("time");
                date  = rs.getDate("date");
                approver = rs.getInt("approver");
                status = rs.getString("status");
                amount = rs.getInt("amount");
                response.add(new PatientAppointmentViewResponse(patientID, doctorID, time, date, approver, status, amount));
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
        System.err.println( e.getClass().getName()+": "+ e.getMessage() );
          }
        return response;
	}

    // Delete Patient Appointment
    //@GetMapping("/cancel/appointment/{patientID}/{doctorID}/{date}/{time}")
    @RequestMapping(
    value = "/cancel/appointment", 
    method = RequestMethod.POST)
    //@PreAuthorize("hasRole('PATIENT')")
	public ResponseEntity<?> getPatientAppoitmentCancel(@RequestBody Map<String, Object> payload) {
        
        int patientID = (int)(payload.get("patientID"));
        int doctorID = (int)(payload.get("doctorID"));
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
    
    @GetMapping("/profile/{id}")
    //@PreAuthorize("hasRole('PATIENT')")
	public ResponseEntity<?> getPatientProfile(@PathVariable long id) {
        Connection c = null;
        Statement stmt = null;
        int age = -1;
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

    @GetMapping("/diagnosis/{id}")
    //@PreAuthorize("hasRole('PATIENT')")
	public Object getPatientDiagnosis(@PathVariable long id) {
        Connection c = null;
        Statement stmt = null;
        int doctorID = -1;
        int patientID = -1;
        String diagnosis = "";
        Date date = null;
        int age = -1;
        String gender = "", address = "", phoneNumber = "", creditCard = "";
        List<PatientDiagnosisResponse> out = new ArrayList<PatientDiagnosisResponse>();
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://ec2-44-202-162-44.compute-1.amazonaws.com:5432/postgres","backend", "CSE545_SS_backend");
            System.out.println("Successfully Connected.");  
            stmt = (Statement) c.createStatement();

            String sql = "SELECT * FROM public.diagnosis as d, public.patient as p where d.\"patientID\" = p.\"patientID\" and p.\"patientID\"="+id;

            ResultSet rs = stmt.executeQuery(sql);
            System.out.println(rs);
            while ( rs.next() ) {
                patientID = rs.getInt("patientID");
                doctorID = rs.getInt("doctorID");
                date  = rs.getDate("date");
                diagnosis = rs.getString("diagnosis");
                age = rs.getInt("age");
                gender = rs.getString("gender");
                address = rs.getString("address");
                creditCard = rs.getString("creditCard");
                out.add(new PatientDiagnosisResponse(patientID, doctorID, date, diagnosis, age, gender, address, phoneNumber, creditCard));
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
        System.err.println( e.getClass().getName()+": "+ e.getMessage() );
          }
        return out;
          //return ResponseEntity.ok(new PatientDiagnosisResponse(doctorID, date, diagnosis, age, gender, address, phoneNumber, creditCard));
	}

    @RequestMapping(
    value = "/profile/update", 
    method = RequestMethod.POST)
    //@PreAuthorize("hasRole('PATIENT')")
	public String updatePatientProfile(@RequestBody Map<String, Object> payload) {
        
        int patientID = (int)(payload.get("patientID"));
        int age = (int)(payload.get("age"));
        String gender = (String)payload.get("gender");
        String address = (String)payload.get("address");
        String phoneNumber = (String)payload.get("phoneNumber");
        String creditCard = (String)payload.get("creditCard");

        String sql = "UPDATE public.patient SET age=" + age + ", gender='" + gender + "', address='" + address + "', \"phoneNumber\"='" + phoneNumber + "', \"creditCard\"='" + creditCard + "' WHERE \"patientID\"=" + patientID + ";";

		int rows = jdbcTemplate.update(sql);
        if (rows > 0) {
            System.out.println("A new row has been inserted.");
        }
        return "Profile Updated Successfully";
	}

    @GetMapping("/transaction/{id}")
    //@PreAuthorize("hasRole('PATIENT')")
	public Object getPatientTransactionDetails(@PathVariable long id) {
        Connection c = null;
        Statement stmt = null;
        int payer = -1;
        int transactionID = -1;
        int transactionAmount = -1;
        String status = "";
        Date date = null;
        List<FetchAllTransactionsResponse> out = new ArrayList<FetchAllTransactionsResponse>();
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://ec2-44-202-162-44.compute-1.amazonaws.com:5432/postgres","backend", "CSE545_SS_backend");
            System.out.println("Successfully Connected.");  
            stmt = (Statement) c.createStatement();

            String sql = "SELECT * FROM public.transaction WHERE payer="+id;

            ResultSet rs = stmt.executeQuery(sql);
            System.out.println(rs);
            while ( rs.next() ) {
            transactionAmount = (int)rs.getInt("transactionAmount");
            transactionID = (int)rs.getInt("transactionID");
            payer = (int)rs.getInt("payer");
            date = java.sql.Date.valueOf(rs.getString("date"));
            out.add(new FetchAllTransactionsResponse(transactionAmount, transactionID, date, status, payer));
                
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
        System.err.println( e.getClass().getName()+": "+ e.getMessage() );
          }
        return out;
          //return ResponseEntity.ok(new PatientDiagnosisResponse(doctorID, date, diagnosis, age, gender, address, phoneNumber, creditCard));
	}

    @GetMapping("/bills/{id}")
    //@PreAuthorize("hasRole('PATIENT')")
    public Object fetchPatientBills(@PathVariable long id) {
        Connection c = null;
        Statement stmt = null;
        int patientID = -1;
        int transactionID = -1;
        int amount = -1;
        String service = "";
        Date date = null;

        List<FetchBillsResponse> out = new ArrayList<FetchBillsResponse>();
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://ec2-44-202-162-44.compute-1.amazonaws.com:5432/postgres","backend", "CSE545_SS_backend");
            System.out.println("Successfully Connected.");  
            stmt = (Statement) c.createStatement();

            String sql = "SELECT * FROM public.bill WHERE \"patientID\"= "+id;

            ResultSet rs = stmt.executeQuery(sql);
            while ( rs.next() ) {
            patientID = (int)rs.getInt("patientID");
            transactionID = (int)rs.getInt("transactionID");
            amount = (int)rs.getInt("amount");
            service = rs.getString("service");
            date = rs.getDate("date");
            out.add(new FetchBillsResponse(patientID, transactionID, amount, service, date));
                
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
        System.err.println( e.getClass().getName()+": "+ e.getMessage() );
          }
        return out;
    }

    @RequestMapping(
    value = "/insurance/claim/insert", 
    method = RequestMethod.POST)
    //@PreAuthorize("hasRole('PATIENT')")
	public String insuranceClaimRequest(@RequestBody Map<String, Object> payload) {
        
        int transactionID = (int)(payload.get("transactionID"));
        int claimedAmount = (int)(payload.get("claimedAmount"));
        String dateOfRequest = (String)(payload.get("dateOfRequest"));

        String sql = "INSERT INTO public.\"insuranceClaim\"(\"transactionID\", \"dateOfRequest\",\"dateOfApprove\",\"claimedAmount\",\"approvedAmount\",date) VALUES (" + transactionID + ", '" + java.sql.Date.valueOf(dateOfRequest) + "', '" + java.sql.Date.valueOf(dateOfRequest) + "', "+ claimedAmount + ",0 , '" + java.sql.Date.valueOf(dateOfRequest) +"')";

		int rows = jdbcTemplate.update(sql);
        if (rows > 0) {
            System.out.println("A new row has been inserted.");
        }
        return "Profile Updated Successfully";
	}
}
