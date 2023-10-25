package com.example.accessbilityplugin;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.XmlElementVisitor;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.NotNull;

public class AudioVideoAltInspection extends LocalInspectionTool {

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new XmlElementVisitor() {
            @Override
            public void visitXmlTag(XmlTag tag) {
                super.visitXmlTag(tag);
                System.out.println("Visiting XML tag: " + tag.getName());

                if (tag.getName().equalsIgnoreCase("TextView")) {
                    XmlAttribute @NotNull [] attributi = tag.getAttributes();

                    boolean flag = false;

                    for (XmlAttribute attribute : attributi) {
                        if (attribute.getName().equalsIgnoreCase("android:text")) {
                            if (!(attribute.getValue().equalsIgnoreCase(""))) {
                                flag = true;
                            }
                        }
                    }

                    if (!flag) {
                        holder.registerProblem(tag, "TextView dovrebbe sempre definire del testo!");
                    }
                }
            }
        };
    }
}