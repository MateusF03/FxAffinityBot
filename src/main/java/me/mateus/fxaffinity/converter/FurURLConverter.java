package me.mateus.fxaffinity.converter;

import me.mateus.fxaffinity.api.FaExportWrapper;
import me.mateus.fxaffinity.entities.FurEntity;
import me.mateus.fxaffinity.entities.SubmissionEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FurURLConverter {

    private static final String URL_BASE = "https?://www.furaffinity.net/";
    public static final Pattern SUBMISSION_URL_PATTERN = Pattern.compile(URL_BASE + "view/(\\d+)/");

    private final FaExportWrapper WRAPPER = new FaExportWrapper();

    public List<FurEntity> convertContent(String content) {

        List<FurEntity> furEntities = new ArrayList<>();
        Matcher submissionMatcher = SUBMISSION_URL_PATTERN.matcher(content);
        while (submissionMatcher.find()) {
            String id = submissionMatcher.group(1);
            SubmissionEntity submission = WRAPPER.getSubmission(id);
            if (submission != null) {
                furEntities.add(submission);
            }
        }
        return furEntities;
    }
}
