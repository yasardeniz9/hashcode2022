package com.common.hashcode;

import com.common.hashcode.model.Contributor;
import com.common.hashcode.model.Project;
import com.common.hashcode.model.Role;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileOperation {

  private static String filePathIncoming = "M:\\HashCode\\2022\\Inputs";
  private static String filePathOutgoing = "M:\\HashCode\\2022\\Outputs";
  private static final Charset charset = Charset.forName("US-ASCII");

  public static Map<String, ProblemSpace> getFileInput() {
    Map<String, ProblemSpace> mapOfFiles = new HashMap<>();

    try (Stream<Path> walk = Files.walk(Paths.get(filePathIncoming))) {

      List<Path> fileNames = walk.filter(Files::isRegularFile).collect(Collectors.toList());

      fileNames.forEach(file -> {
        ProblemSpace problemSpace = new ProblemSpace();

        try (BufferedReader bufferedReader = Files.newBufferedReader(file, charset)) {
          //read first line
          String line = bufferedReader.readLine();
          String[] firstLine = line.split(" ");
          Integer contributorCount = Integer.valueOf(firstLine[0]);
          Integer projectCount = Integer.valueOf(firstLine[1]);

          //read contributors
          for (int i = 0; i < contributorCount; i++) {
            String contributorLine = bufferedReader.readLine();
            String[] contributorAndSkillCount = contributorLine.split(" ");
            Contributor contributor = new Contributor(contributorAndSkillCount[0]);
            int skillCount = Integer.parseInt(contributorAndSkillCount[1]);
            for (int j = 0; j < skillCount; j++) {
              String skillLine = bufferedReader.readLine();
              String[] skillAndLevel = skillLine.split(" ");
              contributor.addSkill(skillAndLevel[0], Integer.parseInt(skillAndLevel[1]));
            }
            problemSpace.addContributor(contributor);
          }

          //read projects
          for (int i = 0; i < projectCount; i++) {
            String projectLine = bufferedReader.readLine();
            String[] projectLineInfo = projectLine.split(" ");
            Project project = new Project(projectLineInfo[0], Integer.parseInt(projectLineInfo[1]),
                Integer.parseInt(projectLineInfo[2]), Integer.parseInt(projectLineInfo[3]));
            int roleCount = Integer.parseInt(projectLineInfo[4]);
            for (int j = 0; j < roleCount; j++) {
              String roleLine = bufferedReader.readLine();
              String[] skillAndLevel = roleLine.split(" ");
              project.addRole(new Role(skillAndLevel[0], Integer.parseInt(skillAndLevel[1])));
            }
            problemSpace.addProject(project);
          }
          mapOfFiles.put(file.getFileName().toString(), problemSpace);
        } catch (IOException e) {
          e.printStackTrace();
        }
      });

    } catch (IOException e) {
      e.printStackTrace();
    }
    return mapOfFiles;
  }

  public static void writeFileOutput(String fileName, ProblemResult problemResult) {
    Path filePath = Paths
        .get(filePathOutgoing + "/" + fileName + "_" + problemResult.getTotalScore() + ".out");

    try (BufferedWriter bufferedWriter = Files.newBufferedWriter(filePath, charset)) {
      bufferedWriter.write(String.valueOf(problemResult.getPlannedProjects().size()));
      bufferedWriter.newLine();
      List<Project> plannedProjects = problemResult.getPlannedProjects();

      for (Project project : plannedProjects) {
        bufferedWriter.write(project.getName());
        bufferedWriter.newLine();
        StringBuilder contributorNames = new StringBuilder();
        project.getRoles().forEach(r -> {
          contributorNames.append(r.getContributor().getName());
          contributorNames.append(" ");
        });
        contributorNames.deleteCharAt(contributorNames.length()-1);
        bufferedWriter.write(contributorNames.toString());
        bufferedWriter.newLine();
      }

    } catch (IOException e) {
      System.err.format("IOException: %s%n", e);
    }
  }
}
