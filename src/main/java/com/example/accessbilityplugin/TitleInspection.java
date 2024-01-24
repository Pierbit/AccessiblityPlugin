package com.example.accessbilityplugin;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.XmlElementVisitor;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.NotNull;

public class TitleInspection extends LocalInspectionTool {

    public @NotNull PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new XmlElementVisitor() {
            @Override
            public void visitXmlTag(XmlTag tag) {
                super.visitXmlTag(tag);

                String test = tag.getName();

                if(tag.getName().equalsIgnoreCase("activity")){
                    if(tag.getAttributeValue("android:label") == null || tag.getAttributeValue("android:label").isEmpty()){

                        holder.registerProblem(tag,"The android:label attribute should be defined so that the activity name will be announced properly");

                    }
                }
            }
        };
    }
}
