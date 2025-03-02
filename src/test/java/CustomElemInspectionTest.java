import com.example.accessbilityplugin.AccessibilityIssue;
import com.example.accessbilityplugin.ColorMeaningInspection;
import com.example.accessbilityplugin.CustomElemInspection;
import com.example.accessbilityplugin.TitleInspection;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import com.intellij.codeInsight.daemon.impl.HighlightInfo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CustomElemInspectionTest extends LightJavaCodeInsightFixtureTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        myFixture.setTestDataPath("src/test/testDataCustomElem");
    }

    public void testCustomElemInspection(){

        int issues_counter = 0;
        myFixture.enableInspections(new CustomElemInspection());

        if(inspectFile("CustomElemTest1.xml")) issues_counter++;
        if(inspectFile("CustomElemTest2.xml")) issues_counter++;
        if(inspectFile("CustomElemTest3.xml")) issues_counter++;
        if(inspectFile("CustomElemTest4.xml")) issues_counter++;
        if(inspectFile("CustomElemTest5.xml")) issues_counter++;

        assertEquals(3,issues_counter);

    }

    public boolean inspectFile(String path){
        myFixture.configureByFile(path);
        List<HighlightInfo> highlightInfoList = myFixture.doHighlighting();
        for(HighlightInfo h: highlightInfoList){
            if(h.getInspectionToolId() != null) {
                if (h.getInspectionToolId().equals("CustomElem")) {
                    return true;
                }
            }
        }

        return false;
    }
}

