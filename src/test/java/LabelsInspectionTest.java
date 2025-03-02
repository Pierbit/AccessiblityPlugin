import com.example.accessbilityplugin.*;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import com.intellij.codeInsight.daemon.impl.HighlightInfo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class LabelsInspectionTest extends LightJavaCodeInsightFixtureTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        myFixture.setTestDataPath("src/test/testDataLabels");
    }

    public void testLabelsInspection(){

        ArrayList<AccessibilityIssue> risultati = new ArrayList<>();
        myFixture.enableInspections(new LabelsInspection());

        inspectFile("LabelsTest1.xml", risultati);
        inspectFile("LabelsTest2.xml", risultati);
        inspectFile("LabelsTest3.xml", risultati);
        inspectFile("LabelsTest4.xml", risultati);
        inspectFile("LabelsTest5.xml", risultati);
        inspectFile("LabelsTest6.xml", risultati);
        inspectFile("LabelsTest7.xml", risultati);
        inspectFile("LabelsTest8.xml", risultati);
        inspectFile("LabelsTest9.xml", risultati);
        inspectFile("LabelsTest10.xml", risultati);

        for(AccessibilityIssue issue: risultati){
            System.out.println(issue.toString());
        }

        assertEquals(2, risultati.size());

    }

    public void inspectFile(String path, ArrayList<AccessibilityIssue> results){
        myFixture.configureByFile(path);
        List<HighlightInfo> highlightInfoList = myFixture.doHighlighting();
        for(HighlightInfo h: highlightInfoList){
            if(h.getInspectionToolId() != null) {
                if (h.getInspectionToolId().equals("Labels")) {
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

