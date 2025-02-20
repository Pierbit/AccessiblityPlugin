import com.example.accessbilityplugin.ColorMeaningInspection;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import com.intellij.codeInsight.daemon.impl.HighlightInfo;

import java.util.List;

public class ColorMeaningInspectionTest extends LightJavaCodeInsightFixtureTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        myFixture.setTestDataPath("src/test/testData");
    }

    public void testColorMeaningInspection(){

        myFixture.configureByFile("InspectionTest1.xml");
        myFixture.enableInspections(new ColorMeaningInspection());

        List<HighlightInfo> highlightInfoList = myFixture.doHighlighting();

        assertFalse(highlightInfoList.isEmpty());
    }
}
