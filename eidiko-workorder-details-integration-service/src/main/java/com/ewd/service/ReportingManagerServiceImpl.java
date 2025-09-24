package com.ewd.service;

import com.ewd.entity.ReportingManager;
import com.ewd.exception.ResourceNotFoundException;
import com.ewd.dto.Response;  // Make sure this matches your Response DTO package
import com.ewd.repository.ReportingManagerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportingManagerServiceImpl implements ReportingManagerService {

    private final ReportingManagerRepo reportingManagerRepo;

    @Autowired
    public ReportingManagerServiceImpl(ReportingManagerRepo reportingManagerRepo) {
        this.reportingManagerRepo = reportingManagerRepo;
    }

    @Override
    public Response addReportingManager(ReportingManager reportingManager) {
        ReportingManager saved = reportingManagerRepo.save(reportingManager);
        Map<String, Object> result = new HashMap<>();
        result.put("manager", saved);

        return Response.builder()
                .status("SUCCESS")
                .message("Manager saved successfully")
                .result(result)
                .build();
    }

    @Override
    public Response updateReportingManager(Long id, ReportingManager reportingManager) {
        ReportingManager existing = reportingManagerRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ReportingManager not found with id: " + id));

        existing.setManager(reportingManager.getManager());
        ReportingManager updated = reportingManagerRepo.save(existing);

        Map<String, Object> result = new HashMap<>();
        result.put("updatedManager", updated);

        return Response.builder()
                .status("SUCCESS")
                .message("Manager updated successfully")
                .result(result)
                .build();
    }

    @Override
    public Response deleteReportingManager(Long id) {
        ReportingManager existing = reportingManagerRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ReportingManager not found with id: " + id));

        reportingManagerRepo.delete(existing);

        return Response.builder()
                .status("SUCCESS")
                .message("Manager deleted successfully")
                .build();
    }

    @Override
    public Response getReportingManagerById(Long id) {
        ReportingManager manager = reportingManagerRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ReportingManager not found with id: " + id));

        Map<String, Object> result = new HashMap<>();
        result.put("manager", manager);

        return Response.builder()
                .status("SUCCESS")
                .message("Manager fetched successfully")
                .result(result)
                .build();
    }

    @Override
    public Response getAllReportingManagers() {
        List<ReportingManager> list = reportingManagerRepo.findAll();

        if (list.isEmpty()) {
            return Response.builder()
                    .status("SUCCESS")
                    .message("No managers found")
                    .result(Map.of("managers", list)) 
                    .build();
        }

        Map<String, Object> result = new HashMap<>();
        result.put("managers", list);

        return Response.builder()
                .status("SUCCESS")
                .message("All managers fetched successfully")
                .result(result)
                .build();
    }

}
