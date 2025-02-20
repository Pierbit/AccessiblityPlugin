package com.example.accessbilityplugin;

import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ex.InspectionToolWrapper;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class SelectAndReportAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if(project==null) return;

        FileChooserDescriptor descriptor = new FileChooserDescriptor(true, false, false, false, false, true);
        Messages.showInfoMessage("Select files to inspect", "Generate report");
        VirtualFile[] files = FileChooser.chooseFiles(descriptor, project, null);

        FileChooserDescriptor descriptor1 = new FileChooserDescriptor(false, true, false, false, false, false);
        Messages.showInfoMessage("Select directory to save report", "Generate report");
        VirtualFile directory = FileChooser.chooseFile(descriptor1, project, null);
        String dirPath = directory.getPath() + "/" + "Accessiblity_report.html";

        if(files.length == 0) return;

        ArrayList<LocalInspectionTool> ispezioni = initializeInspections();

        PsiManager psiManager = PsiManager.getInstance(project);
        InspectionManager inspectionManager = InspectionManager.getInstance(project);
        BufferedWriter writer;

        try {
            writer = new BufferedWriter(new FileWriter(dirPath));
            writer.write("<!DOCTYPE html>\n");
            writer.write("<html lang=\"it\">\n");
            writer.write("<head>\n");
            writer.write("<meta charset=\"UTF-8\">\n");
            writer.write("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
            writer.write("<title>Accessibility Report</title>\n");
            writer.write("<style>\n");
            writer.write("  body { font-family: Arial, sans-serif; margin: 20px; }\n");
            writer.write("  table { width: 100%; border-collapse: collapse; }\n");
            writer.write("  th, td { padding: 8px; text-align: left; border: 1px solid #ddd; }\n");
            writer.write("  th { background-color: #f2f2f2; }\n");
            writer.write("</style>\n");
            writer.write("</head>\n");
            writer.write("<body>\n");
            writer.write("<h1 style=\"text-align: center;\">[Accessibility Report for each file selected]</h1>\n");
            writer.write("</body>\n");
            writer.write("</html>\n");
            writer.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        for(VirtualFile vf : files){
            PsiFile psiFile = psiManager.findFile(vf);
            ProgressManager.getInstance().run(new Task.Backgroundable(project, "Creating report") {
                @Override
                public void run(@NotNull ProgressIndicator indicator) {

                    ReadAction.run(() -> {

                        try {

                            ArrayList<AccessibilityIssue> issues = new ArrayList<>();

                            for(LocalInspectionTool ispezione : ispezioni){

                                List<ProblemDescriptor> problems = inspectionManager.defaultProcessFile(ispezione, psiFile);

                                for(ProblemDescriptor problem: problems){

                                    issues.add(new AccessibilityIssue(ispezione.getID(),
                                            psiFile.getName(), problem.getDescriptionTemplate()));

                                }
                            }

                            generateReportTable(issues, dirPath);

                        }catch (Exception e){
                            //to-do
                        }
                    });
                }
            });
        }
    }

    public void generateReportTable(ArrayList<AccessibilityIssue> accessibilityIssues, String dirPath){

        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(dirPath, true));
            writer.write("<table>\n");
            writer.write("<tr>\n");
            writer.write("<th>File di riferimento</th>\n");
            writer.write("<th>Ispezione di riferimento</th>\n");
            writer.write("<th>Problema riscontrato</th>\n");
            writer.write("</tr>\n");

            for(AccessibilityIssue issue: accessibilityIssues ){
                writer.write("<tr>\n");
                writer.write("<td>"+issue.getFileName()+"</td>\n");
                writer.write("<td>"+issue.getInspectionReference()+"</td>\n");
                writer.write("<td>"+issue.getIssue()+"</td>\n");
                writer.write("</tr>\n");
            }

            writer.write("</table>\n");
            writer.close();

        }catch (IOException e){
            //to-do
        }
    }
    public ArrayList<LocalInspectionTool> initializeInspections(){

        ArrayList<LocalInspectionTool> ispezioni = new ArrayList<>();
        ColorContrastInspection inspection1 = new ColorContrastInspection();
        ColorMeaningInspection inspection2 = new ColorMeaningInspection();
        CustomElemInspection inspection3 = new CustomElemInspection();
        TitleInspection inspection4 = new TitleInspection();
        TouchTargetInspection inspection5 = new TouchTargetInspection();
        LabelsInspection inspection6 = new LabelsInspection();

        ispezioni.add(inspection1);
        ispezioni.add(inspection2);
        ispezioni.add(inspection3);
        ispezioni.add(inspection4);
        ispezioni.add(inspection5);
        ispezioni.add(inspection6);

        return ispezioni;
    }
}
