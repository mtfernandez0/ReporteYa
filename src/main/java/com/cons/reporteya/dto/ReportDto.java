package com.cons.reporteya.dto;

import com.cons.reporteya.entity.Report;
import com.cons.reporteya.service.ReportService;
import lombok.*;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
@Builder
public class ReportDto {
    private Long report_id;

    private String title;

    private Double lat;

    private Double lng;

    private String location;

    public static ReportDto ReportToDto(Report report){
        return ReportDto.builder()
                .report_id(report.getId())
                .title(report.getTitle())
                .lat(report.getMarker().getLatitude())
                .lng(report.getMarker().getLongitude())
                .location(ReportService.finalLocation(report))
                .build();
    }
}
