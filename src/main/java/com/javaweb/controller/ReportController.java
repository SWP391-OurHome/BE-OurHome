package com.javaweb.controller;

import com.javaweb.model.ReportDTO;
import com.javaweb.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

// Controller
@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class ReportController {
    @Autowired
    public ReportService reportService;

    // Create new report
    @PostMapping
    public ResponseEntity<?> createReport(@RequestBody(required = false) ReportDTO reportDTO) {
        try {
            ReportDTO savedReport = reportService.saveReport(reportDTO);
            return ResponseEntity.ok(savedReport);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get all reports for admin
    @GetMapping("/admin")
    public ResponseEntity<List<ReportDTO>> getAllReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    // Update report status
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Integer id, @RequestBody(required = false) Map<String, String> statusUpdate) {
        try {
            ReportDTO updatedReport = reportService.updateReportStatus(id, statusUpdate.get("status"));
            return ResponseEntity.ok(updatedReport);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}