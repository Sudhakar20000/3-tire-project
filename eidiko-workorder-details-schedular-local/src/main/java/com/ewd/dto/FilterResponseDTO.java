package com.ewd.dto;

import com.ewd.entity.Employee;
import com.ewd.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterResponseDTO {
    private List<Employee> employees;
    private List<Project> projects;
}
