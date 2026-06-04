package com.example.SAPA.service;

import com.example.SAPA.DTOs.RegisterRequestDTO;
import com.example.SAPA.DTOs.Response.UserDTOResponse;
import com.example.SAPA.DTOs.UserMapper;
import com.example.SAPA.Models.Entities.DoctorEntity;
import com.example.SAPA.Models.Entities.PatientEntity;
import com.example.SAPA.Models.Entities.UserEntity;
import com.example.SAPA.Repositories.DoctorRepository;
import com.example.SAPA.Repositories.PatientRepository;
import com.example.SAPA.Repositories.UserRepository;
import com.example.SAPA.exceptions.EmptyCollectionException;
import com.example.SAPA.security.DTO.AuthResponse;
import com.example.SAPA.security.entities.CredentialEntity;
import com.example.SAPA.security.entities.RoleEntity;
import com.example.SAPA.security.enums.Role;
import com.example.SAPA.security.repositories.CredentialRepository;
import com.example.SAPA.security.repositories.RoleRepository;
import com.example.SAPA.security.service.JWTService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class UserService {
    //Atributos.
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final CredentialRepository credentialRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;

    //Constructor.
    public UserService(UserRepository userRepository, PatientRepository patientRepository, DoctorRepository doctorRepository, CredentialRepository credentialRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JWTService jwtService){
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.credentialRepository = credentialRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    //Métodos.
    @Transactional
    public AuthResponse registerUser(RegisterRequestDTO request) {

        if (request.getRole() == Role.ROLE_DOCTOR) {
            if (request.getLicenseNumber() == null || request.getLicenseNumber().isBlank()) {
                throw new IllegalArgumentException("La matrícula es obligatoria para los médicos.");
            }
        }

        String encryptedPassword = passwordEncoder.encode(request.getPassword());

        UserEntity userConnector = UserEntity.builder()
                .email(request.getEmail())
                .password(encryptedPassword)
                .status(com.example.SAPA.enums.AccountStatus.ACTIVE)
                .role(com.example.SAPA.enums.UserCategory.valueOf(request.getRole().name().replace("ROLE_", "")))
                .build();

        userConnector = userRepository.save(userConnector);

        if (request.getRole() == Role.ROLE_DOCTOR) {

            DoctorEntity doctor = DoctorEntity.builder()
                    .user(userConnector)
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .licenseNumber(request.getLicenseNumber())
                    .build();
            doctorRepository.save(doctor);

        } else if (request.getRole() == Role.ROLE_PATIENT) {

            PatientEntity patient = PatientEntity.builder()
                    .user(userConnector)
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .build();
            patientRepository.save(patient);
        }

        RoleEntity assignedRole = roleRepository.findByRole(request.getRole())
                .orElseThrow(() -> new RuntimeException("El rol no existe."));

        CredentialEntity securityCredentials = CredentialEntity.builder()
                .email(request.getEmail())
                .password(encryptedPassword)
                .active(true)
                .user(userConnector)
                .roles(Set.of(assignedRole))
                .refreshToken("")
                .build();

        String accessToken = jwtService.generateToken(securityCredentials);
        String refreshToken = jwtService.generateRefreshToken(securityCredentials);

        securityCredentials.setRefreshToken(refreshToken);
        credentialRepository.save(securityCredentials);

        return new AuthResponse(accessToken, refreshToken);
    }

    public List<UserDTOResponse> getAllUsers() throws  EmptyCollectionException{
        if(userRepository.count() == 0) throw new EmptyCollectionException("No hay usuarios registrados");
        return userRepository.findAll().stream().map(UserMapper::fromEntity).toList();
    }
}
