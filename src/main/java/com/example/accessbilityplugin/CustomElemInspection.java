package com.example.accessbilityplugin;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.XmlElementVisitor;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class CustomElemInspection extends LocalInspectionTool {
    public @NotNull PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new XmlElementVisitor() {
            @Override
            public void visitXmlTag(XmlTag tag) {
                super.visitXmlTag(tag);

                XmlFile xmlFile = (XmlFile) tag.getContainingFile();
                String rootName = "";
                boolean focusedFlag = false;

                if(xmlFile.getRootTag() != null) rootName = xmlFile.getRootTag().getName();

                if(rootName.equalsIgnoreCase("selector")){

                    XmlTag root = xmlFile.getRootTag();
                    XmlTag[] tags = root.getSubTags();

                    for(XmlTag t : tags){

                       if(t.getAttributeValue("android:state_focused") != null) {

                           if(t.getAttributeValue("android:state_focused").equalsIgnoreCase("true")){
                               focusedFlag = true;
                           }
                       }
                    }

                    if(!focusedFlag){
                        holder.registerProblem(tag,"A focused state must always be defined for custom elements");
                    }
                }
            }
        };
    }
}
