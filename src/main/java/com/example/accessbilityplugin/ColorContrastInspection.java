package com.example.accessbilityplugin;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlFile;
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
                    System.out.println("result contrast:"+result);
                    if (result <= 4.5 && result != -1) {
                        holder.registerProblem(tag,
                                "Color contrast between text and background does not meet the WCAG standard");
                    }
                }
                if (tag.getName().equalsIgnoreCase("EditText")) {
                    double result = checkTag(tag);
                    System.out.println("result contrast:"+result);
                    if (result <= 4.5 && result != -1) {
                        holder.registerProblem(tag,
                                "Color contrast between text and background does not meet the WCAG standard");
                    }
                }

            }
        };
    }

    public boolean isColor(String color) {

        String pattern = "^#[0-9A-Fa-f]{8}$";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(color);

        if (matcher.matches()) {
            return true;
        } else return false;

    }

    public double checkTag(XmlTag tag) {

        boolean checkContrastFlag1 = false;
        boolean checkContrastFlag2 = false;

        double result = -1;

        Color foreground = null;
        Color background = null;

        if ((tag.getAttributeValue("android:textColor") != null) && isColor(tag.getAttributeValue("android:textColor"))) {
            try {
                String tempcolor = tag.getAttributeValue("android:textColor");
                if(tempcolor.length() >= 9) {
                    foreground = Color.decode(trimColor(tempcolor));
                    checkContrastFlag1 = true;
                }
            } catch (Exception e) {
                //to-do
            }
        }
        if ((tag.getAttributeValue("android:background") != null) && isColor(tag.getAttributeValue("android:background"))) {
            try {
                String tempcolor = tag.getAttributeValue("android:background");
                if(tempcolor.length() >= 9) {
                    background = Color.decode(trimColor(tempcolor));
                    checkContrastFlag2 = true;
                }
            } catch (Exception e) {
                //to-do
            }
        }

        if (!checkContrastFlag1) {
            String temp = tag.getAttributeValue("android:textColor");
            if (temp != null && temp.length() > 7) {

                String temp1 = temp.substring(7);
                String projectPath = tag.getProject().getBasePath();
                String filepath = "app/src/main/res/values/colors.xml";
                VirtualFile vfile = LocalFileSystem.getInstance().findFileByPath(projectPath + "/" + filepath);
                if (vfile != null) {
                    PsiFile pFile = PsiManager.getInstance(tag.getProject()).findFile(vfile);
                    XmlFile colorsFile = (XmlFile) pFile;
                    if(colorsFile != null){

                        XmlTag root = colorsFile.getRootTag();
                        XmlTag[] colori = root.getSubTags();

                        for(XmlTag colore : colori){
                            if(colore.getAttributeValue("name").equalsIgnoreCase(temp1)){
                                //System.out.println(trimColor((colore.getValue().getTrimmedText())));
                                foreground = Color.decode(trimColor(colore.getValue().getTrimmedText()));
                                checkContrastFlag1 = true;
                            }
                        }
                    }
                } else {
                    System.out.println("vfile è null");
                }
            }
        }

        if (!checkContrastFlag2) {
            String temp = tag.getAttributeValue("android:background");
            if (temp != null && temp.length() > 7) {
                String temp1 = temp.substring(7);
                String projectPath = tag.getProject().getBasePath();
                String filepath = "app/src/main/res/values/colors.xml";
                VirtualFile vfile = LocalFileSystem.getInstance().findFileByPath(projectPath + "/" + filepath);
                if (vfile != null) {
                    PsiFile pFile = PsiManager.getInstance(tag.getProject()).findFile(vfile);
                    XmlFile colorsFile = (XmlFile) pFile;
                    if(colorsFile != null){

                        XmlTag root = colorsFile.getRootTag();
                        XmlTag[] colori = root.getSubTags();

                        for(XmlTag colore : colori){
                            if(colore.getAttributeValue("name").equalsIgnoreCase(temp1)){
                                //System.out.println(trimColor((colore.getValue().getTrimmedText())));
                                background = Color.decode(trimColor(colore.getValue().getTrimmedText()));
                                checkContrastFlag2 = true;
                            }
                        }
                    }
                } else {
                    System.out.println("vfile è null");
                }
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

            double red = colore.getRed()/255.0;
            double green = colore.getGreen()/255.0;
            double blue = colore.getBlue()/255.0;

            //System.out.println("Red before:"+red);
            //System.out.println("Blue before:"+blue);
            //System.out.println("Green before:"+green);

            if (red <= 0.03928) red = red / 12.92;
            else red = Math.pow(((red + 0.055) / 1.055), 2.4);

            if (green <= 0.03928) green = green / 12.92;
            else green = Math.pow(((green + 0.055) / 1.055), 2.4);

            if (blue <= 0.03928) blue = blue / 12.92;
            else blue = Math.pow(((blue + 0.055) / 1.055), 2.4);

            //System.out.println("Red after:"+red);
            //System.out.println("Blue after:"+blue);
            //System.out.println("green after:"+green);

            relativeLuminances.add((0.2126 * red) + (0.0722 * blue) + (0.7152 * green));
        }

        return ((Math.max(relativeLuminances.get(0), relativeLuminances.get(1)) + 0.05) / (Math.min(relativeLuminances.get(0), relativeLuminances.get(1)) + 0.05));
    }

    public String trimColor(String input){
        String first = input.substring(0,1);
        String second = input.substring(3,9);
        //System.out.println("OUTPUT TRIMCOLOR: "+first+second);
        return first+second;
    }
}

