package com.bankingwebapp.api;

import com.bankingwebapp.entity.User;
import com.bankingwebapp.service.UserService;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserRestController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@Test
	void createUser_success() throws Exception {
		User inputUser = new User();
		inputUser.setUsername("john");
		inputUser.setPassword("123");
		inputUser.setEmail("john@example.com");
		inputUser.setRole("USER");

		when(userService.getUserByUsername("john")).thenReturn(Optional.empty());
		when(userService.getUserByEmail("john@example.com")).thenReturn(Optional.empty());
		when(userService.saveUser(any(User.class))).thenReturn(inputUser);

		String json = """
				    {
				        "username": "john",
				        "password": "123",
				        "email": "john@example.com",
				        "role": "USER"
				    }
				""";

		mockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isOk()).andExpect(jsonPath("$.username").value("john"));
	}

	@Test
    void createUser_duplicateUsername() throws Exception {
        when(userService.getUserByUsername("john")).thenReturn(Optional.of(new User()));

        String json = """
            {
                "username": "john",
                "password": "123",
                "email": "john@example.com",
                "role": "USER"
            }
        """;

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Username already exists"));
    }

	@Test
	void getUserById_found() throws Exception {
		User user = new User();
		user.setId(1L);
		user.setUsername("alice");
		user.setEmail("alice@example.com");
		user.setPassword("pw");
		user.setRole("USER");

		when(userService.getUserById(1L)).thenReturn(Optional.of(user));

		mockMvc.perform(get("/api/users/1")).andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value("alice"));
	}

	@Test
    void getUserById_notFound() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isNotFound());
    }

	@Test
	void getAllUsers() throws Exception {
		User user1 = new User();
		user1.setUsername("u1");
		user1.setEmail("u1@example.com");
		user1.setPassword("pw1");
		user1.setRole("USER");

		User user2 = new User();
		user2.setUsername("u2");
		user2.setEmail("u2@example.com");
		user2.setPassword("pw2");
		user2.setRole("ADMIN");

		when(userService.getAllUsers()).thenReturn(List.of(user1, user2));

		mockMvc.perform(get("/api/users")).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(2));
	}

	@Test
	void deleteUser_found() throws Exception {
		User user = new User();
		user.setId(1L);
		user.setUsername("to_delete");

		when(userService.getUserById(1L)).thenReturn(Optional.of(user));
		doNothing().when(userService).deleteUserById(1L);

		mockMvc.perform(delete("/api/users/1")).andExpect(status().isNoContent());
	}

	@Test
    void deleteUser_notFound() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNotFound());
    }
}
