import com.example.accessbilityplugin.AccessibilityIssue;
import com.example.accessbilityplugin.ColorMeaningInspection;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import com.intellij.codeInsight.daemon.impl.HighlightInfo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ColorMeaningInspectionTest extends LightJavaCodeInsightFixtureTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        myFixture.setTestDataPath("src/test/testDataColorMeaning");
    }

    public void testColorMeaningInspection(){

        ArrayList<AccessibilityIssue> risultati = new ArrayList<>();
        myFixture.enableInspections(new ColorMeaningInspection());

        inspectFile("ColorMeaningTest1.xml", risultati);
        inspectFile("ColorMeaningTest2.xml", risultati);
        inspectFile("ColorMeaningTest3.xml", risultati);
        inspectFile("ColorMeaningTest4.xml", risultati);
        inspectFile("ColorMeaningTest5.xml", risultati);
        inspectFile("ColorMeaningTest6.xml", risultati);

        int focusable_counter = 0;
        int hint_counter = 0;
        int content_counter = 0;

        for(AccessibilityIssue issue: risultati){
            if(issue.getIssue().contains("Focusable")){
                focusable_counter++;
            } else if(issue.getIssue().contains("Hint")){
                hint_counter++;
            } else if(issue.getIssue().contains("Content")){
                content_counter++;
            }
        }

        assertEquals(14, content_counter);
        assertEquals(1, hint_counter);
        assertEquals(11, focusable_counter);
    }

    public void inspectFile(String path, ArrayList<AccessibilityIssue> results){
        myFixture.configureByFile(path);
        List<HighlightInfo> highlightInfoList = myFixture.doHighlighting();
        for(HighlightInfo h: highlightInfoList){
            if(h.getInspectionToolId() != null) {
                if (h.getInspectionToolId().equals("ColorMeaning")) {
                    String description = h.getDescription();
                    String inspectionReference = h.getInspectionToolId();
                    String fileName = myFixture.getFile().getName();
                    String severity = h.getSeverity().getName();
                    AccessibilityIssue issue = new AccessibilityIssue(inspectionReference, fileName,
                            description, severity);
                    results.add(issue);
                }
            }
        }
    }
}
