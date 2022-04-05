package com.bezkoder.spring.security.postgresql.controllers;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
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
    @GetMapping("/patient/records/{id}")
    //@PreAuthorize("hasRole('DOCTOR')")
    public Object getPatientRecords(@PathVariable long id) {
        
        Connection c = null;
        Statement stmt = null;
        int recordId = -1, inputter = -1, patientID = -1;
        String record = "";
        Date date = null;
        List<DoctorPatientRecordsResponse> out = new ArrayList<DoctorPatientRecordsResponse>();

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://ec2-44-202-162-44.compute-1.amazonaws.com:5432/postgres","backend", "CSE545_SS_backend");
            System.out.println("Successfully Connected.");
            stmt = (Statement) c.createStatement();

            String sql = "SELECT * FROM public.record WHERE \"patientID\"=\'"+id+"\'";

            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                patientID = rs.getInt("patientID");
                recordId = rs.getInt("recordID");
                record = rs.getString("record");
                inputter = rs.getInt("inputter");
                date = rs.getDate("date");
                out.add(new DoctorPatientRecordsResponse(patientID, recordId, inputter, record, date));            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }
        return out;
    }

    // Update Patient Record
    //@GetMapping("/patient/records/{id}/{record}/{date}/{recordID}/{inputter}")
    @RequestMapping(
    value = "/patient/record/update", 
    method = RequestMethod.POST)
    //@PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> updatePatientRecord(@RequestBody Map<String, Object> payload) {
        int id = (int)payload.get("patientID");
        int inputter = (int)(payload.get("inputter"));
        int recordID = (int)(payload.get("recordID"));
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
    //@PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> insertPatientDiagnosis(@RequestBody Map<String, Object> payload) {
        int patientID = (int)payload.get("patientID");
        int doctorID = (int)(payload.get("doctorID")); 
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
    //@PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> getPatientDiagnosisUpdate(@RequestBody Map<String, Object> payload) {
        int patientID = (int)payload.get("patientID");
        int doctorID = (int)payload.get("doctorID"); 
        String newDate = (String)payload.get("newDate");
        String oldDate = (String)payload.get("oldDate");
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
    //@PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> getPatientDiagnosisDelete(@RequestBody Map<String, Object> payload) {
        int patientID = (int)(payload.get("patientID"));
        int doctorID = (int)payload.get("doctorID"); 
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
        return ResponseEntity.ok(new MessageResponse("Successfully Deleted"));
    }

    // Create Prescriptions
    //@GetMapping("/patient/prescription/create/{patientID}/{doctorID}/{date}/{prescriptionID}/{prescription}")
    @RequestMapping(
        value = "/patient/prescription/create", 
        method = RequestMethod.POST)
    //@PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> getPatientPrescriotionsCreate(@RequestBody Map<String, Object> payload) {
        int patientID = (int)payload.get("patientID");
        int doctorID = (int)payload.get("doctorID"); 
        int prescriptionID = (int)payload.get("prescriptionID"); 
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

    @RequestMapping(
        value = "/labtest/create", 
        method = RequestMethod.POST)
    //@PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> createLabTest(@RequestBody Map<String, Object> payload) {
        int patientID = (int)payload.get("patientID");
        String dateRecommended = (String)payload.get("dateRecommended");
        String testName = (String)payload.get("testName");
        String status = (String)payload.get("status");
        int recommender = (int)payload.get("recommender");
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://ec2-44-202-162-44.compute-1.amazonaws.com:5432/postgres","backend", "CSE545_SS_backend");
            System.out.println("Successfully Connected.");
            stmt = (Statement) c.createStatement();

            String sql = "INSERT INTO public.\"labTest\"( \"patientID\", \"testName\", status, \"dateRecommended\", recommender) VALUES (" + patientID + ", '" + testName + "' ,'" + status + "', '" + dateRecommended + "', " + recommender + ");";

            ResultSet rs = stmt.executeQuery(sql);
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }
        return ResponseEntity.ok(new MessageResponse("Successfully Inserted"));
    }
    
}
