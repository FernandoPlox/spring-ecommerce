package com.curso.ecommerce.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

import com.curso.ecommerce.model.Usuario;

import jakarta.servlet.http.HttpSession;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private IUsuarioService usuarioService;
    
    @Autowired
    HttpSession session;

    private final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Buscando usuario con correo electrónico: {}", username);
        Optional<Usuario> optionalUser = usuarioService.findByEmail(username);

        if (optionalUser.isPresent()) {
            Usuario usuario = optionalUser.get();
            log.info("Usuario encontrado con ID: {}", usuario.getId());
            session.setAttribute("idusuario", optionalUser.get().getId());

            // Construir UserDetails
            return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getPassword()) // La contraseña ya debe estar codificada
                .roles(usuario.getTipo())
                .build();
        } else {
            log.error("Usuario no encontrado con correo electrónico: {}", username);
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
    }
}
