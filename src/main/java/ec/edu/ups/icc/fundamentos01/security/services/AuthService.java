package ec.edu.ups.icc.fundamentos01.security.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ec.edu.ups.icc.fundamentos01.security.dtos.AuthResponseDto;
import ec.edu.ups.icc.fundamentos01.security.dtos.LoginRequestDto;
import ec.edu.ups.icc.fundamentos01.security.dtos.RegisterRequestDto;
import ec.edu.ups.icc.fundamentos01.security.models.RoleEntity;
import ec.edu.ups.icc.fundamentos01.security.models.RoleName;
import ec.edu.ups.icc.fundamentos01.security.repository.RoleRepository;
import ec.edu.ups.icc.fundamentos01.security.utils.JwtUtil;
import ec.edu.ups.icc.fundamentos01.users.models.UserEntity;
import ec.edu.ups.icc.fundamentos01.users.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import ec.edu.ups.icc.fundamentos01.exceptions.domain.BadRequestException;
import ec.edu.ups.icc.fundamentos01.exceptions.domain.ConflictException;

@Service
public class AuthService {

    // Dependencias inyectadas para login y registro
    private final AuthenticationManager authenticationManager; // Valida credenciales
    private final UserRepository userRepository;               // Acceso a BD
    private final RoleRepository roleRepository;               // Gestión de roles
    private final PasswordEncoder passwordEncoder;             // Hash de passwords
    private final JwtUtil jwtUtil;                            // Generación de tokens

    public AuthService(AuthenticationManager authenticationManager,
                       UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    
    @Transactional(readOnly = true) // Solo lectura, no modifica BD
    public AuthResponseDto login(LoginRequestDto loginRequest) {
       
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
            )
        );

      
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

     
        Set<String> roles = userDetails.getAuthorities().stream()
            .map(item -> item.getAuthority()) 
            .collect(Collectors.toSet());

      
        return new AuthResponseDto(
            jwt,                      // Token para autenticación
            userDetails.getId(),      // ID del usuario
            userDetails.getName(),    // Nombre completo
            userDetails.getEmail(),   // Email
            roles                     // Roles asignados
        );
    }

  
    @Transactional // Requiere transacción para INSERT
    public AuthResponseDto register(RegisterRequestDto registerRequest) {
     
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new ConflictException("El email ya está registrado");
        }

        // 2. Crear nueva entidad de usuario
        UserEntity user = new UserEntity();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        // Hash del password con BCrypt (nunca almacenar en texto plano)
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        // 3. Asignar rol por defecto ROLE_USER
        // Si no existe: lanza BadRequestException → 400
        RoleEntity userRole = roleRepository.findByName(RoleName.ROLE_USER)
            .orElseThrow(() -> new BadRequestException("Rol por defecto no encontrado"));

        Set<RoleEntity> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        // 4. Guardar en BD (INSERT)
        user = userRepository.save(user);

        // 5. Generar JWT automáticamente para login directo
        // No requiere que el usuario haga login después de registrarse
        UserDetailsImpl userDetails = UserDetailsImpl.build(user);
        String jwt = jwtUtil.generateTokenFromUserDetails(userDetails);

        // Convertir roles a nombres de string
        Set<String> roleNames = user.getRoles().stream()
            .map(role -> role.getName().name()) // RoleName.ROLE_USER → "ROLE_USER"
            .collect(Collectors.toSet());

        // 6. Retornar JWT + datos del usuario registrado
        return new AuthResponseDto(
            jwt,
            user.getId(),
            user.getName(),
            user.getEmail(),
            roleNames
        );
    }
}