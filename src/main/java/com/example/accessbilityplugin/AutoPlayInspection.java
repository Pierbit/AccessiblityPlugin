package com.example.accessbilityplugin;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AutoPlayInspection extends LocalInspectionTool {

    public @NotNull PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {

        return new JavaElementVisitor() {
            @Override
            public void visitMethod(PsiMethod method) {
                super.visitMethod(method);

                PsiDeclarationStatement declarationStatement;
                PsiCodeBlock codeblock = method.getBody();
                if(codeblock != null){

                    PsiElement[] elements = codeblock.getChildren();
                    for(PsiElement elem: elements){

                        if(elem instanceof PsiDeclarationStatement){

                            String variableName = null;
                            declarationStatement = (PsiDeclarationStatement) elem;

                            PsiElement[] elements1 = declarationStatement.getDeclaredElements();
                            PsiLocalVariable variable = (PsiLocalVariable) elements1[0];

                            String matchingClass = variable.getType().getCanonicalText();

                            if(matchingClass.equalsIgnoreCase("android.widget.VideoView")){
                                variableName = variable.getName();
                            }

                            if(variableName != null){

                                for(PsiElement el: elements){

                                    if(el instanceof PsiExpressionStatement){
                                        PsiExpressionStatement temp = (PsiExpressionStatement) el;
                                        if(temp.getText().equalsIgnoreCase(variableName+".start();")){
                                            holder.registerProblem(temp,"start() should not be used, Video MUST NOT start automatically, it must wait user input.", ProblemHighlightType.ERROR);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        };
    }
}
