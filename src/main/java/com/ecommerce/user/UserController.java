package com.ecommerce.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    SendEmailService sendEmailService;

    @Autowired
    JwtUtils jwtUtils;


    @GetMapping(value = "/all")
    public ResponseEntity<List<User>> getUsers(){
        List<User> list = userRepository.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping(value="/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getMail(),
                userDetails.getPassword()
                ));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getUser(@PathVariable String id){
        Optional<User> optionalUser = userRepository.findById(id);
        return new ResponseEntity<>(optionalUser,HttpStatus.OK);
    }


    @GetMapping(value = "/forgot")
    public ResponseEntity<?> forgotPassword(@RequestParam String email, @RequestParam String code){
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isEmpty())
            return new ResponseEntity<String>("404 ! NOT FOUND",HttpStatus.NOT_FOUND);

        sendEmailService.SendEmail(email,"Please enter this code for reset your password : " +code+ " and click here : http://172.20.10.2:8080/auth/yes ." +
                "Your username is : "+optionalUser.get().getUsername()+ " in case you forgot.","RESET PASSWORD");

        return new ResponseEntity<>("Your Email has been found successfully",HttpStatus.ACCEPTED);
    }


    @PutMapping(value = "/{id}")
    public ResponseEntity<?> putUser(@PathVariable String id, @RequestBody NoPassword noPassword){
        Optional<User> userOptional = userRepository.findById(id);

        if(userOptional.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        User user1 = userOptional.get();

        user1.setUsername(noPassword.getUsername());
        user1.setEmail(noPassword.getEmail());


        Set<String> strRole = noPassword.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRole == null) {
            Role userRole = roleRepository.findByName(UserRole.USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRole.forEach(role -> {
                switch (role) {
                    case "ADMIN":
                        Role adminRole = roleRepository.findByName(UserRole.ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(UserRole.USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        userRepository.save(user1);


        return new ResponseEntity<>(user1,HttpStatus.OK);
    }


    @PutMapping(value = "/password/{email}/{pass}")
    public ResponseEntity<?> resetPassword(@PathVariable String email, @PathVariable String pass){

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isEmpty())
            return new ResponseEntity<>("404 ! NOT FOUND",HttpStatus.NOT_FOUND);

        User user = optionalUser.get();
        user.setPassword(encoder.encode(pass));

        userRepository.save(user);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    /*@PutMapping(value = "/{id}" )
    public ResponseEntity<?> putEmailAndUsername(@PathVariable String id, String username, String email){
        Optional<User>
    }*/

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
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
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));


        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id){
        userRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
