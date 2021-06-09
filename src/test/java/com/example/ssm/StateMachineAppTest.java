package com.example.ssm;

import com.example.ssm.dto.EmployeeDTO;
import com.example.ssm.dto.EmployeeWithUpdatedStateDTO;
import com.example.ssm.exception.EntityNotFoundException;
import com.example.ssm.mapper.EmployeeMapper;
import com.example.ssm.model.Employee;
import com.example.ssm.model.EmployeeState;
import com.example.ssm.repository.EmployeeRepository;
import com.example.ssm.service.EmployeeService;
import com.example.ssm.util.EmployeePostgresContainer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class StateMachineAppTest {

    @Autowired
    EmployeeService employeeService;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    @ClassRule
    public static PostgreSQLContainer<EmployeePostgresContainer> postgreSQLContainer = EmployeePostgresContainer.getInstance();

    @Transactional
    @Test
    void addEmployee() throws Exception {
        EmployeeDTO employeeEntity = EmployeeDTO.builder().name("Ahata").email("aaaaa@gmail.com").build();

        mockMvc.perform(post("/api/employees/add")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(employeeEntity)))
                .andExpect(status().isOk());

        Optional<Employee> employeeEntityOptional = employeeRepository.findByName(employeeEntity.getName());
        assertThat(employeeEntityOptional.isPresent()).isTrue();

        assertThat(employeeEntityOptional.get().getState()).isEqualTo(EmployeeState.ADDED);
    }


    @Transactional
    @Test
    void checkEmployee() throws Exception {
        EmployeeWithUpdatedStateDTO resultEmployeeDTO =
                getExpectedResultEmployeeDTO("employee1", EmployeeState.IN_CHECK, true);

        mockMvc.perform(post("/api/employees/{employeeId}/check", resultEmployeeDTO.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    EmployeeDTO employeeDTO = objectMapper.readValue(json, EmployeeDTO.class);
                    assertEquals(resultEmployeeDTO, employeeDTO);
                });
    }

    @Transactional
    @Test
    void approveEmployee() throws Exception {
        EmployeeWithUpdatedStateDTO resultEmployeeDTO =
                getExpectedResultEmployeeDTO("employee2", EmployeeState.APPROVED, true);

        mockMvc.perform(post("/api/employees/{employeeId}/approve", resultEmployeeDTO.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    EmployeeWithUpdatedStateDTO employeeDTO = objectMapper.readValue(json, EmployeeWithUpdatedStateDTO.class);
                    assertEquals(resultEmployeeDTO, employeeDTO);
                });
    }

    @Transactional
    @Test
    void activateEmployee() throws Exception {
        EmployeeWithUpdatedStateDTO resultEmployeeDTO =
                getExpectedResultEmployeeDTO("employee3", EmployeeState.ACTIVE, true);

        mockMvc.perform(post("/api/employees/{employeeId}/activate", resultEmployeeDTO.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    EmployeeWithUpdatedStateDTO employeeDTO = objectMapper.readValue(json, EmployeeWithUpdatedStateDTO.class);
                    assertEquals(resultEmployeeDTO, employeeDTO);
                });
    }

    @Transactional
    @Test
    void checkEmployeeNegative() throws Exception {
        UUID id = UUID.randomUUID();
        mockMvc.perform(post("/api/employees/{employeeId}/check", id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result ->  assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> assertEquals(String.format("There is no entity of type %s with id %s in DB!",
                        Employee.class, id), result.getResolvedException().getMessage()));
    }

    @Transactional
    @Test
    void approveEmployeeNegative() throws Exception {
        EmployeeWithUpdatedStateDTO resultEmployeeDTO =
                getExpectedResultEmployeeDTO("employee1", EmployeeState.ADDED, false);

        mockMvc.perform(post("/api/employees/{employeeId}/approve", resultEmployeeDTO.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    EmployeeWithUpdatedStateDTO employeeDTO = objectMapper.readValue(json, EmployeeWithUpdatedStateDTO.class);
                    assertEquals(resultEmployeeDTO, employeeDTO);
                });
    }

    private Employee getEmployeeEntity(String employeeName) {
        Optional<Employee> optionalEmployee = employeeRepository.findByName(employeeName);
        assertThat(optionalEmployee.isPresent()).isTrue();

        return optionalEmployee.get();
    }

    private EmployeeWithUpdatedStateDTO getExpectedResultEmployeeDTO(String employeeName, EmployeeState state, boolean isUpdatedState) {
        Employee employeeEntity = getEmployeeEntity(employeeName);
        EmployeeWithUpdatedStateDTO resultEmployeeDTO = employeeMapper.getEmployeeWithUpdatedState(employeeEntity, isUpdatedState);
        resultEmployeeDTO.setState(state);
        return resultEmployeeDTO;
    }
}