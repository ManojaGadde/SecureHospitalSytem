package com.bezkoder.spring.security.postgresql.controllers;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bezkoder.spring.security.postgresql.payload.response.DoctorPatientRecordsResponse;
import com.bezkoder.spring.security.postgresql.payload.response.MessageResponse;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    //View Patient Record
    //@GetMapping("/patient/records/{id}")
    @RequestMapping(
    value = "/patient/records", 
    method = RequestMethod.POST)
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> getPatientRecords(@RequestBody Map<String, Object> payload) {
        int id = Integer.parseInt((String)payload.get("Id"));
        
        Connection c = null;
        Statement stmt = null;
        int recordId = -1, inputter = -1;
        String record = "";
        Date date = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://ec2-44-202-162-44.compute-1.amazonaws.com:5432/postgres","backend", "CSE545_SS_backend");
            System.out.println("Successfully Connected.");
            stmt = (Statement) c.createStatement();

            String sql = "SELECT * FROM public.record WHERE \"patientID\"=\'"+id+"\'";

            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                recordId = rs.getInt("recordId");
                record = rs.getString("record");
                inputter = rs.getInt("inputter");
                date = rs.getDate("date");
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }
        return ResponseEntity.ok(new DoctorPatientRecordsResponse(recordId, inputter, record, date));
    }

    // Update Patient Record
    //@GetMapping("/patient/records/{id}/{record}/{date}/{recordID}/{inputter}")
    @RequestMapping(
    value = "/patient/record/update", 
    method = RequestMethod.POST)
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> updatePatientRecord(@RequestBody Map<String, Object> payload) {
        int id = Integer.parseInt((String)(payload.get("patientID")));
        int inputter = Integer.parseInt((String)(payload.get("inputter")));
        int recordID = Integer.parseInt((String)(payload.get("recordID")));
        String date = (String)payload.get("date");
        String record = (String)payload.get("record");

        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://ec2-44-202-162-44.compute-1.amazonaws.com:5432/postgres","backend", "CSE545_SS_backend");
            System.out.println("Successfully Connected.");
            stmt = (Statement) c.createStatement();

            String sql = "UPDATE public.record SET record='"+record+"', date='"+date+"' WHERE \"patientID\"="+id+" AND \"inputter\"="+inputter+" AND \"recordID\"="+recordID+";";

            ResultSet rs = stmt.executeQuery(sql);
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }
        return ResponseEntity.ok(new MessageResponse("Successfully Updated"));
    }

    // Create Patient Diagnosis
    //@GetMapping("/patient/diagnosis/create/{patientID}/{doctorID}/{date}/{diagnosis}")
    @RequestMapping(
        value = "/patient/diagnosis/create", 
        method = RequestMethod.POST)
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> insertPatientDiagnosis(@RequestBody Map<String, Object> payload) {
        int patientID = Integer.parseInt((String)(payload.get("patientID")));
        int doctorID = Integer.parseInt((String)(payload.get("doctorID"))); 
        String date = (String)payload.get("date");
        String diagnosis = (String)payload.get("diagnosis");

        
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://ec2-44-202-162-44.compute-1.amazonaws.com:5432/postgres","backend", "CSE545_SS_backend");
            System.out.println("Successfully Connected.");
            stmt = (Statement) c.createStatement();

            String sql = "INSERT INTO public.diagnosis(\"patientID\",\"doctorID\",date, diagnosis) VALUES ("+patientID+", "+doctorID+", '"+date+"', '"+diagnosis+"');";

            ResultSet rs = stmt.executeQuery(sql);
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }
        return ResponseEntity.ok(new MessageResponse("Successfully Created"));
    }

    // Update Patient Diagnosis
    //@GetMapping("/patient/diagnosis/update/{patientID}/{doctorID}/{newDate}/{oldDate}/{diagnosis}")
    @RequestMapping(
        value = "/patient/diagnosis/update", 
        method = RequestMethod.POST)
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> getPatientDiagnosisUpdate(@RequestBody Map<String, Object> payload) {
        int patientID = Integer.parseInt((String)(payload.get("patientID")));
        int doctorID = Integer.parseInt((String)(payload.get("doctorID"))); 
        String newDate = (String)payload.get("new_date");
        String oldDate = (String)payload.get("old_date");
        String diagnosis = (String)payload.get("diagnosis");
        
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://ec2-44-202-162-44.compute-1.amazonaws.com:5432/postgres","backend", "CSE545_SS_backend");
            System.out.println("Successfully Connected.");
            stmt = (Statement) c.createStatement();

            String sql = "UPDATE public.diagnosis SET date='"+newDate+"', diagnosis='"+diagnosis+"' WHERE \"patientID\"="+patientID+" AND \"doctorID\"="+doctorID+" AND date='"+oldDate+"';";

            ResultSet rs = stmt.executeQuery(sql);
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }
        return ResponseEntity.ok(new MessageResponse("Successfully Updated"));
    }

    // Delete Patient Diagnosis
    //@GetMapping("/patient/diagnosis/delete/{patientID}/{doctorID}/{date}/{diagnosis}")
    @RequestMapping(
        value = "/patient/diagnosis/delete", 
        method = RequestMethod.POST)
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> getPatientDiagnosisDelete(@RequestBody Map<String, Object> payload) {
        int patientID = Integer.parseInt((String)(payload.get("patientID")));
        int doctorID = Integer.parseInt((String)(payload.get("doctorID"))); 
        String date = (String)payload.get("date");
        String diagnosis = (String)payload.get("diagnosis");

        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://ec2-44-202-162-44.compute-1.amazonaws.com:5432/postgres","backend", "CSE545_SS_backend");
            System.out.println("Successfully Connected.");
            stmt = (Statement) c.createStatement();

            String sql = "DELETE  from public.diagnosis WHERE \"patientID\"="+patientID+" AND \"doctorID\"="+doctorID+" AND date='"+date+"' AND diagnosis='"+diagnosis+"';";

            ResultSet rs = stmt.executeQuery(sql);
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }
        return ResponseEntity.ok(new MessageResponse("Successfully Updated"));
    }

    // Create Prescriptions
    //@GetMapping("/patient/prescription/create/{patientID}/{doctorID}/{date}/{prescriptionID}/{prescription}")
    @RequestMapping(
        value = "/patient/prescription/create", 
        method = RequestMethod.POST)
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> getPatientPrescriotionsCreate(@RequestBody Map<String, Object> payload) {
        int patientID = Integer.parseInt((String)(payload.get("patientID")));
        int doctorID = Integer.parseInt((String)(payload.get("doctorID"))); 
        int prescriptionID = Integer.parseInt((String)(payload.get("prescriptionID"))); 
        String date = (String)payload.get("date");
        String prescription = (String)payload.get("prescription");
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://ec2-44-202-162-44.compute-1.amazonaws.com:5432/postgres","backend", "CSE545_SS_backend");
            System.out.println("Successfully Connected.");
            stmt = (Statement) c.createStatement();

            String sql = "INSERT INTO public.prescription(\"patientID\", \"doctorID\", date, \"prescriptionID\", prescription) VALUES ("+patientID+","+doctorID+",'"+date+"',"+prescriptionID+",'"+prescription+"');";

            ResultSet rs = stmt.executeQuery(sql);
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }
        return ResponseEntity.ok(new MessageResponse("Successfully Updated"));
    }
    
}
