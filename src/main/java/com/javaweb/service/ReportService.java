package com.javaweb.service;

import com.javaweb.model.ReportDTO;

import java.util.List;

public interface ReportService {
    public ReportDTO saveReport(ReportDTO reportDTO) throws Exception;
    public List<ReportDTO> getAllReports();
    public ReportDTO updateReportStatus(Integer reportId, String status) throws Exception;
    public ReportDTO deleteReport(Integer reportId) throws Exception;
}
