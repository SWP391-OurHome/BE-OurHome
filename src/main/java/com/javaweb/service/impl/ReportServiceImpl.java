package com.javaweb.service.impl;

import com.javaweb.model.ReportDTO;
import com.javaweb.repository.entity.ListingEntity;
import com.javaweb.repository.entity.PropertyEntity;
import com.javaweb.repository.entity.ReportEntity;
import com.javaweb.repository.entity.UserEntity;
import com.javaweb.repository.impl.ListingRepository;
import com.javaweb.repository.impl.ReportRepository;
import com.javaweb.repository.impl.PropertyRepository;
import com.javaweb.repository.impl.UserRepositoryImpl;
import com.javaweb.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private UserRepositoryImpl userRepository;
    @Autowired
    private ListingRepository listingRepository;

    public ReportDTO saveReport(ReportDTO reportDTO) throws Exception {
        List<String> activeStatuses = Arrays.asList("pending", "under_review");

        if (reportRepository.existsByProperty_IdAndUser_UserIdAndStatusIn(
                reportDTO.getPropertyId(),
                reportDTO.getUserId(),
                activeStatuses)) {
            throw new Exception("User has an active report for this property");
        }

        // Lấy các entity từ ID
        PropertyEntity property = propertyRepository.findById(reportDTO.getPropertyId())
                .orElseThrow(() -> new Exception("Property not found"));
        UserEntity user = userRepository.findById(reportDTO.getUserId())
                .orElseThrow(() -> new Exception("User not found"));

        // Gán vào entity
        ReportEntity report = new ReportEntity();
        report.setProperty(property);
        report.setUser(user);
        report.setReportReason(reportDTO.getReportReason());
        report.setReportDetail(reportDTO.getReportDetail());
        report.setStatus("pending");
        report.setReportDate(LocalDate.now());

        // Lưu và trả về
        ReportEntity savedReport = reportRepository.save(report);
        return convertToDTO(savedReport);
    }

    public List<ReportDTO> getAllReports() {
        return reportRepository.findAllByOrderByReportDateDesc()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ReportDTO updateReportStatus(Integer reportId, String status) throws Exception {
        ReportEntity report = reportRepository.findById(reportId)
                .orElseThrow(() -> new Exception("Report not found"));


        report.setStatus(status);
        ReportEntity updatedReport = reportRepository.save(report);

        Integer propertyId = report.getReportId();
        ListingEntity listing = listingRepository.findByPropertyId(propertyId)
                .orElseThrow(() -> new Exception("Listing not found for propertyId: " + propertyId));

        listing.setListingStatus(false);
        listingRepository.save(listing);

        return convertToDTO(updatedReport);
    }


    private ReportDTO convertToDTO(ReportEntity report) {
        ReportDTO dto = new ReportDTO();
        dto.setReportId(report.getReportId());
        dto.setPropertyId(report.getProperty().getId());
        dto.setUserId(report.getUser().getUserId());
        dto.setReportReason(report.getReportReason());
        dto.setReportDetail(report.getReportDetail());
        dto.setReportDate(report.getReportDate());
        dto.setStatus(report.getStatus());
        return dto;
    }
}
