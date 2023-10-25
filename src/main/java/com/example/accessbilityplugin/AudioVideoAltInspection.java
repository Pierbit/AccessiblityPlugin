package com.example.accessbilityplugin;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.XmlElementVisitor;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class AudioVideoAltInspection extends LocalInspectionTool {

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new XmlElementVisitor() {
            @Override
            public void visitXmlTag(XmlTag tag) {
                super.visitXmlTag(tag);

                if (tag.getName().equalsIgnoreCase("ImageView")) {
                    XmlAttribute @NotNull [] attributi = tag.getAttributes();

                    boolean contentDescriptionFlag = false;
                    boolean focusableFlag = false;
                    boolean screenReaderFocusableFlag = false;

                    for(XmlAttribute attribute : attributi) {

                        if (attribute.getName().equalsIgnoreCase("android:contentDescription")) {
                            if (!(Objects.requireNonNull(attribute.getValue()).equalsIgnoreCase(""))) {
                                contentDescriptionFlag = true;
                            }
                        }
                        if(attribute.getName().equalsIgnoreCase("android:focusable")){
                            if(attribute.getValue() != null){
                                if(Objects.requireNonNull(attribute.getValue()).equalsIgnoreCase("true")){
                                    focusableFlag = true;
                                }
                            }
                        }
                        if(attribute.getName().equalsIgnoreCase("android:screenReaderFocusable")){
                            if(Objects.requireNonNull(attribute.getValue()).equalsIgnoreCase("yes")){
                                screenReaderFocusableFlag = true;
                            }
                        }
                    }
                    if (!contentDescriptionFlag) {
                        holder.registerProblem(tag, "ContentDescription for imageView missing or empty");
                    } else if(!focusableFlag && !screenReaderFocusableFlag){
                        holder.registerProblem(tag, "ContentDescription cannot be focused and will not be announced, define android:focusable and android:screenReaderFocusable or set them to true/yes");
                    } else if(focusableFlag && !screenReaderFocusableFlag){
                        holder.registerProblem(tag, "ContentDescription can be focused but its content will not be announced, define android:screenReaderFocusable or set it to'yes'");
                    } else if(!focusableFlag){
                        holder.registerProblem(tag, "ContentDescription cannot be focused, define android:focusable or set it to 'true'");
                    }
                }
            }
        };
    }
}