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
import com.bezkoder.spring.security.postgresql.payload.response.MessageResponse;
import com.bezkoder.spring.security.postgresql.payload.response.PatientAppointmentViewResponse;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/publicAPI")
public class PublicAPIControllers {
    @Autowired
    private JdbcTemplate jdbcTemplate;
//     @GetMapping("/doctorAppointments")
// 	public Object fetchDoctorAppointments() { 
//         Connection c = null;
//         Statement stmt = null;
//         int doctorID = -1;
//         String name = "";
//         Date date = null;
//         Time time = null;
//         List<FetchAllDoctorAppointmentsResponse> out = new ArrayList<FetchAllDoctorAppointmentsResponse>();
//         try {
//             Class.forName("org.postgresql.Driver");
//             c = DriverManager.getConnection("jdbc:postgresql://ec2-44-202-162-44.compute-1.amazonaws.com:5432/postgres","backend", "CSE545_SS_backend");
//             System.out.println("Successfully Connected.");  
//             stmt = (Statement) c.createStatement();

//             String sql = "SELECT \"doctorID\", name, \"date\", \"time\" FROM (SELECT \"doctorID\", \"date\", \"time\" FROM public.availability EXCEPT (SELECT \"doctorID\", \"date\", \"time\" FROM public.appointment WHERE status != 'completed' AND status != 'approved')) as availability, public.user WHERE \"doctorID\" = \"userID\"";

//             ResultSet rs = stmt.executeQuery(sql);
//             System.out.println(rs);
//             while ( rs.next() ) {
//                 doctorID = rs.getInt("doctorID");
//                 date  = rs.getDate("date");
//                 name = rs.getString("name");
//                 time = rs.getTime("time");
//                 out.add(new FetchAllDoctorAppointmentsResponse(doctorID, date, name, time));
//             }
//             rs.close();
//             stmt.close();
//             c.close();
//         } catch ( Exception e ) {
//         System.err.println( e.getClass().getName()+": "+ e.getMessage() );
//           }
//         return out;
//           //return ResponseEntity.ok(new PatientDiagnosisResponse(doctorID, date, diagnosis, age, gender, address, phoneNumber, creditCard));
// }

// @GetMapping("/appointment/update")
// public Object updateAppointments(@RequestBody Map<String, Object> payload) {
//     Connection c = null;
//     Statement stmt = null;
//     int patientID = (int)payload.get("patientID");
//     int doctorID = (int)payload.get("doctorID");
//     int approver = (int)payload.get("approver");
//     String date = (String)payload.get("date");
//     String time = (String)payload.get("time");
//     String status = (String)payload.get("status");
//     String amount = (String)payload.get("amount");
//     String parsedString = time.split("-")[0];


//     try {
//         Class.forName("org.postgresql.Driver");
//         c = DriverManager.getConnection("jdbc:postgresql://ec2-44-202-162-44.compute-1.amazonaws.com:5432/postgres","backend", "CSE545_SS_backend");
//         System.out.println("Successfully Connected.");  
//         stmt = (Statement) c.createStatement();
//         String sql = "";
//         if(amount == "0")
//             sql = "UPDATE public.appointment SET  approver=" + approver + ", status='" + status + "', amount='" + amount + "' WHERE \"patientID\"=" + patientID + " AND \"doctorID\"=" + doctorID + " AND date='" + java.sql.Date.valueOf(date) + "' AND \"time\"='" + java.sql.Time.valueOf(parsedString) + "';";        
//         else
//             sql = "UPDATE public.appointment SET  approver=" + approver + ", status=" + status + " WHERE \"patientID\"=" + patientID + " AND \"doctorID\"=" + doctorID + " AND date='" + java.sql.Date.valueOf(date) + "' AND \"time\"='" + java.sql.Time.valueOf(parsedString) + "';";

//         ResultSet rs = stmt.executeQuery(sql);
//         rs.close();
//         stmt.close();
//         c.close();
//     } catch ( Exception e ) {
//     System.err.println( e.getClass().getName()+": "+ e.getMessage() );
//       }
//     return "Update Successful";
// }

// @GetMapping("/fetchAllDoctors")
//     //@PreAuthorize("hasRole('PATIENT')")
// 	public Object fetchDoctorDetails() {
//         Connection c = null;
//         Statement stmt = null;
//         int doctorID = -1;
//         String name = "";
//         List<DoctorNamesResponse> out = new ArrayList<DoctorNamesResponse>();
//         try {
//             Class.forName("org.postgresql.Driver");
//             c = DriverManager.getConnection("jdbc:postgresql://ec2-44-202-162-44.compute-1.amazonaws.com:5432/postgres","backend", "CSE545_SS_backend");
//             System.out.println("Successfully Connected.");  
//             stmt = (Statement) c.createStatement();

//             String sql = "SELECT \"doctorID\", name    FROM public.doctor as d, public.user as u    WHERE d.\"doctorID\" = u.\"userID\";";

//             ResultSet rs = stmt.executeQuery(sql);
//             System.out.println(rs);
//             while ( rs.next() ) {
//                 doctorID = rs.getInt("doctorID");
//                 name = rs.getString("name");
//                 out.add(new DoctorNamesResponse(doctorID, name));
//             }
//             rs.close();
//             stmt.close();
//             c.close();
//         } catch ( Exception e ) {
//         System.err.println( e.getClass().getName()+": "+ e.getMessage() );
//           }
//         return out;
//           //return ResponseEntity.ok(new PatientDiagnosisResponse(doctorID, date, diagnosis, age, gender, address, phoneNumber, creditCard));
// 	}


}
