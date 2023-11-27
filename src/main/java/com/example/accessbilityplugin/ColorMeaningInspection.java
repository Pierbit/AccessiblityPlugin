package com.example.accessbilityplugin;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.XmlElementVisitor;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Objects;

public class ColorMeaningInspection extends LocalInspectionTool {

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new XmlElementVisitor() {
            @Override
            public void visitXmlTag(XmlTag tag) {
                super.visitXmlTag(tag);

                if (tag.getName().equalsIgnoreCase("ImageView")) {

                    String testString = tag.getAttributeValue("android:contentDescription");

                    if(testString == null || testString.isEmpty()){
                        holder.registerProblem(tag, "ContentDescription for imageView missing or empty");
                    }
                }

                if(tag.getName().equalsIgnoreCase("VideoView")){

                    String testString = tag.getAttributeValue("android:contentDescription");

                    if(testString == null || testString.isEmpty()){
                        holder.registerProblem(tag, "ContentDescription for videoView missing or empty");
                    }

                }

                if(tag.getName().equalsIgnoreCase("EditText")){

                    String testString1 = tag.getAttributeValue("android:hint");
                    String testString2 = tag.getAttributeValue("android:focusable");

                    if(testString1 == null || testString1.isEmpty()){
                        holder.registerProblem(tag, "Hint for EditText missing or empty");
                    }
                    if(testString2 == null || testString2.isEmpty()){
                        holder.registerProblem(tag, "Focusable attribute for EditText missing or empty");
                    }

                }

                if(tag.getName().equalsIgnoreCase("Button")){

                    String testString1 = tag.getAttributeValue("android:contentDescription");
                    String testString2 = tag.getAttributeValue("android:focusable");

                    if(testString1 == null || testString1.isEmpty()){
                        holder.registerProblem(tag, "ContentDescription for button missing or empty");
                    }
                    if(testString2 == null || testString2.isEmpty()){
                        holder.registerProblem(tag, "Focusable attribute for button missing or empty");
                    }
                }
            }
        };
    }
}