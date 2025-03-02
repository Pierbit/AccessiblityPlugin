package com.example.accessbilityplugin;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.editor.Document;
import com.intellij.psi.*;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.NotNull;

public class LabelsInspection extends LocalInspectionTool {

    public @NotNull PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new XmlElementVisitor() {
            @Override
            public void visitXmlTag(XmlTag tag) {
                super.visitXmlTag(tag);

                String id = tag.getAttributeValue("android:id");
                int tagline = getLine(tag);

                if (id != null && !(id.isEmpty())) {

                    int slashIndex = id.indexOf('/');
                    String temp = id.substring(slashIndex + 1);

                    XmlTag parent = tag.getParentTag();
                    if (parent != null) {
                        XmlTag[] children = parent.getSubTags();

                        for (XmlTag child : children) {

                            String label = child.getAttributeValue("android:labelFor");

                            if (label != null) {

                                int slashIndex1 = label.indexOf('/');
                                String temp1 = label.substring(slashIndex1 + 1);

                                if (temp.equals(temp1)) {
                                    int childline = getLine(child);
                                    if (childline > tagline)
                                        holder.registerProblem(child, "Label should come before the referenced tag", ProblemHighlightType.INFORMATION);
                                }

                            }
                        }
                    }
                }
            }
        };
    }

    public int getLine(XmlTag tag){

        int lineNumber = -1;
        PsiFile psiFile = tag.getContainingFile();

        if(psiFile != null){
            Document document = PsiDocumentManager.getInstance(tag.getProject()).getDocument(psiFile);
            if(document != null){
                lineNumber = document.getLineNumber(tag.getTextOffset()) + 1;
            }
        }
        return lineNumber;
    }
}
