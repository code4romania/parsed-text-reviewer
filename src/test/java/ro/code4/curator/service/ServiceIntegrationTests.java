package ro.code4.curator.service;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import ro.code4.curator.entity.Text;
import ro.code4.curator.repository.ReviewedTextRepository;
import ro.code4.curator.transferObjects.ParsedTextFindingTO;
import ro.code4.curator.transferObjects.ParsedTextTO;

import java.util.List;

import static java.lang.Math.round;
import static org.junit.Assert.*;
import static ro.code4.curator.transferObjects.ParsedTextFindingTO.with;
import static ro.code4.curator.transferObjects.ParsedTextTO.withTextReference;

/**
 * The scope of this test is to validate business logic with as little mocking as possible.
 * We will use in memory impl of repositories.
 */
public class ServiceIntegrationTests {

    private ParsedTextService parsedTextService;
    private ReviewTextService reviewTextService;
    private InMemoryParsedTextRepository parsedInputRepo;
    private ReviewedTextRepository reviewedInputRepo;

    @Before
    public void setUp() throws Exception {
        parsedInputRepo = new InMemoryParsedTextRepository();
        reviewedInputRepo = new InMemoryReviewedInputRepository();

        parsedTextService = new ParsedTextService();
        parsedTextService.setParsedInputRepo(parsedInputRepo);

        reviewTextService = new ReviewTextService();
        reviewTextService.setParsedInputRepository(parsedInputRepo);
        reviewTextService.setReviewedInputRepo(reviewedInputRepo);
    }

    @Test
    @Ignore
    public void submitParsedText_submitReview_verifyMergeLogic() throws Exception {
        ParsedTextTO parsedTextTO = withTextReference(Text.with("srcId", "dna", "abc abcd"));
        parsedTextTO = parsedTextService.submitParsedText(parsedTextTO);

        ParsedTextFindingTO findingTO = ParsedTextFindingTO.with(parsedTextTO, "ab", "autor", 2, "parserId");
        parsedTextTO.getParsedFields().add(findingTO);
        parsedTextTO = parsedTextService.submitParsedText(parsedTextTO);

//        reviewTextService.submitReview(parsedTextTO.g)

        fail("TODO");
    }

    @Test
    public void onSubmitParsedText_should_increaseVotesForDuplicateFindings() throws Exception {
        ParsedTextTO textTo1 = withTextReference(Text.with("id1", "dna", "abcd"));
        ParsedTextFindingTO finding1 = with(
                null, "b", "nume", 1, "test-parser");
        textTo1.getParsedFields().add(finding1);
        ParsedTextTO savedTo1 = parsedTextService.submitParsedText(textTo1);
        List<ParsedTextFindingTO> parsedFields1 = parsedTextService.getAllParsedTexts().get(0).getParsedFields();
        assertEquals("should contain all findings",
                1, round(parsedFields1.get(0).getVotes()));

        ParsedTextTO textTO2 = withTextReference(Text.with("id1", "dna", "abcd"));
        ParsedTextFindingTO finding2 = with(
                null, "b", "nume", 1, "test-parser");
        textTO2.getParsedFields().add(finding2);
        ParsedTextTO savedTo2 = parsedTextService.submitParsedText(textTO2);

        List<ParsedTextFindingTO> parsedFields = savedTo2.getParsedFields();
        assertEquals("findings should be merged",
                1, parsedFields.size());
        assertEquals(2, round(parsedFields.get(0).getVotes()));
    }

    @Test
    public void onSubmitParsedText_should_mergeFindingsForDuplicates() throws Exception {
        ParsedTextTO textTo1 = withTextReference(Text.with("id1", "dna", "abcd"));
        ParsedTextFindingTO finding1 = with(
                null, "b", "nume", 1, "test-parser");
        textTo1.getParsedFields().add(finding1);
        ParsedTextTO savedTo1 = parsedTextService.submitParsedText(textTo1);
        assertEquals("should contain the first text only",
                1, parsedTextService.getAllParsedTexts().size());
        assertEquals("should contain all findings",
                1, parsedTextService.getAllParsedTexts().get(0).getParsedFields().size());

        ParsedTextTO textTO2 = withTextReference(Text.with("id1", "dna", "abcd"));
        ParsedTextFindingTO finding2 = with(
                null, "c", "nume", 2, "test-parser");
        textTO2.getParsedFields().add(finding2);
        ParsedTextTO savedTo2 = parsedTextService.submitParsedText(textTO2);

        assertEquals("findings should be joined",
                2, savedTo2.getParsedFields().size());
        assertEquals("finding 1",
                "b", savedTo2.getParsedFields().get(0).getParsedValue());
        assertEquals("finding 2",
                "c", savedTo2.getParsedFields().get(1).getParsedValue());

    }

    @Test
    public void should_mergeDuplicates_submittedParsedText() throws Exception {
        ParsedTextTO textTO1 = withTextReference(Text.with("id1", "dna", "abcd"));
        ParsedTextTO textTO2 = withTextReference(Text.with("id1", "dna", "abcd"));
        ParsedTextTO out1 = parsedTextService.submitParsedText(textTO1);
        ParsedTextTO out2 = parsedTextService.submitParsedText(textTO2);
        assertEquals("both parsed texts should be duplicates", out1.getEntityId(), out2.getEntityId());
        assertEquals("should contain only one text, the merged one",
                1, parsedTextService.getAllParsedTexts().size());
    }

    @Test
    public void should_FindById_submittedParsedText() throws Exception {
        ParsedTextTO out = parsedTextService.submitParsedText(new ParsedTextTO());
        assertEquals(out, parsedTextService.getParsedTextById(out.getEntityId()));
    }

    @Test
    public void should_NotFindById_submittedAndDeleted_ParsedText() throws Exception {
        ParsedTextTO out = parsedTextService.submitParsedText(new ParsedTextTO());
        parsedTextService.deleteParsedTextById(out.getEntityId());

        assertNull(parsedTextService.getParsedTextById(out.getEntityId()));
    }

}
