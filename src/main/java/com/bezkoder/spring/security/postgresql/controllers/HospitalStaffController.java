package com.bezkoder.spring.security.postgresql.controllers;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.Fetch;

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
import com.bezkoder.spring.security.postgresql.payload.response.MessageResponse;
import com.bezkoder.spring.security.postgresql.payload.response.PatientAppointmentViewResponse;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/hospitalStaff")
public class HospitalStaffController {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @GetMapping("/fetchAllAppointments")
    @PreAuthorize("hasRole('HOSPITALSTAFF')")
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

            String sql = "SELECT * FROM public.appointment WHERE status='requested'";
            ResultSet rs = stmt.executeQuery(sql);
            while ( rs.next() ) {
                
                patientID = rs.getInt("patientID");
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
        value = "/patient/appointment/approve", 
        method = RequestMethod.POST)
    @PreAuthorize("hasRole('HOSPITALSTAFF')")
	public ResponseEntity<?> approveAppointment(@RequestBody Map<String, Object> payload) {
        int patientID = (int)payload.get("patientID");
        int doctorID = (int)payload.get("doctorID");
        int hospitalStaffID = (int)payload.get("hospitalStaffID");
        String date = (String)payload.get("date");
        String time = (String)payload.get("time");
        String parsedString = time.split("-")[0];
        
        Connection c = null;
        Statement stmt = null;
        
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://ec2-44-202-162-44.compute-1.amazonaws.com:5432/postgres","backend", "CSE545_SS_backend");
            System.out.println("Successfully Connected.");  
            stmt = (Statement) c.createStatement();

            String sql = "UPDATE public.appointment SET  approver=" + hospitalStaffID + ", status='approved'"  + " WHERE \"patientID\"=" + patientID + " AND \"doctorID\"=" + doctorID + " AND date='" + java.sql.Date.valueOf(date) + "' AND time='" + java.sql.Time.valueOf(parsedString) + "';";           
            ResultSet rs = stmt.executeQuery(sql);
            
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
        System.err.println( e.getClass().getName()+": "+ e.getMessage() );
          }
        return ResponseEntity.ok(new MessageResponse("Approved Successful"));       
	}

    @RequestMapping(
        value = "/patient/appointment/decline", 
        method = RequestMethod.POST)
    @PreAuthorize("hasRole('HOSPITALSTAFF')")
	public ResponseEntity<?> declineAppointment(@RequestBody Map<String, Object> payload) {
        int patientID = (int)payload.get("patientID");
        int doctorID = (int)payload.get("doctorID");
        int hospitalStaffID = (int)payload.get("hospitalStaffID");
        String date = (String)payload.get("date");
        String time = (String)payload.get("time");
        String parsedString = time.split("-")[0];
        
        Connection c = null;
        Statement stmt = null;
        
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://ec2-44-202-162-44.compute-1.amazonaws.com:5432/postgres","backend", "CSE545_SS_backend");
            System.out.println("Successfully Connected.");  
            stmt = (Statement) c.createStatement();

            String sql = "UPDATE public.appointment SET  approver=" + hospitalStaffID + ", status='denied'"  + " WHERE \"patientID\"=" + patientID + " AND \"doctorID\"=" + doctorID + " AND date='" + java.sql.Date.valueOf(date) + "' AND time='" + java.sql.Time.valueOf(parsedString) + "';";           
            ResultSet rs = stmt.executeQuery(sql);
            
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
        System.err.println( e.getClass().getName()+": "+ e.getMessage() );
          }
        return ResponseEntity.ok(new MessageResponse("Denied Successful"));       
	}

    @RequestMapping(
    value = "/record/create", 
    method = RequestMethod.POST)
    @PreAuthorize("hasRole('HOSPITALSTAFF')")
	public String recordCreate(@RequestBody Map<String, Object> payload) {
        
        int patientID = (int)payload.get("patientID");
        int inputter = (int)payload.get("inputter");
        String record = (String)payload.get("record");
        String date = (String)payload.get("date");

        String sql = "INSERT INTO public.record(\"patientID\", record, inputter,date) VALUES (" + patientID + ", '" + record + "'," + inputter + ", '" + java.sql.Date.valueOf(date) + "');";

		int rows = jdbcTemplate.update(sql);
        if (rows > 0) {
            System.out.println("A new row has been inserted.");
        }
        return "Appointment Booked Successfully";
	}

}
