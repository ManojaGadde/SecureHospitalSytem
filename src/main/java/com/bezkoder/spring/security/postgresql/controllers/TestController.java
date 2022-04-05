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
import com.bezkoder.spring.security.postgresql.payload.response.FetchAllDoctorsResponse;
import com.bezkoder.spring.security.postgresql.payload.response.FetchUserDetailsResponse;
import com.bezkoder.spring.security.postgresql.payload.response.MessageResponse;
import com.bezkoder.spring.security.postgresql.payload.response.PatientAppointmentViewResponse;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class TestController {
  @Autowired
    private JdbcTemplate jdbcTemplate;
	@GetMapping("/all")
	public String allAccess() {
		return "Public Content.";
	}
	
	@GetMapping("/appointments")
	public Object fetchDoctorAppointments() { 
        Connection c = null;
        Statement stmt = null;
        int doctorID = -1;
        String name = "";
        Date date = null;
        Time time = null;
        List<FetchAllDoctorAppointmentsResponse> out = new ArrayList<FetchAllDoctorAppointmentsResponse>();
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://ec2-44-202-162-44.compute-1.amazonaws.com:5432/postgres","backend", "CSE545_SS_backend");
            System.out.println("Successfully Connected.");  
            stmt = (Statement) c.createStatement();

            String sql = "SELECT \"doctorID\", name, \"date\", \"time\" FROM (SELECT \"doctorID\", \"date\", \"time\" FROM public.availability EXCEPT (SELECT \"doctorID\", \"date\", \"time\" FROM public.appointment WHERE status != 'completed' AND status != 'approved')) as availability, public.user WHERE \"doctorID\" = \"userID\"";

            ResultSet rs = stmt.executeQuery(sql);
            System.out.println(rs);
            while ( rs.next() ) {
                doctorID = rs.getInt("doctorID");
                date  = rs.getDate("date");
                name = rs.getString("name");
                time = rs.getTime("time");
                out.add(new FetchAllDoctorAppointmentsResponse(doctorID, date, name, time));
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

@GetMapping("/appointment/update")
public Object updateAppointments(@RequestBody Map<String, Object> payload) {
    Connection c = null;
    Statement stmt = null;
    int patientID = (int)payload.get("patientID");
    int doctorID = (int)payload.get("doctorID");
    int approver = (int)payload.get("approver");
    String date = (String)payload.get("date");
    String time = (String)payload.get("time");
    String status = (String)payload.get("status");
    String amount = (String)payload.get("amount");
    String parsedString = time.split("-")[0];

    try {
        Class.forName("org.postgresql.Driver");
        c = DriverManager.getConnection("jdbc:postgresql://ec2-44-202-162-44.compute-1.amazonaws.com:5432/postgres","backend", "CSE545_SS_backend");
        System.out.println("Successfully Connected.");  
        stmt = (Statement) c.createStatement();
        String sql = "";
        if(amount == "0")
            sql = "UPDATE public.appointment SET  approver=" + approver + ", status='" + status + "', amount='" + amount + "' WHERE \"patientID\"=" + patientID + " AND \"doctorID\"=" + doctorID + " AND date='" + java.sql.Date.valueOf(date) + "' AND \"time\"='" + java.sql.Time.valueOf(parsedString) + "';";        
        else
            sql = "UPDATE public.appointment SET  approver=" + approver + ", status=" + status + " WHERE \"patientID\"=" + patientID + " AND \"doctorID\"=" + doctorID + " AND date='" + java.sql.Date.valueOf(date) + "' AND \"time\"='" + java.sql.Time.valueOf(parsedString) + "';";

        ResultSet rs = stmt.executeQuery(sql);
        rs.close();
        stmt.close();
        c.close();
    } catch ( Exception e ) {
    System.err.println( e.getClass().getName()+": "+ e.getMessage() );
      }
    return "Update Successful";
}	


@GetMapping("/fetchAllAppointments")
    //@PreAuthorize("hasRole('HOSPITALSTAFF')")
	public Object fetchAllAppointments() {
        Connection c = null;
        Statement stmt = null;
        int patientID = -1;
        int doctorID = -1;
        Date date = null;
        Time time = null;
        int approver = -1;
        String status = null;
        int amount = -1;
        List<FetchAllDoctorsResponse> out = new ArrayList<FetchAllDoctorsResponse>();

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://ec2-44-202-162-44.compute-1.amazonaws.com:5432/postgres","backend", "CSE545_SS_backend");
            System.out.println("Successfully Connected.");  
            stmt = (Statement) c.createStatement();

            String sql = "SELECT * FROM public.appointment";
            ResultSet rs = stmt.executeQuery(sql);
            while ( rs.next() ) {
                
                patientID = rs.getInt("patientID");
                doctorID = rs.getInt("doctorID");
                date  = rs.getDate("date");
                time = rs.getTime("time");
                approver = rs.getInt("approver");
                status = rs.getString("status");
                amount = rs.getInt("amount");
                out.add(new FetchAllDoctorsResponse(patientID, doctorID, time, date, approver, status, amount));
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
    value = "/book/appointment", 
    method = RequestMethod.POST)
    //@PreAuthorize("hasRole('PATIENT')")
	public ResponseEntity<?> bookAppointment(@RequestBody Map<String, Object> payload) {
        try{
        int patientID = (int)payload.get("patientID");
        int doctorID = (int)payload.get("doctorID");
        String time = (String)payload.get("time");
        String date = (String)payload.get("date");

        String parsedString = time.split("-")[0];
        String sql = "INSERT INTO public.appointment(\"patientID\", \"doctorID\", \"time\", date) VALUES (" + patientID + "," + doctorID + ", '" + java.sql.Time.valueOf(parsedString) + "', '" + java.sql.Date.valueOf(date) + "');";
        System.out.println(sql);

		int rows = jdbcTemplate.update(sql);
        if (rows > 0) {
            System.out.println("A new row has been inserted.");
        }
      }
      catch ( Exception e ) {
        System.err.println( e.getClass().getName()+": "+ e.getMessage() );
          }
        return ResponseEntity.ok("Successful");
	}


@GetMapping("/fetchAllDoctors")
    //@PreAuthorize("hasRole('PATIENT')")
	public Object fetchDoctorDetails() {
        Connection c = null;
        Statement stmt = null;
        int doctorID = -1;
        String name = "";
        List<DoctorNamesResponse> out = new ArrayList<DoctorNamesResponse>();
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://ec2-44-202-162-44.compute-1.amazonaws.com:5432/postgres","backend", "CSE545_SS_backend");
            System.out.println("Successfully Connected.");  
            stmt = (Statement) c.createStatement();

            String sql = "SELECT \"doctorID\", name    FROM public.doctor as d, public.user as u    WHERE d.\"doctorID\" = u.\"userID\";";

            ResultSet rs = stmt.executeQuery(sql);
            System.out.println(rs);
            while ( rs.next() ) {
                doctorID = rs.getInt("doctorID");
                name = rs.getString("name");
                out.add(new DoctorNamesResponse(doctorID, name));
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
	//@PreAuthorize("hasRole('ADMIN')")
	public String adminAccess() {
		return "Admin Board.";
	}

	@RequestMapping(
		value = "/transaction/create",
		method = RequestMethod.POST)
    //@PreAuthorize("hasRole('PATIENT')")
	public Object createTransaction(@RequestBody Map<String, Object> payload) {
        Connection c = null;
        Statement stmt = null;
        System.out.println("Before");
        int payer = 137;//(int)payload.get("patientID");
        int transactionAmount = 100;//(int)payload.get("transactionAmount");
        String status = "completed";//(String)payload.get("status");
        String date = "2022-04-04";//(String)payload.get("date");
        System.out.println("After");

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://ec2-44-202-162-44.compute-1.amazonaws.com:5432/postgres","backend", "CSE545_SS_backend");
            System.out.println("Successfully Connected.");  
            stmt = (Statement) c.createStatement();

            String sql = "INSERT INTO public.transaction(\"transactionAmount\", payer, status, date) VALUES (" + transactionAmount + ", " + payer + ", '" + status + "', '" + java.sql.Date.valueOf(date) + "');";
            System.out.println(sql);

            ResultSet rs = stmt.executeQuery(sql);
            System.out.println(rs);
            
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
        System.err.println( e.getClass().getName()+": "+ e.getMessage() );
          }
        return "Row Inserted";
          //return ResponseEntity.ok(new PatientDiagnosisResponse(doctorID, date, diagnosis, age, gender, address, phoneNumber, creditCard));
	}

	@RequestMapping(
		value = "/transaction/update",
		method = RequestMethod.POST)
    //@PreAuthorize("hasRole('PATIENT')")
	public Object updateTransaction(@RequestBody Map<String, Object> payload) {
        Connection c = null;
        Statement stmt = null;
        int transactionID = (int)payload.get("transactionID");
        String status = (String)payload.get("status");
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://ec2-44-202-162-44.compute-1.amazonaws.com:5432/postgres","backend", "CSE545_SS_backend");
            System.out.println("Successfully Connected.");  
            stmt = (Statement) c.createStatement();

            String sql =  "UPDATE public.transaction	SET status='" + status + "' WHERE \"transactionID\"=" + transactionID + ";";

            ResultSet rs = stmt.executeQuery(sql);
            System.out.println(rs);
            
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
        System.err.println( e.getClass().getName()+": "+ e.getMessage() );
          }
        return "Updated Succesfully";
          //return ResponseEntity.ok(new PatientDiagnosisResponse(doctorID, date, diagnosis, age, gender, address, phoneNumber, creditCard));
	}

  @GetMapping("/admin/users")
  //@PreAuthorize("hasRole('ADMIN')")
	public Object getAllUsers() {
        Connection c = null;
        Statement stmt = null;
        int userID = -1;
        String name = "";
        String email = "";
        String accountType = "";
        List<FetchUserDetailsResponse> out = new ArrayList<FetchUserDetailsResponse>();
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://ec2-44-202-162-44.compute-1.amazonaws.com:5432/postgres","backend", "CSE545_SS_backend");
            System.out.println("Successfully Connected.");  
            stmt = (Statement) c.createStatement();

            String sql = "SELECT \"userID\", name, email, \"accountType\"	FROM public.\"user\"";

            ResultSet rs = stmt.executeQuery(sql);
            System.out.println(rs);
            while ( rs.next() ) {
                userID = rs.getInt("userID");
                name = rs.getString("name");
                email = rs.getString("email");
                accountType = rs.getString("accountType");
                out.add(new FetchUserDetailsResponse(userID, name, email, accountType));
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

  @GetMapping("/admin/delete/user/{id}")
  //@PreAuthorize("hasRole('ADMIN')")
	public Object deleteUser(@PathVariable long id) {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://ec2-44-202-162-44.compute-1.amazonaws.com:5432/postgres","backend", "CSE545_SS_backend");
            System.out.println("Successfully Connected.");  
            stmt = (Statement) c.createStatement();

            String sql =  "DELETE FROM public.\"user\"	WHERE \"userID\"="+id;

            ResultSet rs = stmt.executeQuery(sql);
            System.out.println(rs);
            
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
        System.err.println( e.getClass().getName()+": "+ e.getMessage() );
          }
        return "Deleted Succesfully";
          //return ResponseEntity.ok(new PatientDiagnosisResponse(doctorID, date, diagnosis, age, gender, address, phoneNumber, creditCard));
	}

  @RequestMapping(
		value = "/admin/user/update",
		method = RequestMethod.POST)
  //@PreAuthorize("hasRole('ADMIN')")
  public Object updateUser(@RequestBody Map<String, Object> payload) {
    Connection c = null;
    Statement stmt = null;
    String name = (String)payload.get("name");
    String email = (String)payload.get("email");
    String accountType = (String)payload.get("accountType");
    int userID = (int)payload.get("userID");

    try {
        Class.forName("org.postgresql.Driver");
        c = DriverManager.getConnection("jdbc:postgresql://ec2-44-202-162-44.compute-1.amazonaws.com:5432/postgres","backend", "CSE545_SS_backend");
        System.out.println("Successfully Connected.");  
        stmt = (Statement) c.createStatement();

        String sql =  "UPDATE public.\"user\" SET name='" + name + "', email='" + email + "', \"accountType\"='" + accountType + "'	WHERE \"userID\"=" + userID + ";";

        ResultSet rs = stmt.executeQuery(sql);
        System.out.println(rs);
        
        rs.close();
        stmt.close();
        c.close();
    } catch ( Exception e ) {
    System.err.println( e.getClass().getName()+": "+ e.getMessage() );
      }
    return "Updated Succesfully";
      //return ResponseEntity.ok(new PatientDiagnosisResponse(doctorID, date, diagnosis, age, gender, address, phoneNumber, creditCard));
}

}
