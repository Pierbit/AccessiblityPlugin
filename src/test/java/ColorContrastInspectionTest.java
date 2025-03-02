import com.example.accessbilityplugin.AccessibilityIssue;
import com.example.accessbilityplugin.ColorContrastInspection;
import com.example.accessbilityplugin.ColorMeaningInspection;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import com.intellij.codeInsight.daemon.impl.HighlightInfo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ColorContrastInspectionTest extends LightJavaCodeInsightFixtureTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        myFixture.setTestDataPath("src/test/testDataColorContrast");
    }

    public void testColorContrastInspection(){

        ArrayList<AccessibilityIssue> risultati = new ArrayList<>();
        myFixture.enableInspections(new ColorContrastInspection());

        inspectFile("ColorContrastTest1.xml", risultati);
        inspectFile("ColorContrastTest2.xml", risultati);
        inspectFile("ColorContrastTest3.xml", risultati);
        inspectFile("ColorContrastTest4.xml", risultati);
        inspectFile("ColorContrastTest5.xml", risultati);
        inspectFile("ColorContrastTest6.xml", risultati);
        inspectFile("ColorContrastTest7.xml", risultati);
        inspectFile("ColorContrastTest8.xml", risultati);
        inspectFile("ColorContrastTest9.xml", risultati);

        for(AccessibilityIssue issue: risultati){
            System.out.println(issue.toString());
        }

        assertEquals(5, risultati.size());
    }

    public void inspectFile(String path, ArrayList<AccessibilityIssue> results){
        myFixture.configureByFile(path);
        List<HighlightInfo> highlightInfoList = myFixture.doHighlighting();
        for(HighlightInfo h: highlightInfoList){
            if(h.getInspectionToolId() != null) {
                if (h.getInspectionToolId().equals("ColorContrast")) {
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
