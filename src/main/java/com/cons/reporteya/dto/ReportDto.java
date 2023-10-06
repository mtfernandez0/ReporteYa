package com.cons.reporteya.dto;

import com.cons.reporteya.entity.Report;
import com.cons.reporteya.service.ReportService;
import lombok.*;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
@Builder
public class ReportDto {
    private Long report_id;
    private String title;
    private String description;
    private Double lat;
    private Double lng;
    private String location;

    public static ReportDto ReportToDto(Report report){
        return ReportDto.builder()
                .report_id(report.getId())
                .title(report.getTitle())
                .description(report.getDescription())
                .lat(report.getMarker().getLatitude())
                .lng(report.getMarker().getLongitude())
                .location(report.getMarker().getLocation_name())
                .build();
    }
}
