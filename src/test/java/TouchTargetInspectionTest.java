import com.example.accessbilityplugin.AccessibilityIssue;
import com.example.accessbilityplugin.ColorContrastInspection;
import com.example.accessbilityplugin.ColorMeaningInspection;
import com.example.accessbilityplugin.TouchTargetInspection;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import com.intellij.codeInsight.daemon.impl.HighlightInfo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class TouchTargetInspectionTest extends LightJavaCodeInsightFixtureTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        myFixture.setTestDataPath("src/test/testDataTouchTarget");
    }

    public void testTouchTargetInspection(){

        ArrayList<AccessibilityIssue> risultati = new ArrayList<>();
        myFixture.enableInspections(new TouchTargetInspection());

        inspectFile("TouchTargetTest1.xml", risultati);
        inspectFile("TouchTargetTest2.xml", risultati);
        inspectFile("TouchTargetTest3.xml", risultati);
        inspectFile("TouchTargetTest4.xml", risultati);
        inspectFile("TouchTargetTest5.xml", risultati);
        inspectFile("TouchTargetTest6.xml", risultati);
        inspectFile("TouchTargetTest7.xml", risultati);

        for(AccessibilityIssue issue: risultati){
            System.out.println(issue.toString());
        }

        assertEquals(14, risultati.size());

    }

    public void inspectFile(String path, ArrayList<AccessibilityIssue> results){
        myFixture.configureByFile(path);
        List<HighlightInfo> highlightInfoList = myFixture.doHighlighting();
        for(HighlightInfo h: highlightInfoList){
            if(h.getInspectionToolId() != null) {
                if (h.getInspectionToolId().equals("TouchTarget")) {
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

