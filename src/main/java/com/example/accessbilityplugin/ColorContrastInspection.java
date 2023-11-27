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

                if (tag.getName().equalsIgnoreCase("Button")) {
                    double result = checkTag(tag);
                    if (result <= 4.5 && result != -1) {
                        holder.registerProblem(tag,
                                "Color contrast between text and background does not meet the WCAG standard");
                    }
                }
                if (tag.getName().equalsIgnoreCase("EditText")){
                    double result = checkTag(tag);
                    if (result <= 4.5 && result != -1){
                        holder.registerProblem(tag,
                                "Color contrast between text and background does not meet the WCAG standard");
                    }
                }

            }
        };
    }

    public boolean isColor(String color) {

        String pattern = "^#[0-9A-Fa-f]{6}$";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(color);

        if (matcher.matches()) {
            System.out.println("String is a color");
            return true;
        } else return false;

    }

    public double checkTag(XmlTag tag) {

        boolean checkContrastFlag1 = false;
        boolean checkContrastFlag2 = false;

        double result = -1;

        Color foreground = null;
        Color background = null;

        if (tag.getAttributeValue("android:textColor") != null) {
            try {
                String tempcolor = tag.getAttributeValue("android:textColor").substring(0, 7);
                foreground = Color.decode(tempcolor);
                checkContrastFlag1 = true;
            } catch (Exception e) {
                //to-do
            }
        }
        if (tag.getAttributeValue("android:background") != null) {
            try {
                String tempcolor = tag.getAttributeValue("android:background").substring(0, 7);
                background = Color.decode(tempcolor);
                checkContrastFlag2 = true;
            } catch (Exception e) {
                //to-do
            }
        }

        if (checkContrastFlag1 && checkContrastFlag2) {

            result = checkContrast(foreground, background);
        }

        return result;
    }

    public double checkContrast(Color colore1, Color colore2) {

        ArrayList<Color> colori = new ArrayList<>();
        ArrayList<Double> relativeLuminances = new ArrayList<>();
        colori.add(colore1);
        colori.add(colore2);

        for (Color colore : colori) {

            double red;
            double green;
            double blue;

            if (colore.getRed() <= 0.03928) red = colore.getRed() / 12.92;
            else red = Math.pow(((colore.getRed() + 0.055) / 1.055), 2.4);

            if (colore.getGreen() <= 0.03928) green = colore.getGreen() / 12.92;
            else green = Math.pow(((colore.getGreen() + 0.055) / 1.055), 2.4);

            if (colore.getBlue() <= 0.03928) blue = colore.getBlue() / 12.92;
            else blue = Math.pow(((colore.getBlue() + 0.055) / 1.055), 2.4);

            relativeLuminances.add((0.2126 * red) + (0.0722 * blue) + (0.7152 * green));
        }

        return ((Math.max(relativeLuminances.get(0), relativeLuminances.get(1)) + 0.05) / (Math.min(relativeLuminances.get(0), relativeLuminances.get(1)) + 0.05));
    }
}
