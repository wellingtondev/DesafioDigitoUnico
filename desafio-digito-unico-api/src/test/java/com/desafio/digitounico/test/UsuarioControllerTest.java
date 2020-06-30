package com.desafio.digitounico.test;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.desafio.digitounico.controller.UsuarioController;
import com.desafio.digitounico.dto.UsuarioDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(UsuarioController.class)
@RunWith(SpringRunner.class)
public class UsuarioControllerTest {

	private static final Long ID = 1L;
	private static final String NOME = "Wellington";
	private static final String SOBRENOME = "Felipe";
	private static final String EMAIL = "wellingtonfdev@gmail.com";
	private static final String URL = "/api/usuario";
	private static final ObjectMapper OM = new ObjectMapper();

	@MockBean
	private UsuarioController controller;

	@Autowired
	private MockMvc mvc;
	
	
	@Test
    public void getAll() throws Exception {
		UsuarioDTO dto = UsuarioDTO.builder().id(ID).nome(NOME).email(EMAIL).build();
        List<UsuarioDTO> dtos = Arrays.asList(dto);

        given(controller.getAll()).willReturn(dtos);

        mvc.perform(get(URL + "/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
	
	@Test
	public void getAllById() throws Exception {
		UsuarioDTO dto = UsuarioDTO.builder().id(ID).nome(NOME).email(EMAIL).build();
		
		when(controller.getById(ID)).thenReturn(dto);
		
		mvc.perform(get(URL + "/" + ID)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
	
	@Test
    public void create() throws Exception {
		UsuarioDTO dto = UsuarioDTO.builder().nome(NOME).email(EMAIL).build();
		UsuarioDTO dtoSalvo = UsuarioDTO.builder().id(ID).nome(NOME).email(EMAIL).build();

		when(controller.create(dto)).thenReturn(new ResponseEntity<>(dtoSalvo, HttpStatus.CREATED));

        mvc.perform(post(URL + "/" + ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"nome\": \"Wellington\", \"email\": \"wellingtonfdev@gmail.com\" }")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

	@Test
	public void update() throws Exception {
		UsuarioDTO dto = UsuarioDTO.builder().id(ID).nome(NOME).email(EMAIL).build();
		UsuarioDTO updateDto = UsuarioDTO.builder().id(1L).nome(NOME.concat(" ").concat(SOBRENOME)).email(EMAIL).build();
		
		when(controller.update(ID, dto)).thenReturn(new ResponseEntity<>(updateDto, HttpStatus.CREATED));

        mvc.perform(put(URL + "/" + ID)
                .content(OM.writeValueAsBytes(updateDto))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
	}
	
	@Test
    public void deleteById() throws Exception {
        when(controller.deleteById(ID))
        .thenReturn(new ResponseEntity<>(ID, HttpStatus.OK));

        mvc.perform(delete(URL + "/" + ID)
        		.contentType(MediaType.APPLICATION_JSON)
        		.accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }
	
}
