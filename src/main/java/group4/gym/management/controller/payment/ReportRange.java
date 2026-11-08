package group4.gym.management.controller.payment;

import group4.gym.management.exception.ValidationException;
import group4.gym.management.util.DateRange;
import group4.gym.management.util.RequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Reads the fromDate/toDate filter shared by the report pages (BR-13, VR-16).
 * Empty fields use the current month; an invalid date or from > to shows an error
 * and falls back to the current month so the page still renders.
 */
final class ReportRange {

    private ReportRange() {
    }

    /**
     * Returns the requested range and sets the range, error and fieldErrors request attributes.
     */
    static DateRange read(HttpServletRequest request) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        LocalDate from = readDate(request, "fromDate", fieldErrors);
        LocalDate to = readDate(request, "toDate", fieldErrors);

        DateRange range = null;
        if (fieldErrors.isEmpty()) {
            try {
                range = DateRange.of(from, to);
            } catch (ValidationException ex) {
                // DateRange only rejects from > to; keep the page's message in English.
                fieldErrors.put("toDate", "End date must be on or after the start date.");
            }
        }
        if (range == null) {
            range = DateRange.currentMonth();
            request.setAttribute("error", "Invalid date range. Showing the current month instead.");
            request.setAttribute("fieldErrors", fieldErrors);
        }
        request.setAttribute("range", range);
        return range;
    }

    private static LocalDate readDate(HttpServletRequest request, String name, Map<String, String> fieldErrors) {
        try {
            return RequestUtil.getDate(request, name);
        } catch (DateTimeParseException ex) {
            fieldErrors.put(name, "Use a valid date.");
            return null;
        }
    }
}
