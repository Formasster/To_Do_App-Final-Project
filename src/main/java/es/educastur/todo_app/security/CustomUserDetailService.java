package es.educastur.todo_app.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import es.educastur.todo_app.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Busca al usuario en la base de datos
        return userRepository.findByUsername(username)
                // Si no existe, lanza un error que devolverá un HTTP 401
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    }
}