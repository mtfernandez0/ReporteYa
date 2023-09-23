package com.cons.reporteya.dto;

import com.cons.reporteya.entity.Report;
import lombok.*;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
@Builder
public class ReportDashDto {

    private Long report_id;
    private String title;
    private String description;
    private String additional_directions;

    public static ReportDashDto ReportToDashDto(Report report){
        return ReportDashDto.builder()
                .report_id(report.getId())
                .title(report.getTitle())
                .build();
    }
}
