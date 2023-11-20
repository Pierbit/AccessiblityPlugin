package com.example.accessbilityplugin;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.XmlElementVisitor;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorContrastInspection extends LocalInspectionTool {

    public @NotNull PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new XmlElementVisitor() {
            @Override
            public void visitXmlTag(XmlTag tag) {
                super.visitXmlTag(tag);

                if(tag.getName().equalsIgnoreCase("Button")){

                    XmlAttribute @NotNull [] attributi = tag.getAttributes();

                    for(XmlAttribute attribute : attributi){

                        boolean checkContrastFlag1 = false;
                        boolean checkContrastFlag2 = false;
                        Color foreground = null;
                        Color background = null;

                        if(attribute.getName().equalsIgnoreCase("textColor")){
                            if(attribute.getValue() != null && isColor(attribute.getValue())){
                                checkContrastFlag1 = true;
                                foreground = Color.decode(attribute.getValue());
                            }
                        }
                        if(attribute.getName().equalsIgnoreCase("background")){
                            if(attribute.getValue() != null && isColor(attribute.getValue())){
                                checkContrastFlag2 = true;
                                background = Color.decode(attribute.getValue());
                            }
                        }

                        if(checkContrastFlag1 && checkContrastFlag2){

                            double result = checkContrast(foreground,background);
                            if(result <= 4.5){
                                holder.registerProblem(tag,
                                        "Color contrast between text and background does not meet the WCAG standard",
                                        ProblemHighlightType.ERROR);
                            }
                        }
                    }
                }
            }
        };
    }

    public boolean isColor(String color){

        String pattern = "^#[0-9A-Fa-f]{6}$";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(color);

        return matcher.matches();

    }

    public double checkContrast(Color colore1, Color colore2){

        ArrayList<Color> colori = new ArrayList<>();
        ArrayList<Double> relativeLuminances = new ArrayList<>();
        colori.add(colore1);
        colori.add(colore2);

        for(Color colore: colori){

            double red;
            double green;
            double blue;

            if(colore.getRed() <= 0.03928) red = colore.getRed() / 12.92;
            else red = Math.pow(((colore.getRed() + 0.055) / 1.055), 2.4);

            if(colore.getGreen() <= 0.03928) green = colore.getGreen() / 12.92;
            else green = Math.pow(((colore.getGreen() + 0.055) / 1.055), 2.4);

            if(colore.getBlue() <= 0.03928) blue= colore.getBlue() / 12.92;
            else blue = Math.pow(((colore.getBlue() + 0.055) / 1.055), 2.4);

            relativeLuminances.add((0.2126*red)+(0.0722*blue)+(0.7152*green));
        }

        return ((Math.max(relativeLuminances.get(0),relativeLuminances.get(1)) + 0.05) / (Math.min(relativeLuminances.get(0), relativeLuminances.get(1)) + 0.05));
    }
}
