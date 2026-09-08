package com.aistartup.incubator.controller;

import com.aistartup.incubator.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    /**
     * Compiles idea, validation, business model canvas, competitor analysis,
     * financial plan, MVP plan and investor pitch data for a startup into a
     * single downloadable PDF report.
     */
    @GetMapping("/{startupId}/pdf")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long startupId) {
        byte[] pdf = reportService.generatePdfReport(startupId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=startup-plan-" + startupId + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
