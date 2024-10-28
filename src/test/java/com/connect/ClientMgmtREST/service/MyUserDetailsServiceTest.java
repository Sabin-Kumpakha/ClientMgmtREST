package com.connect.ClientMgmtREST.service;

import com.connect.ClientMgmtREST.enums.Role;
import com.connect.ClientMgmtREST.model.User;
import com.connect.ClientMgmtREST.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@SpringBootTest
public class MyUserDetailsServiceTest {

    @InjectMocks
    private MyUserDetailsService myUserDetailsService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test   // loadByUserNameTest
    void loadUserByUsernameTest(){
       User mockUser = new User();
        mockUser.setName("Ram");
        mockUser.setEmail("ram@gmail.com");
        mockUser.setPhoneNumber("12345");
        mockUser.setPassword("password");
        mockUser.setRole(Role.USER);

        Mockito.when(userRepository.findUserByEmail(ArgumentMatchers.anyString())).thenReturn(Optional.of(mockUser));

        UserDetails userDetails = myUserDetailsService.loadUserByUsername("Ram");
        Assertions.assertNotNull(userDetails);
    }

    @Test   // Testing Existing User
    void testExistingUser() {
        User existingUser = new User();
        existingUser.setName("Sita");
        existingUser.setEmail("sita@gmail.com");
        existingUser.setPhoneNumber("67890");
        existingUser.setPassword("password123");
        existingUser.setRole(Role.ADMIN);

        Mockito.when(userRepository.findUserByEmail("sita@gmail.com")).thenReturn(Optional.of(existingUser));

        UserDetails userDetails = myUserDetailsService.loadUserByUsername("sita@gmail.com");
        Assertions.assertNotNull(userDetails);
        Assertions.assertEquals("sita@gmail.com", userDetails.getUsername());
    }

    @Test   // Testing Non Existing User
    void testNonExistingUser() {
        Mockito.when(userRepository.findUserByEmail("someone@gmail.com")).thenReturn(Optional.empty());

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            myUserDetailsService.loadUserByUsername("someone@gmail.com");
        });
    }

    @Test   // Testing User with ADMIN Role
    void testUserWithDifferentRoles() {
        User adminUser = new User();
        adminUser.setName("Admin");
        adminUser.setEmail("admin@gmail.com");
        adminUser.setPhoneNumber("54321");
        adminUser.setPassword("adminpassword");
        adminUser.setRole(Role.ADMIN);

        Mockito.when(userRepository.findUserByEmail("admin@gmail.com")).thenReturn(Optional.of(adminUser));

        UserDetails userDetails = myUserDetailsService.loadUserByUsername("admin@gmail.com");
        Assertions.assertNotNull(userDetails);
        Assertions.assertEquals("admin@gmail.com", userDetails.getUsername());
        Assertions.assertTrue(userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN")));
    }

    @Test   // Testing User with null Email
    void testUserWithNullEmail() {
        User nullEmailUser = new User();
        nullEmailUser.setName("NullEmail");
        nullEmailUser.setEmail(null);
        nullEmailUser.setPhoneNumber("98765");
        nullEmailUser.setPassword("nullpassword");
        nullEmailUser.setRole(Role.USER);

        Mockito.when(userRepository.findUserByEmail(null)).thenReturn(Optional.of(nullEmailUser));

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            myUserDetailsService.loadUserByUsername("");
        });
    }


}
