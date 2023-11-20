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
                    XmlAttribute @NotNull [] attributi = tag.getAttributes();
                    boolean contentDescriptionFlag = false;



                    for(XmlAttribute attribute : attributi) {

                        if (attribute.getName().equalsIgnoreCase("android:contentDescription")) {
                            if (!(Objects.requireNonNull(attribute.getValue()).equalsIgnoreCase(""))) {
                                contentDescriptionFlag = true;
                            }
                        }
                    }
                    if (!contentDescriptionFlag) {
                        holder.registerProblem(tag, "ContentDescription for imageView missing or empty");
                    }
                }

                if(tag.getName().equalsIgnoreCase("VideoView")){

                    XmlAttribute @NotNull [] attributi = tag.getAttributes();
                    boolean contentDescriptionFlag = false;

                    for(XmlAttribute attribute : attributi) {

                        if (attribute.getName().equalsIgnoreCase("android:contentDescription")) {
                            if (!(Objects.requireNonNull(attribute.getValue()).equalsIgnoreCase(""))) {
                                contentDescriptionFlag = true;
                            }
                        }
                    }
                    if (!contentDescriptionFlag) {
                        holder.registerProblem(tag, "ContentDescription for VideoView missing or empty");
                    }
                }

                if(tag.getName().equalsIgnoreCase("EditText")){

                    XmlAttribute @NotNull [] attributi = tag.getAttributes();
                    boolean hintFlag = false;
                    boolean focusableFlag = false;

                    for(XmlAttribute attribute : attributi) {

                        if (attribute.getName().equalsIgnoreCase("android:hint")) {
                            if (!(Objects.requireNonNull(attribute.getValue()).equalsIgnoreCase(""))) {
                                hintFlag = true;
                            }
                        }

                        if (attribute.getName().equalsIgnoreCase("android:focusable")){
                            if(!(Objects.requireNonNull(attribute.getValue()).equalsIgnoreCase("true"))){
                                focusableFlag = true;
                            }
                        }
                    }
                    if (!hintFlag) {
                        holder.registerProblem(tag, "Hint should be defined for EditText");
                    }
                    if(focusableFlag) {
                        holder.registerProblem(tag, "Focusable attribute should be set to true for every focusable element");
                    }
                }

                if(tag.getName().equalsIgnoreCase("Button")){

                    XmlAttribute @NotNull [] attributi = tag.getAttributes();
                    boolean contentDescriptionFlag = false;
                    boolean focusableFlag = false;

                    for(XmlAttribute attribute : attributi) {

                        if (attribute.getName().equalsIgnoreCase("android:contentDescription")) {
                            if (!(Objects.requireNonNull(attribute.getValue()).equalsIgnoreCase(""))) {
                                contentDescriptionFlag = true;
                            }
                        }
                        if (attribute.getName().equalsIgnoreCase("android:focusable")){
                            if((attribute.getValue().equals("true"))){
                                focusableFlag = true;
                            }
                        }
                    }
                    if (!contentDescriptionFlag) {
                        holder.registerProblem(tag, "ContentDescription should be defined for button");
                    }
                    if (!focusableFlag){
                        holder.registerProblem(tag,"Focusable attribute should be declared and set to true for button");
                    }
                }
            }
        };
    }
}