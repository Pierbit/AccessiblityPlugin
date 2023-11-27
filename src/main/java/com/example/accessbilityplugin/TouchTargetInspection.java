package com.example.accessbilityplugin;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.XmlElementVisitor;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TouchTargetInspection extends LocalInspectionTool {
    public @NotNull PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new XmlElementVisitor() {
            @Override
            public void visitXmlTag(XmlTag tag) {
                super.visitXmlTag(tag);

                if(tag.getName().equalsIgnoreCase("Button")){

                    String pattern = "([0-9]{1,100})(?:dip|dp)";

                    Pattern regex = Pattern.compile(pattern);

                    String temp1 = tag.getAttributeValue("android:layout_width");
                    String temp2 = tag.getAttributeValue("android:layout_height");

                    if(temp1 != null && temp2 != null){

                        Matcher matcher = regex.matcher(temp1);
                        Matcher matcher1 = regex.matcher(temp2);

                        if(matcher.matches() && matcher1.matches()){

                            String test = matcher.group(1);
                            String test1 = matcher1.group(1);

                            try {

                                int width_size = Integer.parseInt(test);
                                int height_size = Integer.parseInt(test1);

                                if(width_size < 48) holder.registerProblem(tag,"Minimum width for touch target is 48dip", ProblemHighlightType.ERROR);
                                if(height_size < 48) holder.registerProblem(tag,"Minimum height for touch target is 48dip", ProblemHighlightType.ERROR);

                            }catch(Exception e){
                                System.out.println("Input was not integer");
                            }
                        }
                    }
                }

            }
        };
    }
}
