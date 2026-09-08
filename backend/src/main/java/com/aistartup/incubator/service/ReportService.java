package com.aistartup.incubator.service;

import com.aistartup.incubator.exception.ApiException;
import com.aistartup.incubator.model.*;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.function.Supplier;

/**
 * Assembles the outputs of every module (validation, business model canvas,
 * competitor analysis, financial plan, MVP plan, investor pitch) for a given
 * startup into a single investor-ready PDF report. Any module that hasn't
 * been generated yet for the startup is silently skipped.
 */
@Service
@RequiredArgsConstructor
public class ReportService {

    private final StartupService startupService;
    private final ValidationService validationService;
    private final BusinessModelService businessModelService;
    private final CompetitorService competitorService;
    private final FinancialService financialService;
    private final MvpService mvpService;
    private final PitchService pitchService;

    public byte[] generatePdfReport(Long startupId) {
        Startup startup = startupService.getById(startupId);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (PdfDocument pdfDoc = new PdfDocument(new PdfWriter(baos));
             Document document = new Document(pdfDoc)) {

            document.add(new Paragraph(startup.getName())
                    .setBold().setFontSize(22).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("AI-Generated Startup Plan")
                    .setFontSize(12).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph(" "));

            addSection(document, "Overview", startup.getDescription());
            addSection(document, "Industry", startup.getIndustry());

            tryAdd(() -> validationService.getLatest(startupId), v -> {
                addSection(document, "Validation — Feasibility Score", v.getFeasibilityScore() + " / 100");
                addSection(document, "Market Size Analysis", v.getMarketSizeAnalysis());
                addSection(document, "Key Risks", v.getRisks());
                addSection(document, "Recommendations", v.getRecommendations());
            });

            tryAdd(() -> businessModelService.getLatest(startupId), b -> {
                document.add(new Paragraph("Business Model Canvas").setBold().setFontSize(16));
                addSection(document, "Value Propositions", b.getValuePropositions());
                addSection(document, "Customer Segments", b.getCustomerSegments());
                addSection(document, "Channels", b.getChannels());
                addSection(document, "Customer Relationships", b.getCustomerRelationships());
                addSection(document, "Key Activities", b.getKeyActivities());
                addSection(document, "Key Resources", b.getKeyResources());
                addSection(document, "Key Partners", b.getKeyPartners());
                addSection(document, "Cost Structure", b.getCostStructure());
                addSection(document, "Revenue Streams", b.getRevenueStreams());
            });

            tryAdd(() -> competitorService.getLatest(startupId), c -> {
                document.add(new Paragraph("Competitor Analysis").setBold().setFontSize(16));
                addSection(document, "Competitors", c.getCompetitors());
                addSection(document, "Market Position", c.getMarketPosition());
                addSection(document, "Differentiation Strategy", c.getDifferentiationStrategy());
            });

            tryAdd(() -> financialService.getLatest(startupId), f -> {
                document.add(new Paragraph("Financial Plan").setBold().setFontSize(16));
                addSection(document, "Estimated Startup Cost", "$" + f.getEstimatedStartupCost());
                addSection(document, "Funding Needed", "$" + f.getFundingNeeded());
                addSection(document, "Break-Even Month", String.valueOf(f.getBreakEvenMonth()));
                addSection(document, "Assumptions", f.getAssumptions());
            });

            tryAdd(() -> mvpService.getLatest(startupId), m -> {
                document.add(new Paragraph("MVP Roadmap").setBold().setFontSize(16));
                addSection(document, "Core Features", m.getCoreFeatures());
                addSection(document, "Recommended Tech Stack", m.getRecommendedTechStack());
                addSection(document, "Estimated Timeline", m.getEstimatedTimeline());
            });

            tryAdd(() -> pitchService.getLatest(startupId), p -> {
                document.add(new Paragraph("Investor Pitch").setBold().setFontSize(16));
                addSection(document, "Elevator Pitch", p.getElevatorPitch());
                addSection(document, "Problem", p.getProblemStatement());
                addSection(document, "Solution", p.getSolution());
                addSection(document, "Market Opportunity", p.getMarketOpportunity());
                addSection(document, "Business Model Summary", p.getBusinessModelSummary());
                addSection(document, "Ask & Use of Funds", p.getAskAndUseOfFunds());
            });

            document.close();
            return baos.toByteArray();
        }
    }

    private <T> void tryAdd(Supplier<T> fetch, java.util.function.Consumer<T> render) {
        try {
            render.accept(fetch.get());
        } catch (ApiException notFound) {
            // Module hasn't been generated yet for this startup — skip it in the report.
        }
    }

    private void addSection(Document document, String heading, String body) {
        if (body == null || body.isBlank()) return;
        document.add(new Paragraph(heading).setBold().setFontSize(13));
        document.add(new Paragraph(body).setFontSize(11));
        document.add(new Paragraph(" "));
    }
}
