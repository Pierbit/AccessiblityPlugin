import com.example.accessbilityplugin.AccessibilityIssue;
import com.example.accessbilityplugin.ColorMeaningInspection;
import com.example.accessbilityplugin.TitleInspection;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import com.intellij.codeInsight.daemon.impl.HighlightInfo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class TitleInspectionTest extends LightJavaCodeInsightFixtureTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        myFixture.setTestDataPath("src/test/testDataTitle");
    }

    public void testTitleInspection(){

        ArrayList<AccessibilityIssue> risultati = new ArrayList<>();
        myFixture.enableInspections(new TitleInspection());

        inspectFile("TitleTest1.xml", risultati);
        inspectFile("TitleTest2.xml", risultati);
        inspectFile("TitleTest3.xml", risultati);

        assertEquals(33,risultati.size());

    }

    public void inspectFile(String path, ArrayList<AccessibilityIssue> results){
        myFixture.configureByFile(path);
        List<HighlightInfo> highlightInfoList = myFixture.doHighlighting();
        for(HighlightInfo h: highlightInfoList){
            if(h.getInspectionToolId() != null) {
                if (h.getInspectionToolId().equals("Title")) {
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
