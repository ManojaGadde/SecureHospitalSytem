package com.bezkoder.spring.security.postgresql.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bezkoder.spring.security.postgresql.models.ERole;
import com.bezkoder.spring.security.postgresql.models.Role;
import com.bezkoder.spring.security.postgresql.models.User;
import com.bezkoder.spring.security.postgresql.payload.request.LoginRequest;
import com.bezkoder.spring.security.postgresql.payload.request.SignupRequest;
import com.bezkoder.spring.security.postgresql.payload.response.JwtResponse;
import com.bezkoder.spring.security.postgresql.payload.response.MessageResponse;
import com.bezkoder.spring.security.postgresql.repository.RoleRepository;
import com.bezkoder.spring.security.postgresql.repository.UserRepository;
import com.bezkoder.spring.security.postgresql.security.jwt.JwtUtils;
import com.bezkoder.spring.security.postgresql.security.services.UserDetailsImpl;
import org.springframework.jdbc.core.JdbcTemplate;

@EnableAutoConfiguration
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
    private JdbcTemplate jdbcTemplate;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());
		
		String[] details = GetUserDetails(userDetails.getEmail());

		return ResponseEntity.ok(new JwtResponse(jwt, (long) Integer.parseInt(details[0]),
												 userDetails.getUsername(), 
												 details[1], 
												 userDetails.getEmail(), 
												 roles));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username is already taken!"));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}

		// Create new user's account
		String password = encoder.encode(signUpRequest.getPassword());
		User user = new User(signUpRequest.getUsername(), 
							 signUpRequest.getEmail(),
							 password
							 );

		Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();
		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);
					break;
				case "patient":
					Role patientRole = roleRepository.findByName(ERole.ROLE_PATIENT)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(patientRole);
					break;
				case "doctor":
				Role doctorRole = roleRepository.findByName(ERole.ROLE_DOCTOR)
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
				roles.add(doctorRole);
					break;
				case "hospital_staff":
				Role hospitalStaffRole = roleRepository.findByName(ERole.ROLE_HOSPITALSTAFF)
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
				roles.add(hospitalStaffRole);
					break;
				case "lab_staff":
				Role labStaffRole = roleRepository.findByName(ERole.ROLE_LABSTAFF)
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
				roles.add(labStaffRole);
					break;
				case "insurance_staff":
				Role insuranceStaffRole = roleRepository.findByName(ERole.ROLE_INSURANCESTAFF)
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
				roles.add(insuranceStaffRole);
					break;
					

				case "mod":
					Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(modRole);

					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);

		//String sql = "INSERT INTO public.\"user\"(\"userID\", name, email, password, \"accountType\", \"startDate\", \"expiryDate\")" + 
			//"VALUES (51, 'abhishek', 'shashankrddy@gmail.com', '123', 'admin', '2021-03-18', '2022-03-18')";
		
		// Insert Into UserTable
		InsertIntoUserTable(strRoles, signUpRequest, password);
		if(strRoles.contains("patient"))
			InsertIntoPatientTable(signUpRequest);

		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

	public void InsertIntoPatientTable(SignupRequest signUpRequest)
	{
		String sql = "INSERT INTO public.\"patient\"(\"patientID\", age, gender, address, \"phoneNumber\", \"creditCard\")" + 
			String.format("VALUES('%d', '%d', '%s', '%s', '%s', '%s')", (long)Integer.parseInt(GetUserDetails(signUpRequest.getEmail())[0]), 
			Integer.parseInt(signUpRequest.getAge()), signUpRequest.getGender(), signUpRequest.getAddress(), signUpRequest.getPhoneNumber(), 
			signUpRequest.getCreditCard());

		int rows = jdbcTemplate.update(sql);
        if (rows > 0) {
            System.out.println("A new row has been inserted.");
        }
	}

	public void InsertIntoUserTable(Set<String> strRoles, SignupRequest signUpRequest, String password)
	{
		LocalDate dateObj = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String date = dateObj.format(formatter);
		
        String arr[] = new String[strRoles.size()];
        arr = strRoles.toArray(arr);

		String sql = "INSERT INTO public.\"user\"(name, email, password, \"accountType\", \"startDate\")" + 
			String.format("VALUES('%s', '%s', '%s', '%s', '%s')", signUpRequest.getName(), signUpRequest.getEmail(), password, 
			arr[0], date);

		int rows = jdbcTemplate.update(sql);
        if (rows > 0) {
            System.out.println("A new row has been inserted.");
        }
	}

	public String[] GetUserDetails(String email)
	{
		String[] result = new String[2];
		Connection c = null;
        Statement stmt = null;
		int patientID = -1;
		String name = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://ec2-44-202-162-44.compute-1.amazonaws.com:5432/postgres","backend", "CSE545_SS_backend");
            System.out.println("Successfully Connected.");  
            stmt = (Statement) c.createStatement();
			String sql = String.format("SELECT \"userID\", name FROM public.\"user\" WHERE email='%s'", email);
            ResultSet rs = stmt.executeQuery(sql);
            while ( rs.next() ) {
                patientID = rs.getInt("userID");
				name = rs.getString("name");
			}
		}catch ( Exception e ) {
			System.err.println( e.getClass().getName()+": "+ e.getMessage() );
			  }
		result[0] = String.format("%s", patientID);
		result[1] = name;
		return result;
	}
}
